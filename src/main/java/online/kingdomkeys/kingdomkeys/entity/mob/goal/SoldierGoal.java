package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import java.util.List;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.phys.AABB;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

public class SoldierGoal extends TargetGoal {
	// 0-Normal, 1-spinning

		private final int MAX_SPINNING_TICKS = 2 * 20;
		private int spinTicks = 0;
				
		private int ticksToChooseAI = 0; //Ticks in base state after an attack happened
		
		public SoldierGoal(PathfinderMob creature) {
			super(creature, true);
			ticksToChooseAI = 20;
		}
		
		@Override
		public boolean canContinueToUse() {
			if (this.mob.getTarget() != null) {
				//Set AI to use
				if(ticksToChooseAI <= 0 && EntityHelper.getState(mob) == 0) { //No random since it has only one attack
					setSpinning(mob);
					ticksToChooseAI = 150;
				} else {
					if(EntityHelper.getState(mob) == 0) {
						ticksToChooseAI-=2;
					}
				}

				if(isSpinning()) {
					spinningAI();
				}
				
				return true;
			} else { //If no target
				EntityHelper.setState(this.mob, 0);
			}
			return false;
		}

		private void spinningAI() {			
			spinTicks+=2;
			
			double r = 1.5D;
            AABB aabb = new AABB(mob.position().x, mob.position().y, mob.position().z, mob.position().x + 1, mob.position().y + 1, mob.position().z + 1).inflate(r, 0, r);
    		List<LivingEntity> list = mob.level().getEntitiesOfClass(LivingEntity.class, aabb);
    		list.remove(mob);
    		
            for(LivingEntity enemy : list) {
				mob.doHurtTarget(enemy);
			}
			
			if(spinTicks >= MAX_SPINNING_TICKS) {
				EntityHelper.setState(this.mob, 0);
				spinTicks = 0;
			}
		}
	
		public void setSpinning(Mob mob) {
			spinTicks = 0;
			EntityHelper.setState(mob, 1);
		}
		
		@Override
		public void start() {
			EntityHelper.setState(this.mob, 0);
		}

		private boolean isSpinning() {
			return EntityHelper.getState(this.mob) == 1;
		}
				
		@Override
		public boolean canUse() {
			return this.mob.getTarget() != null;
		}

	}