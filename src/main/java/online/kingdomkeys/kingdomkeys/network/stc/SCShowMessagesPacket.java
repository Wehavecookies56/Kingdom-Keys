package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.Title;

public class SCShowMessagesPacket {

	public List<Title> titles;

	public SCShowMessagesPacket() { }

	public SCShowMessagesPacket(List<Title> titles) {
		this.titles = titles;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeNbt(Utils.Title.writeList(titles));
	}

	public static SCShowMessagesPacket decode(FriendlyByteBuf buffer) {
		SCShowMessagesPacket msg = new SCShowMessagesPacket();
		msg.titles = Utils.Title.readList(buffer.readNbt());
		return msg;
	}

	public static void handle(final SCShowMessagesPacket message, Supplier<NetworkEvent.Context> ctx) {
		if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT)
			ctx.get().enqueueWork(() -> ClientHandler.handle(message));
		ctx.get().setPacketHandled(true);
	}

	public static class ClientHandler {
		@OnlyIn(Dist.CLIENT)
		public static void handle(SCShowMessagesPacket message) {
			DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.showTitles(message));
		}
	}

}
