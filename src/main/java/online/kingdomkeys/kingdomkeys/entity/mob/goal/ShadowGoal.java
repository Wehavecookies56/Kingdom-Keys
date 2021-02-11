package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.TargetGoal;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

public class ShadowGoal extends TargetGoal {
	// 1 - in Shadow ; 0 - in Overworld

	private final int MAX_DISTANCE_FOR_AI = 100, MAX_DISTANCE_FOR_LEAP = 10, MAX_DISTANCE_FOR_DASH = 25, MAX_DISTANCE_FOR_ATTACK = 5, TIME_BEFORE_NEXT_ATTACK = 70, TIME_OUTSIDE_THE_SHADOW = 70;
	private int shadowTicks = 70, oldAi = -1, ticksUntilNextAttack;
	private boolean canUseNextAttack = true;
	private double originalAttackDamage;

	public ShadowGoal(CreatureEntity creature) {
		super(creature, true);
		ticksUntilNextAttack = TIME_BEFORE_NEXT_ATTACK;   
		this.originalAttackDamage = this.goalOwner.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();
	}

	@Override
	public boolean shouldContinueExecuting() {
		if(this.goalOwner.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue() > 0)
			this.originalAttackDamage = this.goalOwner.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue();

		if (this.goalOwner.getAttackTarget() != null && this.goalOwner.getDistanceSq(this.goalOwner.getAttackTarget()) < MAX_DISTANCE_FOR_AI) {
			System.out.println(originalAttackDamage);
			if (this.goalOwner.isOnGround()) {
				if (!isInShadow()) {
					shadowTicks--;
					if (shadowTicks <= 0) {
						EntityHelper.setState(this.goalOwner, 1);
	                    this.goalOwner.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.0D);
						canUseNextAttack = false;
					}
				} else {
				//	this.goalOwner.setInvisible(false);
				}
			}

			if (isInShadow()) {
				this.goalOwner.setInvulnerable(true);
				//this.goalOwner.setInvisible(true);
				canUseNextAttack = false;
				shadowTicks++;
				if (shadowTicks >= TIME_OUTSIDE_THE_SHADOW) {
					EntityHelper.setState(this.goalOwner, 0);
					this.goalOwner.setInvulnerable(false);
					canUseNextAttack = true;
                    this.goalOwner.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(originalAttackDamage);
				}
			}

			EntityHelper.Dir dir = EntityHelper.get8Directions(this.goalOwner);
			int currentAi = this.goalOwner.world.rand.nextInt(2);

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
			if (this.goalOwner.isOnGround() && this.goalOwner.getDistanceSq(this.goalOwner.getAttackTarget()) <= MAX_DISTANCE_FOR_LEAP && currentAi == 0 && canUseNextAttack) {
				oldAi = 0;

				this.goalOwner.setMotion(this.goalOwner.getMotion().add(0, 0.5, 0));

				switch (dir) {
				case NORTH:
					this.goalOwner.setMotion(this.goalOwner.getMotion().add(0, 0, -0.7));
					break;
				case NORTH_WEST:
					this.goalOwner.setMotion(this.goalOwner.getMotion().add(-0.7, 0, -0.7));
					break;
				case SOUTH:
					this.goalOwner.setMotion(this.goalOwner.getMotion().add(0, 0, 0.7));
					break;
				case NORTH_EAST:
					this.goalOwner.setMotion(this.goalOwner.getMotion().add(0.7, 0, -0.7));
					break;
				case WEST:
					this.goalOwner.setMotion(this.goalOwner.getMotion().add(-0.7, 0, 0));
					break;
				case SOUTH_WEST:
					this.goalOwner.setMotion(this.goalOwner.getMotion().add(-0.7, 0, 0.7));
					break;
				case EAST:
					this.goalOwner.setMotion(this.goalOwner.getMotion().add(0.7, 0, 0));
					break;
				case SOUTH_EAST:
					this.goalOwner.setMotion(this.goalOwner.getMotion().add(0.7, 0, 0.7));
					break;
				}

				if (this.goalOwner.world.rand.nextInt(2) == 0) {
					EntityHelper.setState(this.goalOwner, 0);
					this.goalOwner.setInvulnerable(false);
                    this.goalOwner.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(originalAttackDamage);
				} else {
					EntityHelper.setState(this.goalOwner, 1);
					this.goalOwner.setInvulnerable(true);
                    this.goalOwner.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0D);
				}

				canUseNextAttack = false;
			}

			// Dash
			if (this.goalOwner.isOnGround() && this.goalOwner.getDistanceSq(this.goalOwner.getAttackTarget()) <= MAX_DISTANCE_FOR_DASH && currentAi == 1 && canUseNextAttack) {
				oldAi = 1;

				this.goalOwner.setMotion(this.goalOwner.getMotion().add(0, 0.2, 0));

				switch (dir) {
				case NORTH:
					this.goalOwner.setMotion(this.goalOwner.getMotion().add(0, 0, -1));
					break;
				case NORTH_WEST:
					this.goalOwner.setMotion(this.goalOwner.getMotion().add(-1, 0, -1));
					break;
				case SOUTH:
					this.goalOwner.setMotion(this.goalOwner.getMotion().add(0, 0, 1));
					break;
				case NORTH_EAST:
					this.goalOwner.setMotion(this.goalOwner.getMotion().add(1, 0, -1));
					break;
				case WEST:
					this.goalOwner.setMotion(this.goalOwner.getMotion().add(-1, 0, 0));
					break;
				case SOUTH_WEST:
					this.goalOwner.setMotion(this.goalOwner.getMotion().add(-1, 0, 1));
					break;
				case EAST:
					this.goalOwner.setMotion(this.goalOwner.getMotion().add(1, 0, 0));
					break;
				case SOUTH_EAST:
					this.goalOwner.setMotion(this.goalOwner.getMotion().add(1, 0, 1));
					break;
				}

				if (this.goalOwner.world.rand.nextInt(2) == 0) {
					EntityHelper.setState(this.goalOwner, 0);
					this.goalOwner.setInvulnerable(false);
                    this.goalOwner.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(originalAttackDamage);
				} else {
					EntityHelper.setState(this.goalOwner, 1);
					this.goalOwner.setInvulnerable(true);
                    this.goalOwner.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0D);
				}

				canUseNextAttack = false;
			}

			return true;
		}
		EntityHelper.setState(this.goalOwner, 0);
		this.goalOwner.setInvulnerable(false);
        this.goalOwner.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(originalAttackDamage);
		return false;
	}

	@Override
	public void startExecuting() {
		EntityHelper.setState(this.goalOwner, 0);
		this.goalOwner.setInvulnerable(false);
	}

	private boolean isInShadow() {
		return EntityHelper.getState(this.goalOwner) == 1;
	}

	@Override
	public boolean shouldExecute() {
		return this.goalOwner.getAttackTarget() != null && this.goalOwner.getDistanceSq(this.goalOwner.getAttackTarget()) < MAX_DISTANCE_FOR_AI;
	}

}