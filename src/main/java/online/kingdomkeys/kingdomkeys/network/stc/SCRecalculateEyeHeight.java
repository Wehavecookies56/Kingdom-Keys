package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;

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
		ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.recalcEyeHeight()));
		ctx.get().setPacketHandled(true);
	}

}
