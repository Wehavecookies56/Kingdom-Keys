package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.world.Explosion;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

public class AssassinGoal extends TargetGoal {
	// 2 - is Exploding ; 1 - in Shadow ; 0 - in Overworld

	private final int MAX_DISTANCE_FOR_AI = 100, TIME_BEFORE_NEXT_ATTACK = 70, TIME_OUTSIDE_THE_SHADOW = 70;
	private int undergroundTicks = 70, ticksUntilNextAttack, ticksToLowHealth = 70, ticksToExplode = 30;
	private boolean canUseNextAttack = true;

	public AssassinGoal(CreatureEntity creature) {
		super(creature, true);
		ticksUntilNextAttack = TIME_BEFORE_NEXT_ATTACK;
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (this.goalOwner.getAttackTarget() != null) {
			
			if(goalOwner.getHealth() <= goalOwner.getMaxHealth() / 4) { //If the assassin is at 25% hp or less
				if(isExploding()) {
					ticksToExplode--;
					//System.out.println("exploding "+ticksToExplode);
					if(ticksToExplode <= 0) {
						explode();
					}
				} else {
					ticksToLowHealth--;
					//System.out.println("low health "+ticksToLowHealth);
					if(ticksToLowHealth <= 0) {
						EntityHelper.setState(this.goalOwner, 2);
	                    this.goalOwner.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
	                    this.goalOwner.setInvulnerable(true);
					}
				}
				return true;
			}
			if(this.goalOwner.getDistance(this.goalOwner.getAttackTarget()) < 6) {
				if (this.goalOwner.onGround) {
					if (!isUnderground()) {
						undergroundTicks--;
						if (undergroundTicks <= 0) {
							EntityHelper.setState(this.goalOwner, 1);
							//shadowTicks = TIME_OUTSIDE_THE_SHADOW;
							canUseNextAttack = false;
						}
					} else {
	
					}
				}
	
				if (isUnderground()) {
					this.goalOwner.setInvulnerable(true);
	
					canUseNextAttack = false;
					if(this.goalOwner.getDistance(this.goalOwner.getAttackTarget()) < 6) {
						this.goalOwner.attackEntityAsMob(this.goalOwner.getAttackTarget());
					} else {
						EntityHelper.setState(this.goalOwner, 0);
						this.goalOwner.setInvulnerable(false);
						undergroundTicks = TIME_OUTSIDE_THE_SHADOW;
						canUseNextAttack = true;
					}
					undergroundTicks++;
					if (undergroundTicks >= TIME_OUTSIDE_THE_SHADOW) {
						EntityHelper.setState(this.goalOwner, 0);
						this.goalOwner.setInvulnerable(false);
	
						canUseNextAttack = true;
					}
				}
	
				//EntityHelper.Dir dir = EntityHelper.get8Directions(this.goalOwner);
				//int currentAi = this.goalOwner.world.rand.nextInt(2);
	
				if (!canUseNextAttack) {
					ticksUntilNextAttack--;
					if (ticksUntilNextAttack <= 0) {
						canUseNextAttack = true;
						ticksUntilNextAttack = TIME_BEFORE_NEXT_ATTACK;
					}
				}
	
				
				/*if (oldAi != -1 && canUseNextAttack) {
					if (currentAi == 0 && oldAi == 0)
						currentAi = 1;
					if (currentAi == 1 && oldAi == 1)
						currentAi = 0;
				}
	
				switch(currentAi) {
				case 0:
					
					break;
				}*/
			}

			return true;
		}
		EntityHelper.setState(this.goalOwner, 0);
		this.goalOwner.setInvulnerable(false);
		return false;
	}

	private void explode() {
        goalOwner.world.createExplosion(goalOwner, goalOwner.getPosX(), goalOwner.getPosY(), goalOwner.getPosZ(), 3, false, Explosion.Mode.NONE);
        goalOwner.remove();
	}

	@Override
	public void startExecuting() {
		EntityHelper.setState(this.goalOwner, 0);
		this.goalOwner.setInvulnerable(false);
	}

	private boolean isUnderground() {
		return EntityHelper.getState(this.goalOwner) == 1;
	}
	
	private boolean isExploding() {
		return EntityHelper.getState(this.goalOwner) == 2;
	}

	@Override
	public boolean shouldExecute() {
		return this.goalOwner.getAttackTarget() != null && this.goalOwner.getDistanceSq(this.goalOwner.getAttackTarget()) < MAX_DISTANCE_FOR_AI;
	}

}