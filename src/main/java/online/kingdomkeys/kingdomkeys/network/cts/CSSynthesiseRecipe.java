package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.SpawningMode;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldCapability;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSSynthesiseRecipe {

	ResourceLocation name;

	public CSSynthesiseRecipe() {
	}

	public CSSynthesiseRecipe(ResourceLocation name) {
		this.name = name;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(this.name);
	}

	public static CSSynthesiseRecipe decode(FriendlyByteBuf buffer) {
		CSSynthesiseRecipe msg = new CSSynthesiseRecipe();
		msg.name = buffer.readResourceLocation();
		return msg;
	}

	public static void handle(CSSynthesiseRecipe message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			if(player.getInventory().getFreeSlot() > -1) {
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

				Recipe recipe = RecipeRegistry.getInstance().getValue(message.name);
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
						for (int s = 0; s < stacksToGive-1; s++) {
							player.getInventory().add(new ItemStack(i, stack.getMaxStackSize()));
						}
						int remainder = recipe.getAmount() - ((stacksToGive - 1) * stack.getMaxStackSize());
						if (remainder > 0) {
							player.getInventory().add(new ItemStack(i, remainder));
						}
						if (i instanceof KeychainItem && ModConfigs.heartlessSpawningMode == SpawningMode.AFTER_KEYCHAIN) {
							IWorldCapabilities worldData = ModCapabilities.getWorld(player.level());
							worldData.setHeartlessSpawnLevel(1);
							PacketHandler.sendToAllPlayers(new SCSyncWorldCapability(worldData));
						}
					}
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
				}
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
