package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowWarning;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldData;

import javax.annotation.Nullable;

public record CSStruggleSettings(Struggle struggle) implements Packet {

	public static final Type<CSStruggleSettings> TYPE = new Type<>(KingdomKeys.rl("cs_struggle_settings"));

	public static final StreamCodec<FriendlyByteBuf, CSStruggleSettings> STREAM_CODEC = StreamCodec.composite(
			Struggle.STREAM_CODEC,
			CSStruggleSettings::struggle,
			CSStruggleSettings::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		WorldData worldData = WorldData.get(player.getServer());
		Struggle match = worldData.getStruggleFromBlockPos(struggle.blockPos);

		if (match == null) {
			return;
		}

		if (match.getOwnerId() != null && !match.getOwnerId().equals(player.getUUID())) {
			return;
		}

		int range = ModConfigs.SERVER.struggleArenaRange.get();

		if (tooFar(struggle.getC1(), match.blockPos, range) || tooFar(struggle.getC2(), match.blockPos, range) || tooFar(struggle.getSpectatorPos(), match.blockPos, range)) {
			Component warning = Component.translatable(Strings.WarningStruggleRange, range);
			player.displayClientMessage(warning, true);
			SCShowWarning.send(player, warning);
			return;
		}

		match.setSize(struggle.getSize());
		match.setDamageMult(struggle.getDamageMult());
		match.setRoundTimeSeconds(struggle.getRoundTimeSeconds());
		match.setStartingScore(struggle.getStartingScore());
		match.setName(struggle.getName());
		match.setC1(struggle.c1);
		match.setC2(struggle.c2);
		match.setSpectatorPos(struggle.getSpectatorPos());
		match.setMode(struggle.getMode());

		worldData.setDirty();
		PacketHandler.sendToAll(new SCSyncWorldData(player.getServer()));
	}

	private static boolean tooFar(@Nullable BlockPos pos, BlockPos board, int range) {
		if (pos == null) {
			return false;
		}

		int dx = pos.getX() - board.getX();
		int dz = pos.getZ() - board.getZ();
		int dy = Math.abs(pos.getY() - board.getY());

		return dx * dx + dz * dz > range * range || dy > range;
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
