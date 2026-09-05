package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public class DarkTrainingOrbEntity extends TrainingOrbEntity {

	public DarkTrainingOrbEntity(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		this.xpReward = 6;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createLivingAttributes()
				.add(Attributes.MAX_HEALTH, 24.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.26D)
				.add(Attributes.FOLLOW_RANGE, 32.0D)
				.add(Attributes.ATTACK_DAMAGE, 3.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 0.6D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.3D);
	}

	@Override
	protected ParticleOptions trail() {
		return random.nextInt(3) == 0 ? ParticleTypes.END_ROD : ParticleTypes.SMOKE;
	}

	@Override
	protected int cdTicks() {
		return 50;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.SOUL_ESCAPE.value();
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 3;
	}
}
