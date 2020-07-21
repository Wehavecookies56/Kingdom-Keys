package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class SCRecalculateEyeHeight {

	public SCRecalculateEyeHeight() {
	}

	public void encode(PacketBuffer buffer) {
	
	}

	public static SCRecalculateEyeHeight decode(PacketBuffer buffer) {
		SCRecalculateEyeHeight msg = new SCRecalculateEyeHeight();
		return msg;
	}

	public static void handle(final SCRecalculateEyeHeight message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = KingdomKeys.proxy.getClientPlayer();
			player.recalculateSize();
		});
		ctx.get().setPacketHandled(true);
	}

}
