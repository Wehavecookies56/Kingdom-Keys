package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class FiragaBurstControllerEntity extends BaseMagicProjectile {
	Magic fire = ModMagic.FIRE.get();

	public FiragaBurstControllerEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
	}

	public FiragaBurstControllerEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockedOnEntity) {
		super(ModEntities.TYPE_FIRAGABURST.get(), player, world);
		this.dmgMult = dmgMult;
		this.lockOnEntity = lockedOnEntity;
		this.damageType = KKDamageTypes.FIRE;
		maxTicks = 100;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			discard();
			return;
		}

		if (!level().isClientSide && getOwner() instanceof Player player) {
			if (tickCount == 10) {
				this.setDeltaMovement(Vec3.ZERO);
			}
			if (tickCount > 10 && tickCount % 3 == 0 && tickCount < maxTicks - 10) {
				float radius = 6F;
				if (getOwner() instanceof Player) {
					List<LivingEntity> targets = Utils.getLivingEntitiesInRadiusExcludingParty(player, this, radius, radius, radius);

					if (!targets.isEmpty()) {// Random enemies target
						LivingEntity target = targets.get(random.nextInt(targets.size()));
						FiragaEntity firaga = new FiragaEntity(level(), player, dmgMult, target);
						firaga.setMagic(getMagic());
						firaga.setPos(getX(), getY() + 1.5F, getZ());
						Vec3 direction = target.position().add(0, target.getBbHeight() * 0.5D, 0).subtract(firaga.position()).normalize();
						firaga.setDeltaMovement(direction.scale(1.5D));
						level().addFreshEntity(firaga);

					} else { //Random throws to the ground
						double angle = random.nextDouble() * Math.PI * 2.0;
						double distance = random.nextDouble() * radius;

						double targetX = getX() + Math.cos(angle) * distance;
						double targetZ = getZ() + Math.sin(angle) * distance;

						BlockPos groundPos = level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, BlockPos.containing(targetX, getY(), targetZ));
						Vec3 targetPos = new Vec3(targetX, groundPos.getY() + 0.5D, targetZ);
						FiragaEntity firaga = new FiragaEntity(level(), player, dmgMult * 0.4F, null);
						firaga.setMagic(getMagic());
						firaga.setPos(getX(), getY() + 1.5F, getZ());
						Vec3 direction = targetPos.subtract(firaga.position()).normalize();
						firaga.setDeltaMovement(direction.scale(1.5D));
						level().addFreshEntity(firaga);
					}

					fire.playMagicCastSound(player, player);
				}
			}
		}

		super.tick();
	}

	@Override
	protected void onHit(HitResult result) {

	}
}