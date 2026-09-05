package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class LightTrainingOrbEntity extends TrainingOrbEntity {
	public LightTrainingOrbEntity(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		this.xpReward = 3;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createLivingAttributes()
				.add(Attributes.MAX_HEALTH, 12.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3D)
				.add(Attributes.FOLLOW_RANGE, 24.0D)
				.add(Attributes.ATTACK_DAMAGE, 1.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 0.2D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.0D);
	}

	@Override
	protected ParticleOptions trail() {
		return ParticleTypes.END_ROD;
	}

	@Override
	protected int cdTicks() {
		return 30;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 4;
	}
}
