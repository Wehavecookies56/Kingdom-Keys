package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Who a projectile hit and who is around it, decided the same way for all of them: never the one who fired it,
 * and only those Utils.canHarm allows when it comes to hurting.
 */
public final class ProjectileHits {

	private ProjectileHits() {}

	/** Whether this hit landed on the one who fired the projectile, which no projectile reacts to */
	public static boolean hitOwner(HitResult hit, @Nullable Entity owner) {
		return owner != null && hit instanceof EntityHitResult entityHit && entityHit.getEntity() == owner;
	}

	/** The living entity this hit landed on, ally or not, but never the shooter */
	@Nullable
	public static LivingEntity struck(HitResult hit, @Nullable Entity owner) {
		return hit instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity living && living != owner ? living : null;
	}

	/** The living entity this hit landed on, if the shooter may hurt it */
	@Nullable
	public static LivingEntity target(HitResult hit, @Nullable Entity owner) {
		LivingEntity struck = struck(hit, owner);
		return struck != null && Utils.canHarm(owner, struck) ? struck : null;
	}

	/** Everyone within radius of center (the projectile, the caster, a target...) the shooter may hurt, leaving out whoever it already hit directly */
	public static List<LivingEntity> around(Entity center, @Nullable Entity owner, double radius, @Nullable LivingEntity directHit) {
		return around(center, owner, radius, radius, radius, directHit);
	}

	public static List<LivingEntity> around(Entity center, @Nullable Entity owner, double radiusX, double radiusY, double radiusZ, @Nullable LivingEntity directHit) {
		return center.level().getEntitiesOfClass(LivingEntity.class, center.getBoundingBox().inflate(radiusX, radiusY, radiusZ), e -> e.isAlive() && e != owner && e != directHit && Utils.canHarm(owner, e));
	}

	/** Everyone inside this box the shooter may hurt */
	public static List<LivingEntity> in(Level level, AABB box, @Nullable Entity owner) {
		return level.getEntitiesOfClass(LivingEntity.class, box, e -> e.isAlive() && e != owner && Utils.canHarm(owner, e));
	}

	/** Every entity around center apart from the shooter and its allies, not only living ones, for what also drags items and the like along */
	public static List<Entity> entitiesAround(Entity center, @Nullable Entity owner, double radiusX, double radiusY, double radiusZ) {
		return center.level().getEntities(center, center.getBoundingBox().inflate(radiusX, radiusY, radiusZ), e -> e.isAlive() && e != owner && Utils.canHarm(owner, e));
	}

	/** Everyone within radius of the projectile, allies included, for what it does to friend and foe alike (like putting fires out) */
	public static List<LivingEntity> everyoneAround(Entity projectile, @Nullable Entity owner, double radius, @Nullable LivingEntity directHit) {
		return projectile.level().getEntitiesOfClass(LivingEntity.class, projectile.getBoundingBox().inflate(radius), e -> e.isAlive() && e != owner && e != directHit);
	}
}
