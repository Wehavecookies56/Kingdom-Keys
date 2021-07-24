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
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
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

public class CSSynthesiseKeyblade {

	ResourceLocation name;

	public CSSynthesiseKeyblade() {
	}

	public CSSynthesiseKeyblade(ResourceLocation name) {
		this.name = name;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeResourceLocation(this.name);
	}

	public static CSSynthesiseKeyblade decode(FriendlyByteBuf buffer) {
		CSSynthesiseKeyblade msg = new CSSynthesiseKeyblade();
		msg.name = buffer.readResourceLocation();
		return msg;
	}

	public static void handle(CSSynthesiseKeyblade message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
			if(player.getInventory().getFreeSlot() > -1) {
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				
				Item item = ForgeRegistries.ITEMS.getValue(message.name);
				
				Recipe recipe = RecipeRegistry.getInstance().getValue(item.getRegistryName());
				Iterator<Entry<Material, Integer>> it = recipe.getMaterials().entrySet().iterator();
				boolean hasMaterials = true;
				while(it.hasNext()) { //Check if the player has the materials (checked serverside just in case)
					Entry<Material, Integer> m = it.next();
					if(playerData.getMaterialAmount(m.getKey()) < m.getValue()) {
						hasMaterials = false;
					}
				}
				
				if(hasMaterials && playerData.getMunny() >= recipe.getCost()) { //If the player has the materials substract them and give the item
					playerData.setMunny(playerData.getMunny() - recipe.getCost());
					Iterator<Entry<Material, Integer>> ite = recipe.getMaterials().entrySet().iterator();
					while(ite.hasNext()) {
						Entry<Material, Integer> m = ite.next();
						playerData.removeMaterial(m.getKey(), m.getValue());
					}
					
					Item i = recipe.getResult();
					
					int amount = recipe.getAmount();
					player.getInventory().add(new ItemStack(i,amount));
					
					if(i instanceof KeychainItem && ModConfigs.heartlessSpawningMode == SpawningMode.AFTER_KEYCHAIN) {
						IWorldCapabilities worldData = ModCapabilities.getWorld(player.level);
						worldData.setHeartlessSpawnLevel(1);
						PacketHandler.sendToAllPlayers(new SCSyncWorldCapability(worldData));
					}
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)player);
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
