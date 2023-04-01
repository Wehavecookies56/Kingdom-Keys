package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

public class DragoonGoal extends TargetGoal {
	// 0-Normal, 1-spinning

		private final int MAX_BALL_TICKS = 3 * 20;
		private int ballTicks = 0;
				
		private final int MAX_FALL_TICKS = 1 * 20;
		private int fallTicks = 0;
		
		private int ticksToChooseAI = 0; //Ticks in base state after an attack happened
		
		public DragoonGoal(PathfinderMob creature) {
			super(creature, true);
			ticksToChooseAI = 20;
		}
		
		@Override
		public boolean canContinueToUse() {
			if (this.mob.getTarget() != null) {
				//Set AI to use
				if(ticksToChooseAI <= 0 && EntityHelper.getState(mob) == 0) { //No random since it has only one attack
                    if(mob.level.random.nextInt(100) + mob.level.random.nextDouble() <= 40) { // again but for another randomized number to see which morph to run, there's a 50/50 chance for both
                    	setBall(mob);
						ticksToChooseAI = 120;
                    } else {
                    	doJump(mob);
                    	ticksToChooseAI = 80;
                    }
				} else {
					if(EntityHelper.getState(mob) == 0) {
						ticksToChooseAI-=2;
					}
				}

				if(isBall()) {
					ballAI();
				}
				
				if(isFalling()) {
					fallingAI();
				}
				
				return true;
			} else { //If no target
				setDefault(mob);
			}
			return false;
		}

		private void doJump(Mob mob) {
			this.mob.setDeltaMovement(mob.getDeltaMovement().add(0, 0.8, 0));
			EntityHelper.Dir dir = EntityHelper.get8Directions(this.mob);

			float jumpSpeed = 1F;
			switch (dir) {
			case NORTH:
				mob.setDeltaMovement(mob.getDeltaMovement().add(0, 0, -jumpSpeed));
				break;
			case NORTH_WEST:
				mob.setDeltaMovement(mob.getDeltaMovement().add(-jumpSpeed, 0, -jumpSpeed));
				break;
			case SOUTH:
				mob.setDeltaMovement(mob.getDeltaMovement().add(0, 0, jumpSpeed));
				break;
			case NORTH_EAST:
				mob.setDeltaMovement(mob.getDeltaMovement().add(jumpSpeed, 0, -jumpSpeed));
				break;
			case WEST:
				mob.setDeltaMovement(mob.getDeltaMovement().add(-jumpSpeed, 0, 0));
				break;
			case SOUTH_WEST:
				mob.setDeltaMovement(mob.getDeltaMovement().add(-jumpSpeed, 0, jumpSpeed));
				break;
			case EAST:
				mob.setDeltaMovement(mob.getDeltaMovement().add(jumpSpeed, 0, 0));
				break;
			case SOUTH_EAST:
				mob.setDeltaMovement(mob.getDeltaMovement().add(jumpSpeed, 0, jumpSpeed));
				break;
			}			
		}

		private void ballAI() {			
			ballTicks+=2;

			if(ballTicks >= MAX_BALL_TICKS) {
				setFall(mob);
				ballTicks = 0;
			}
		}
		
		private void fallingAI() {			
			fallTicks+=2;
            for(LivingEntity enemy : EntityHelper.getEntitiesNear(this.mob, 3))
            	mob.doHurtTarget(enemy);

			if(fallTicks >= MAX_FALL_TICKS) {
				fallTicks = 0;
				setDefault(mob);
			}
		}
		
		public void setFall(Mob mob) {
			fallTicks = 0;
			EntityHelper.setState(mob, 2);
			mob.level.playSound(null, mob.blockPosition(), ModSounds.portal.get(), SoundSource.HOSTILE, 1F, 2F);
			mob.level.playSound(null, mob.getTarget().blockPosition(), ModSounds.portal.get(), SoundSource.HOSTILE, 1F, 2F);

			mob.teleportTo(mob.getTarget().getX(), mob.getTarget().getY() + 5, mob.getTarget().getZ());
            mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
		}
	
		public void setBall(Mob mob) {
			ballTicks = 0;
			EntityHelper.setState(mob, 1);
            mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
		}
		
		public void setDefault(Mob mob) {
			EntityHelper.setState(mob, 0);
            mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.27D);
		}
		
		@Override
		public void start() {
			EntityHelper.setState(this.mob, 0);
		}

		private boolean isBall() {
			return EntityHelper.getState(this.mob) == 1;
		}
		
		private boolean isFalling() {
			return EntityHelper.getState(this.mob) == 2;
		}
				
		@Override
		public boolean canUse() {
			return this.mob.getTarget() != null;
		}

	}