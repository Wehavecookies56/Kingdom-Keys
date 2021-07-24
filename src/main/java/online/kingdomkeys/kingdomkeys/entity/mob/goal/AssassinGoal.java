package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.level.Explosion;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

public class AssassinGoal extends TargetGoal {
	// 2 - is Exploding ; 1 - in Shadow ; 0 - in Overworld

	private final int MAX_DISTANCE_FOR_AI = 100, TIME_BEFORE_NEXT_ATTACK = 70, TIME_TO_GO_UNDERGROUND = 120, TIME_UNDERGROUND = 30;
	private int undergroundTicks = 70, ticksUntilNextAttack, ticksToLowHealth = 70, ticksToExplode = 30;
	private boolean canUseNextAttack = true;

	public AssassinGoal(PathfinderMob creature) {
		super(creature, true);
		ticksUntilNextAttack = TIME_BEFORE_NEXT_ATTACK;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.mob.getTarget() != null) {
			if(mob.getHealth() <= mob.getMaxHealth() / 4) { //If the assassin is at 25% hp or less
				if(isExploding()) {
					ticksToExplode--;
					if(ticksToExplode <= 0) {
						explode();
					}
				} else {
					ticksToLowHealth--;
					if(ticksToLowHealth <= 0) {
						EntityHelper.setState(this.mob, 2);
	                    this.mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
	                    this.mob.setInvulnerable(true);
					}
				}
				return true;
			}
			
			
			if (isUnderground()) {
				this.mob.setInvulnerable(true);

				canUseNextAttack = false;
				if(this.mob.distanceTo(this.mob.getTarget()) < 5) {
					this.mob.doHurtTarget(this.mob.getTarget());
				} else {
					EntityHelper.setState(this.mob, 0);
					this.mob.setInvulnerable(false);
					undergroundTicks = TIME_TO_GO_UNDERGROUND;
					canUseNextAttack = true;
				}
				
				undergroundTicks++;
				if (undergroundTicks >= TIME_UNDERGROUND) { //Go to the surface
					EntityHelper.setState(this.mob, 0);
					this.mob.setInvulnerable(false);

					canUseNextAttack = true;
				}
			}
			
			if(this.mob.distanceTo(this.mob.getTarget()) < 5) { //If target is in range
				if (this.mob.isOnGround()) {
					if (!isUnderground()) {
						undergroundTicks--;
						if (undergroundTicks <= 0) {
							EntityHelper.setState(this.mob, 1);
							canUseNextAttack = false;
						}
					} else {
	
					}
				}
	
				if (!canUseNextAttack) {
					ticksUntilNextAttack--;
					if (ticksUntilNextAttack <= 0) {
						canUseNextAttack = true;
						ticksUntilNextAttack = TIME_BEFORE_NEXT_ATTACK;
					}
				}
			}

			return true;
		}
		EntityHelper.setState(this.mob, 0);
		this.mob.setInvulnerable(false);
		return false;
	}

	private void explode() {
        mob.level.explode(mob, mob.getX(), mob.getY(), mob.getZ(), 6, false, Explosion.BlockInteraction.NONE);
        mob.remove(false);
	}

	@Override
	public void start() {
		EntityHelper.setState(this.mob, 0);
		this.mob.setInvulnerable(false);
	}

	private boolean isUnderground() {
		return EntityHelper.getState(this.mob) == 1;
	}
	
	private boolean isExploding() {
		return EntityHelper.getState(this.mob) == 2;
	}

	@Override
	public boolean canUse() {
		return this.mob.getTarget() != null && this.mob.distanceToSqr(this.mob.getTarget()) < MAX_DISTANCE_FOR_AI;
	}

}