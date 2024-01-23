package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;

public class SCOpenCODoorGui {

	public BlockPos pos;
	
	public SCOpenCODoorGui() { }
	
	public SCOpenCODoorGui(BlockPos pos) {
		this.pos = pos;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
	}

	public static SCOpenCODoorGui decode(FriendlyByteBuf buffer) {
		SCOpenCODoorGui msg = new SCOpenCODoorGui();
		msg.pos = buffer.readBlockPos();
		return msg;
	}

	public static void handle(final SCOpenCODoorGui message, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
			ctx.get().enqueueWork(() -> ClientHandler.handle(message));
		ctx.get().setPacketHandled(true);
	}

	public static class ClientHandler {
		@OnlyIn(Dist.CLIENT)
		public static void handle(SCOpenCODoorGui message) {
			DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.openCODoorGui(message));
		}
	}

}
