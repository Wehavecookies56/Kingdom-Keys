package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.menu.GummiHangarMenu;
import online.kingdomkeys.kingdomkeys.network.Packet;
import static online.kingdomkeys.kingdomkeys.block.gummi.GummiHangarBlock.SHOW_LINES;

public record CSShowHangarLinesPacket(int containerID) implements Packet {

	public static final Type<CSShowHangarLinesPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_show_hangar_lines"));

	public static final StreamCodec<FriendlyByteBuf, CSShowHangarLinesPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			CSShowHangarLinesPacket::containerID,
			CSShowHangarLinesPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		if (player.containerMenu.containerId != containerID)
			return;

		GummiHangarMenu container = (GummiHangarMenu) player.containerMenu;
		BlockPos origin = container.TE.getBlockPos();
		Level level = player.level();

		BlockState hangar = level.getBlockState(origin);
        level.setBlockAndUpdate(origin,hangar.setValue(SHOW_LINES, hangar.getValue(SHOW_LINES).next()));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}