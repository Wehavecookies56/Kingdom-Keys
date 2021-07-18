package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.util.IExtendedReach;

public class CSExtendedReach {

	int entityId;

	public CSExtendedReach() {
	}

	public CSExtendedReach(int entityId) {
		this.entityId = entityId;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.entityId);
	}

	public static CSExtendedReach decode(PacketBuffer buffer) {
		CSExtendedReach msg = new CSExtendedReach();
		msg.entityId = buffer.readInt();
		return msg;
	}

	public static void handle(CSExtendedReach message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
	        Entity theEntity = player.world.getEntityByID(message.entityId);
	        if (ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), ItemStack.EMPTY)) {
	            return;
	        }
	        if (player.getHeldItemMainhand().getItem() instanceof IExtendedReach) {
	            IExtendedReach theExtendedReachWeapon = (IExtendedReach) player.getHeldItemMainhand().getItem();
	            double distanceSq = player.getDistanceSq(theEntity);
	            double reachSq = theExtendedReachWeapon.getReach() * theExtendedReachWeapon.getReach();
	            if (reachSq >= distanceSq) {
	                player.attackTargetEntityWithCurrentItem(theEntity);
	            }
	        }

		});
		ctx.get().setPacketHandled(true);
	}

}
