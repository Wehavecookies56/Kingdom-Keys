package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class SCRecalculateEyeHeight {

	public SCRecalculateEyeHeight() {
	}

	public void encode(FriendlyByteBuf buffer) {
	
	}

	public static SCRecalculateEyeHeight decode(FriendlyByteBuf buffer) {
		SCRecalculateEyeHeight msg = new SCRecalculateEyeHeight();
		return msg;
	}

	public static void handle(final SCRecalculateEyeHeight message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = KingdomKeys.proxy.getClientPlayer();
			player.refreshDimensions();
		});
		ctx.get().setPacketHandled(true);
	}

}
