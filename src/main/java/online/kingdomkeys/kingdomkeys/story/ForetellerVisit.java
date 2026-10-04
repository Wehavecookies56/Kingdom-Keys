package online.kingdomkeys.kingdomkeys.story;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.mob.ForetellerEntity;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.OpenSpot;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;

import java.util.List;
import java.util.Set;

public class ForetellerVisit {
	public static final ResourceLocation FIRST_MEETING = KingdomKeys.rl("foreteller_first_meeting");

	private static final int DAWN_FROM = 0, DAWN_UNTIL = 2000;

	private static final int CHECK_INTERVAL = 20;

	private static final double[] FAR_DISTANCES = {20D, 16D, 24D, 12D};
	private static final double[] NEAR_DISTANCES = {4D, 3D, 5D, 2D};

	private static final Set<ResourceKey<Level>> CLOSED = Set.of(
			ModDimensions.DIVE_TO_THE_HEART,
			ModDimensions.STATION_OF_SORROW,
			ModDimensions.CASTLE_OBLIVION,
			ModDimensions.DAYBREAK_TOWN
	);

	@SubscribeEvent
	public void onServerTick(ServerTickEvent.Post event) {
		if (event.getServer().getTickCount() % CHECK_INTERVAL != 0 || !ModConfigs.getServerConfig().storyProgress.get()) {
			return;
		}

		for (ServerPlayer player : List.copyOf(event.getServer().getPlayerList().getPlayers())) {
			PlayerData data = PlayerData.get(player);

			if (data == null || !data.hasFlag(StoryFlags.FORETELLER_OWED) || !isMorning(player.serverLevel())) {
				continue;
			}

			visit(player, data);
		}
	}

	private static boolean isMorning(ServerLevel level) {
		long time = level.getDayTime() % 24000L;
		return time >= DAWN_FROM && time < DAWN_UNTIL;
	}

	public static boolean visit(ServerPlayer player, PlayerData data) {
		if (!canBeVisited(player, data)) {
			return false;
		}

		BlockPos spot = findSpot(player);

		if (spot == null) {
			return false;
		}

		return arrive(player, data, spot);
	}

	public static boolean force(ServerPlayer player, PlayerData data) {
		BlockPos spot = findSpot(player);
		return spot != null && data.hasUnion() && arrive(player, data, spot);
	}

	private static boolean arrive(ServerPlayer player, PlayerData data, BlockPos spot) {
		ForetellerEntity master = ModEntities.TYPE_FORETELLER.get().create(player.serverLevel());

		if (master == null) {
			return false;
		}

		Union union = data.getUnion();
		master.setUnion(union);
		master.moveTo(spot.getX() + 0.5D, spot.getY(), spot.getZ() + 0.5D, player.getYRot() + 180F, 0F);
		master.finalizeSpawn(player.serverLevel(), player.serverLevel().getCurrentDifficultyAt(spot), MobSpawnType.EVENT, null);
		master.wearUnionRobes();

		// He has come for one conversation, not to run a shop
		master.setDialogue(FIRST_MEETING);
		master.lookAt(EntityAnchorArgument.Anchor.EYES, player.getEyePosition());
		master.seek(player);

		if (!player.serverLevel().addFreshEntity(master)) {
			return false;
		}

		data.removeFlag(StoryFlags.FORETELLER_OWED);
		data.addFlag(StoryFlags.FORETELLER_VISITED);
		PacketHandler.sendTo(new SCSyncPlayerData(player), player);

		return true;
	}

	private static boolean canBeVisited(ServerPlayer player, PlayerData data) {
		return data.hasUnion() && !data.isOrgMember() && player.isAlive() && !player.isSpectator() && !CLOSED.contains(player.level().dimension()) && player.serverLevel().canSeeSky(player.blockPosition());
	}

	private static BlockPos findSpot(ServerPlayer player) {
		ServerLevel level = player.serverLevel();
		EntityType<?> type = ModEntities.TYPE_FORETELLER.get();

		Vec3 spot = OpenSpot.find(level, null, player.position(), player.getYRot() + 180F, FAR_DISTANCES, type, 0.4D);

		if (spot == null) {
			spot = OpenSpot.find(level, player.getEyePosition(), player.position(), player.getYRot(), NEAR_DISTANCES, type, 0.1D);
		}

		return spot == null ? null : BlockPos.containing(spot);
	}
}
