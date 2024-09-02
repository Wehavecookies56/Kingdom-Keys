package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.SpawningMode;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowMessagesPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldData;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.Title;

public record CSSynthesiseRecipe(ResourceLocation name) implements Packet {

	public static final Type<CSSynthesiseRecipe> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_synthesis_recipe"));

	public static final StreamCodec<FriendlyByteBuf, CSSynthesiseRecipe> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC,
			CSSynthesiseRecipe::name,
			CSSynthesiseRecipe::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		if(player.getInventory().getFreeSlot() > -1) {
			PlayerData playerData = PlayerData.get(player);

			Recipe recipe = RecipeRegistry.getInstance().getValue(name);
			int stacksToGive = Utils.stacksForItemAmount(new ItemStack(recipe.getResult()), recipe.getAmount());
			if (stacksToGive <= Utils.getFreeSlotsForPlayer(player)) {
				Iterator<Entry<Material, Integer>> it = recipe.getMaterials().entrySet().iterator();
				boolean hasMaterials = true;
				boolean enoughMunny = playerData.getMunny() >= recipe.getCost();
				boolean enoughTier = !ModConfigs.requireSynthTier || playerData.getSynthLevel() >= recipe.getTier();

				while (it.hasNext()) { //Check if the player has the materials (checked serverside just in case)
					Entry<Material, Integer> m = it.next();
					if (playerData.getMaterialAmount(m.getKey()) < m.getValue()) {
						hasMaterials = false;
					}
				}

				if (hasMaterials && enoughMunny && enoughTier) { //If the player has the materials substract them and give the item
					playerData.setMunny(playerData.getMunny() - recipe.getCost());
					//playerData.setSynthExperience(600);
					//playerData.setSynthLevel(1);
					playerData.addSynthExperience(10 + recipe.getTier() * 2);

					Iterator<Entry<Material, Integer>> ite = recipe.getMaterials().entrySet().iterator();
					while (ite.hasNext()) {
						Entry<Material, Integer> m = ite.next();
						playerData.removeMaterial(m.getKey(), m.getValue());
					}

					Item i = recipe.getResult();
					ItemStack stack = new ItemStack(i);
					for (int s = 0; s < stacksToGive - 1; s++) {
						player.getInventory().add(new ItemStack(i, stack.getMaxStackSize()));
					}
					int remainder = recipe.getAmount() - ((stacksToGive - 1) * stack.getMaxStackSize());
					if (remainder > 0) {
						player.getInventory().add(new ItemStack(i, remainder));
					}
					if (i instanceof KeychainItem && ModConfigs.heartlessSpawningMode == SpawningMode.AFTER_KEYCHAIN) {
						WorldData worldData = WorldData.get(player.getServer());
						if (worldData.getHeartlessSpawnLevel() == 0) {
							List<Title> titles = List.of(
									new Utils.Title("", Strings.HeartlessIntro1),
									new Utils.Title("", Strings.HeartlessIntro2),
									new Utils.Title("", Strings.HeartlessIntro3));

							//for(Player p : Utils.getAllPlayers(player.level().getServer())){
							PacketHandler.sendToAll(new SCShowMessagesPacket(titles));
							//}
							Utils.playSoundToEveryone((ServerLevel) player.level(), SoundEvents.WITHER_SPAWN, 1F, 1F);
						}
						worldData.setHeartlessSpawnLevel(1);
						PacketHandler.sendToAll(new SCSyncWorldData(player.getServer()));
					}
				}
				PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
			}
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}


























