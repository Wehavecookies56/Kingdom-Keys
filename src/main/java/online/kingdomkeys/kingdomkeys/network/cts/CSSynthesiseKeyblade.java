package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;

public class CSSynthesiseKeyblade {

	String name;

	public CSSynthesiseKeyblade() {
	}

	public CSSynthesiseKeyblade(String name) {
		this.name = name;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.name.length());
		buffer.writeString(this.name);
	}

	public static CSSynthesiseKeyblade decode(PacketBuffer buffer) {
		CSSynthesiseKeyblade msg = new CSSynthesiseKeyblade();
		int len = buffer.readInt();
		msg.name = buffer.readString(len);
		return msg;
	}

	public static void handle(CSSynthesiseKeyblade message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities props = ModCapabilities.get(player);
			
			String name = message.name.substring("item.kingdomkeys.".length());
			ResourceLocation loc = new ResourceLocation(KingdomKeys.MODID, name);
			KeybladeItem item = (KeybladeItem) ForgeRegistries.ITEMS.getValue(loc);
			
			Iterator<Entry<Material, Integer>> it = item.getRecipe().getMaterials().entrySet().iterator();
			boolean hasMaterials = true;
			while(it.hasNext()) { //Check if the player has the materials (checked serverside just in case)
				Entry<Material, Integer> m = it.next();
				if(props.getMaterialAmount(m.getKey()) < m.getValue()) {
					hasMaterials = false;
				}
			}
			
			if(hasMaterials) { //If the player has the materials substract them and give the item
			Iterator<Entry<Material, Integer>> ite = item.getRecipe().getMaterials().entrySet().iterator();
				while(ite.hasNext()) {
					Entry<Material, Integer> m = ite.next();
					props.removeMaterial(m.getKey(), m.getValue());
				}
				//TODO Item i = item.getRecipe().getResult();
				
				int amount = item.getRecipe().getAmount();
				player.inventory.addItemStackToInventory(new ItemStack(item,amount));
			}
			PacketHandler.sendTo(new SCSyncCapabilityPacket(props), (ServerPlayerEntity)player);
		});
		ctx.get().setPacketHandled(true);
	}

}
