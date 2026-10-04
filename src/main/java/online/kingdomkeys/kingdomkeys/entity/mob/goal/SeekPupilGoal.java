package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.util.OpenSpot;

import java.util.EnumSet;
import java.util.UUID;
import java.util.function.Supplier;

public class SeekPupilGoal extends Goal {
	private static final double REACHED = 3D, LOST = 40D;
	private static final double[] CATCH_UP_DISTANCES = {5D, 4D, 6D, 3D};

	private final PathfinderMob seeker;
	private final Supplier<UUID> pupilId;
	private Player pupil;

	public SeekPupilGoal(PathfinderMob seeker, Supplier<UUID> pupilId) {
		this.seeker = seeker;
		this.pupilId = pupilId;
		setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
	}

	@Override
	public boolean canUse() {
		pupil = find();
		return pupil != null && seeker.distanceTo(pupil) > REACHED + 1.5D;
	}

	@Override
	public boolean canContinueToUse() {
		return pupil != null && pupil == find() && seeker.distanceTo(pupil) > REACHED;
	}

	@Override
	public void stop() {
		seeker.getNavigation().stop();
		pupil = null;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void tick() {
		seeker.getLookControl().setLookAt(pupil, 30F, 30F);

		if (seeker.tickCount % 10 != 0) {
			return;
		}
		if (seeker.distanceTo(pupil) > LOST || seeker.getNavigation().isStuck() || !seeker.getNavigation().moveTo(pupil, 1D)) {
			Vec3 spot = OpenSpot.find(seeker.level(), pupil.getEyePosition(), pupil.position(), pupil.getYRot(), CATCH_UP_DISTANCES, seeker.getType(), 0.1D);
			if (spot != null) {
				seeker.getNavigation().stop();
				seeker.teleportTo(spot.x, spot.y, spot.z);
			}
		}
	}

	private Player find() {
		UUID id = pupilId.get();
		Player player = id == null ? null : seeker.level().getPlayerByUUID(id);
		return player != null && player.isAlive() && !player.isSpectator() ? player : null;
	}
}
