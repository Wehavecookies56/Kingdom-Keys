package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.integration.epicfight.SeprateClassToAvoidLoadingIssuesExtendedReach;
import online.kingdomkeys.kingdomkeys.util.IExtendedReach;

public class CSExtendedReach {

	int entityId;

	public CSExtendedReach() {
	}

	public CSExtendedReach(int entityId) {
		this.entityId = entityId;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.entityId);
	}

	public static CSExtendedReach decode(FriendlyByteBuf buffer) {
		CSExtendedReach msg = new CSExtendedReach();
		msg.entityId = buffer.readInt();
		return msg;
	}

	public static void handle(CSExtendedReach message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = ctx.get().getSender();
	        Entity theEntity = player.level.getEntity(message.entityId);
			if(SeprateClassToAvoidLoadingIssuesExtendedReach.isBattleMode(player))
				return;
	        if (ItemStack.matches(player.getMainHandItem(), ItemStack.EMPTY)) {
	            return;
	        }
	        if (player.getMainHandItem().getItem() instanceof IExtendedReach) {
	            IExtendedReach theExtendedReachWeapon = (IExtendedReach) player.getMainHandItem().getItem();
	            double distanceSq = player.distanceToSqr(theEntity);
	            double reachSq = theExtendedReachWeapon.getReach() * theExtendedReachWeapon.getReach();
	            if (reachSq >= distanceSq) {
	                player.attack(theEntity);
	            }
	        }

		});
		ctx.get().setPacketHandled(true);
	}

}
