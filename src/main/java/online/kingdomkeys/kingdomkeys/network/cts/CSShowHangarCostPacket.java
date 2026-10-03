package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.menu.GummiHangarMenu;
import online.kingdomkeys.kingdomkeys.network.Packet;

import static online.kingdomkeys.kingdomkeys.block.gummi.GummiHangarBlock.SHOW_COST;

public record CSShowHangarCostPacket(int containerID) implements Packet {
	public static final Type<CSShowHangarCostPacket> TYPE = new Type<>(KingdomKeys.rl("cs_show_hangar_cost"));

	public static final StreamCodec<FriendlyByteBuf, CSShowHangarCostPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT, CSShowHangarCostPacket::containerID,
			CSShowHangarCostPacket::new
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
        level.setBlockAndUpdate(origin,hangar.setValue(SHOW_COST, !hangar.getValue(SHOW_COST)));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}