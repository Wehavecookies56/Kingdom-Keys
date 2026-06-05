package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;

import java.util.ArrayList;
import java.util.List;

public class FaithEntityController extends BaseMagicProjectile {
	private static final int BEAM_COUNT = 12;
	private static final int SPAWN_INTERVAL = 2;
	private static final double START_RADIUS = 5D;

	private final List<FaithBeamEntity> beams = new ArrayList<>();

	int maxTicks = 80;
	float dmgMult = 1;
	LivingEntity lockedOnEntity;

	public FaithEntityController(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public FaithEntityController(Level world, LivingEntity player, float dmgMult, LivingEntity lockedOnEntity) {
		super(ModEntities.TYPE_FAITH.get(), player, world);
		this.dmgMult = dmgMult;
		this.lockedOnEntity = lockedOnEntity;
		this.damageType = KKDamageTypes.LIGHT;
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}

		if (getOwner() == null) {
			remove(RemovalReason.KILLED);
			return;
		}

		if(getOwner() instanceof Player player) {
			PlayerData playerData = PlayerData.get(player);
			playerData.setMagicCasttimeTicks(tickCount < 40 ? 10 : 0);
		}

		if (!level().isClientSide) {
			if (tickCount % SPAWN_INTERVAL == 0) {
				int beamIndex = tickCount / SPAWN_INTERVAL;

				if (beamIndex <= BEAM_COUNT) {
					double angle = (Math.PI * 2D / BEAM_COUNT) * beamIndex;

					double x = getOwner().getX() + Math.cos(angle) * START_RADIUS;
					double z = getOwner().getZ() + Math.sin(angle) * START_RADIUS;
					int groundY = level().getHeight(Types.WORLD_SURFACE, (int) Math.floor(x), (int) Math.floor(z));

					FaithBeamEntity beam = new FaithBeamEntity(level(), (LivingEntity) getOwner(), dmgMult, x, groundY + 0.1D, z);
					beam.setBaseAngle(angle);
					level().addFreshEntity(beam);
					playSound(ModSounds.lightBeam.get());
					beams.add(beam);
				}
			}
		}

		if (tickCount == 40) {
			playSound(ModSounds.lightBeam.get(),1,0.5F);
			for (FaithBeamEntity beam : beams) {
				if (beam != null && beam.isAlive()) {
					beam.startExpanding(getOwner().getX(), getOwner().getZ());
				}
			}
		}

		super.tick();
	}

	@Override
	protected void onHit(HitResult result) {

	}
}

