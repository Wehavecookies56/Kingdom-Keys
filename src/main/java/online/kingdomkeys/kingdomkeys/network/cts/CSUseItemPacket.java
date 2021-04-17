package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.item.KKPotionItem;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class CSUseItemPacket {
	
	int slot;
	String target;
	
	public CSUseItemPacket() {}

	public CSUseItemPacket(int slot) {
		this.slot = slot;
		this.target = "";
	}
	
	public CSUseItemPacket(int slot, String target) {
		this.slot = slot;
		this.target = target;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.slot);
		buffer.writeInt(this.target.length());
		buffer.writeString(this.target);
	}

	public static CSUseItemPacket decode(PacketBuffer buffer) {
		CSUseItemPacket msg = new CSUseItemPacket();
		msg.slot = buffer.readInt();
		int length = buffer.readInt();
		msg.target = buffer.readString(length);
		return msg;
	}

	public static void handle(CSUseItemPacket message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
        		KKPotionItem potion = (KKPotionItem) playerData.getEquippedItem(message.slot).getItem();
        		
        		if(message.target.equals("")) {
            		potion.potionEffect(player);
				} else {
					PlayerEntity targetEntity = Utils.getPlayerByName(player.world, message.target);
	        		potion.potionEffect(targetEntity);
				}
        		playerData.equipItem(message.slot, ItemStack.EMPTY);

				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity)player);
			
		});
		ctx.get().setPacketHandled(true);
	}

}
