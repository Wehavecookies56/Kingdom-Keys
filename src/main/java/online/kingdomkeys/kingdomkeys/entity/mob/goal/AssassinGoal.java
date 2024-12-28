package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseKHEntity;

public class AssassinGoal extends TargetGoal {
	// 2 - is Exploding ; 1 - in Shadow ; 0 - in Overworld

	private final int MAX_DISTANCE_FOR_AI = 100, TIME_BEFORE_NEXT_ATTACK = 70, TIME_TO_GO_UNDERGROUND = 120, TIME_UNDERGROUND = 30;
	private int undergroundTicks = 70, ticksUntilNextAttack, ticksToLowHealth = 70, ticksToExplode = 30;
	private boolean canUseNextAttack = true;

	private BaseKHEntity mob;
	public AssassinGoal(PathfinderMob creature) {
		super(creature, true);
		ticksUntilNextAttack = TIME_BEFORE_NEXT_ATTACK;
		this.mob = (BaseKHEntity) creature;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.mob.getTarget() != null) {
			if(mob.getHealth() <= mob.getMaxHealth() / 4) { //If the assassin is at 25% hp or less
				if(isExploding()) {
					ticksToExplode-=2;
					if(ticksToExplode <= 0) {
						explode();
					}
				} else {
					ticksToLowHealth-=2;
					if(ticksToLowHealth <= 0) {
						mob.setState(2);
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
					mob.setState(0);
					this.mob.setInvulnerable(false);
					undergroundTicks = TIME_TO_GO_UNDERGROUND;
					canUseNextAttack = true;
				}
				
				undergroundTicks+=2;
				if (undergroundTicks >= TIME_UNDERGROUND) { //Go to the surface
					mob.setState(0);
					this.mob.setInvulnerable(false);

					canUseNextAttack = true;
				}
			}
			
			if(this.mob.distanceTo(this.mob.getTarget()) < 5) { //If target is in range
				if (this.mob.onGround()) {
					if (!isUnderground()) {
						undergroundTicks-=2;
						if (undergroundTicks <= 0) {
							mob.setState(1);
							canUseNextAttack = false;
						}
					} else {
	
					}
				}
	
				if (!canUseNextAttack) {
					ticksUntilNextAttack-=2;
					if (ticksUntilNextAttack <= 0) {
						canUseNextAttack = true;
						ticksUntilNextAttack = TIME_BEFORE_NEXT_ATTACK;
					}
				}
			}

			return true;
		}
		mob.setState(0);
		this.mob.setInvulnerable(false);
		return false;
	}

	private void explode() {
        mob.level().explode(mob, mob.getX(), mob.getY(), mob.getZ(), 6, false, Level.ExplosionInteraction.NONE);
        mob.remove(Entity.RemovalReason.KILLED);
	}

	@Override
	public void start() {
		mob.setState(0);
		this.mob.setInvulnerable(false);
	}

	private boolean isUnderground() {
		return mob.getState() == 1;
	}
	
	private boolean isExploding() {
		return mob.getState() == 2;
	}

	@Override
	public boolean canUse() {
		return this.mob.getTarget() != null && this.mob.distanceToSqr(this.mob.getTarget()) < MAX_DISTANCE_FOR_AI;
	}

}