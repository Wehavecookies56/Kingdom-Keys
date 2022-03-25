package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

public class ShadowGoal extends TargetGoal {
	// 1 - in Shadow ; 0 - in Overworld

	private final int MAX_DISTANCE_FOR_AI = 100, MAX_DISTANCE_FOR_LEAP = 10, MAX_DISTANCE_FOR_DASH = 25, TIME_BEFORE_NEXT_ATTACK = 70, TIME_OUTSIDE_THE_SHADOW = 70;
	private int shadowTicks = 70, oldAi = -1, ticksUntilNextAttack;
	private boolean canUseNextAttack = true;
	private double originalAttackDamage;

	public ShadowGoal(PathfinderMob creature) {
		super(creature, true);
		ticksUntilNextAttack = TIME_BEFORE_NEXT_ATTACK;   
		this.originalAttackDamage = this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
	}

	@Override
	public boolean canContinueToUse() {
		if(this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue() > 0)
			this.originalAttackDamage = this.mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();

		if (this.mob.getTarget() != null && this.mob.distanceToSqr(this.mob.getTarget()) < MAX_DISTANCE_FOR_AI) {
		
			if (this.mob.isOnGround()) {
				if (!isInShadow()) {
					shadowTicks--;
					if (shadowTicks <= 0) {
						EntityHelper.setState(this.mob, 1);
	                    this.mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.0D);
						canUseNextAttack = false;
					}
				} else {
				//	this.goalOwner.setInvisible(false);
				}
			}

			if (isInShadow()) {
				this.mob.setInvulnerable(true);
				//this.goalOwner.setInvisible(true);
				canUseNextAttack = false;
				shadowTicks++;
				if (shadowTicks >= TIME_OUTSIDE_THE_SHADOW) {
					EntityHelper.setState(this.mob, 0);
					this.mob.setInvulnerable(false);
					canUseNextAttack = true;
                    this.mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(originalAttackDamage);
				}
			}

			EntityHelper.Dir dir = EntityHelper.get8Directions(this.mob);
			int currentAi = this.mob.level.random.nextInt(2);

			if (!canUseNextAttack) {
				ticksUntilNextAttack--;
				if (ticksUntilNextAttack <= 0) {
					canUseNextAttack = true;
					ticksUntilNextAttack = TIME_BEFORE_NEXT_ATTACK;
				}
			}

			if (oldAi != -1 && canUseNextAttack) {
				if (currentAi == 0 && oldAi == 0)
					currentAi = 1;
				if (currentAi == 1 && oldAi == 1)
					currentAi = 0;
			}

			// Leaping
			if (this.mob.isOnGround() && this.mob.distanceToSqr(this.mob.getTarget()) <= MAX_DISTANCE_FOR_LEAP && currentAi == 0 && canUseNextAttack) {
				oldAi = 0;

				this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0, 0.5, 0));

				switch (dir) {
				case NORTH:
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0, 0, -0.7));
					break;
				case NORTH_WEST:
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(-0.7, 0, -0.7));
					break;
				case SOUTH:
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0, 0, 0.7));
					break;
				case NORTH_EAST:
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.7, 0, -0.7));
					break;
				case WEST:
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(-0.7, 0, 0));
					break;
				case SOUTH_WEST:
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(-0.7, 0, 0.7));
					break;
				case EAST:
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.7, 0, 0));
					break;
				case SOUTH_EAST:
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.7, 0, 0.7));
					break;
				}

				if (this.mob.level.random.nextInt(2) == 0) {
					EntityHelper.setState(this.mob, 0);
					this.mob.setInvulnerable(false);
                    this.mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(originalAttackDamage);
				} else {
					EntityHelper.setState(this.mob, 1);
					this.mob.setInvulnerable(true);
                    this.mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0D);
				}

				canUseNextAttack = false;
			}

			// Dash
			if (this.mob.isOnGround() && this.mob.distanceToSqr(this.mob.getTarget()) <= MAX_DISTANCE_FOR_DASH && currentAi == 1 && canUseNextAttack) {
				oldAi = 1;

				this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0, 0.2, 0));

				switch (dir) {
				case NORTH:
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0, 0, -1));
					break;
				case NORTH_WEST:
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(-1, 0, -1));
					break;
				case SOUTH:
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0, 0, 1));
					break;
				case NORTH_EAST:
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(1, 0, -1));
					break;
				case WEST:
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(-1, 0, 0));
					break;
				case SOUTH_WEST:
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(-1, 0, 1));
					break;
				case EAST:
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(1, 0, 0));
					break;
				case SOUTH_EAST:
					this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(1, 0, 1));
					break;
				}

				if (this.mob.level.random.nextInt(2) == 0) {
					EntityHelper.setState(this.mob, 0);
					this.mob.setInvulnerable(false);
                    this.mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(originalAttackDamage);
				} else {
					EntityHelper.setState(this.mob, 1);
					this.mob.setInvulnerable(true);
                    this.mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0D);
				}

				canUseNextAttack = false;
			}

			return true;
		}
		EntityHelper.setState(this.mob, 0);
		this.mob.setInvulnerable(false);
        this.mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(originalAttackDamage);
		return false;
	}

	@Override
	public void start() {
		EntityHelper.setState(this.mob, 0);
		this.mob.setInvulnerable(false);
	}

	private boolean isInShadow() {
		return EntityHelper.getState(this.mob) == 1;
	}

	@Override
	public boolean canUse() {
		return this.mob.getTarget() != null && this.mob.distanceToSqr(this.mob.getTarget()) < MAX_DISTANCE_FOR_AI;
	}

}