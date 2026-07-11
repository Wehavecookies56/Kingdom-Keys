package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class IceBarrageControllerEntity extends BaseMagicProjectile {
	private static final int MAX_SPIKES = 3;
	private static final int DELAY = 10;
	private static final int BREAK_TIME = 60;

	private final List<IceSpikeEntity> spikes = new ArrayList<>();

	private LivingEntity target;
	private Vec3 spawnPos;

	private int spawnTimer;
	private int spikesSpawned;

	private float formationYaw = Float.NaN;

	public IceBarrageControllerEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
	}

	public IceBarrageControllerEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockedOnEntity) {
		super(ModEntities.TYPE_ICEBARRAGE.get(), player, world);

		this.dmgMult = dmgMult;
		this.lockOnEntity = lockedOnEntity;
		this.target = lockedOnEntity;

		this.damageType = KKDamageTypes.ICE;
		this.maxTicks = 200;
	}

	private LivingEntity findTarget(Player player) {
		List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(5), e -> e != player && Utils.isHostile(e));

		double closest = Double.MAX_VALUE;
		LivingEntity result = null;

		for (LivingEntity entity : entities) {
			double dist = player.distanceToSqr(entity);

			if (dist < closest) {
				closest = dist;
				result = entity;
			}
		}

		return result;
	}

	@Override
	public void tick() {
		super.tick();

		if (level().isClientSide) {
			return;
		}

		if (!(getOwner() instanceof Player player)) {
			discard();
			return;
		}

		// Buscar objetivo solo una vez
		if (tickCount == 1) {

			if (target == null || !target.isAlive()) {
				target = lockOnEntity instanceof LivingEntity living ? living : findTarget(player);
			}

			if (target != null) {
				BlockPos ground = level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, target.blockPosition());
				spawnPos = new Vec3(target.getX(), ground.getY(), target.getZ());
			} else {
				Vec3 look = player.getLookAngle();
				Vec3 front = player.position().add(look.x * 3.0D, 0, look.z * 3.0D);

				BlockPos ground = level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BlockPos.containing(front));
				spawnPos = new Vec3(front.x, ground.getY(), front.z);
			}
			if (Float.isNaN(formationYaw)) {
				formationYaw = level().random.nextFloat() * 360F;
			}
			setPos(spawnPos.x, spawnPos.y, spawnPos.z);
		}

		if (spawnPos == null) {
			return;
		}

		// Spawn icicles
		spawnTimer++;

		if (spikesSpawned < MAX_SPIKES && spawnTimer >= DELAY) {
			spawnTimer = 0;
			spawnIceSpike(spikesSpawned);
			spikesSpawned++;
		}

		// Break icicles
		if (spikesSpawned >= MAX_SPIKES && tickCount >= BREAK_TIME) {
			for (IceSpikeEntity spike : spikes) {
				if (spike != null && spike.isAlive()) {
					spike.startBreaking();
				}
			}
			if (tickCount >= BREAK_TIME + 9) {
				discard();
			}
		}
	}

	private void spawnIceSpike(int variant) {
		IceSpikeEntity spike = new IceSpikeEntity(level(), (LivingEntity) getOwner(), dmgMult);

		spike.setPos(spawnPos.x, spawnPos.y, spawnPos.z);

		spike.setVariant(variant);
		spike.setBaseYaw(formationYaw);

		switch (variant) {
			case 0 -> spike.setTilt(-(20F + level().random.nextFloat() * 15F));
			case 1 -> spike.setTilt(20F + level().random.nextFloat() * 15F);
			case 2 -> spike.setTilt(0F);
		}

		level().addFreshEntity(spike);
		level().playSound(null, getX(),getY(),getZ(), ModSounds.blizzard.get(), SoundSource.PLAYERS);
		spikes.add(spike);
	}

	@Override
	protected void onHit(HitResult result) {

	}
}