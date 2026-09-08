package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.mob.MasterDuelEntity;
import online.kingdomkeys.kingdomkeys.lib.DuelDifficulty;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record CSStartDuel(DuelDifficulty difficulty) implements Packet {
	public static final Type<CSStartDuel> TYPE = new Type<>(KingdomKeys.rl("cs_start_duel"));

	public static final StreamCodec<FriendlyByteBuf, CSStartDuel> STREAM_CODEC = StreamCodec.composite(
			DuelDifficulty.STREAM_CODEC, CSStartDuel::difficulty,
			CSStartDuel::new
	);

	private static final double[] DISTANCES = { 5.0D, 4.0D, 3.0D, 2.0D };
	private static final int MAX_DROP = 3;

	@Override
	public void handle(IPayloadContext context) {
		if (!(context.player() instanceof ServerPlayer player)) {
			return;
		}

		PlayerData playerData = PlayerData.get(player);
		if (playerData == null || !playerData.hasUnion()) {
			return;
		}

		Union union = playerData.getUnion();
		ServerLevel level = player.serverLevel();

		MasterDuelEntity master = new MasterDuelEntity(level, union, difficulty.getLevel(playerData.getLevel()));

		if (!placeMasterForDuel(level, master, player)) {
			return;
		}

		master.setDuelist(player);
		master.setPersistenceRequired();
		master.lookAt(EntityAnchorArgument.Anchor.EYES, player.position());
		master.setYBodyRot(master.getYRot());
		master.setYHeadRot(master.getYRot());

		level.addFreshEntity(master);
		master.setTarget(player);
	}

	private static boolean placeMasterForDuel(ServerLevel level, MasterDuelEntity master, ServerPlayer player) {
		Vec3 from = player.position();
		Vec3 facing = player.getLookAngle().multiply(1.0D, 0.0D, 1.0D);

		Vec3 ahead = facing.lengthSqr() < 1.0E-4D ? Vec3.directionFromRotation(0F, player.getYRot()) : facing.normalize();

		for (double distance : DISTANCES) {
			for (int drop = 0; drop <= MAX_DROP; drop++) {
				Vec3 at = from.add(ahead.scale(distance)).subtract(0.0D, drop, 0.0D);
				master.setPos(at.x, at.y, at.z);
				if (level.noCollision(master)) {
					return true;
				}
			}
		}

		master.setPos(from.x, from.y, from.z);
		return level.noCollision(master);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
