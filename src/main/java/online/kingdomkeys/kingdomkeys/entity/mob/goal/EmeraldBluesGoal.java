package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class EmeraldBluesGoal extends TargetGoal {
	// 0-Normal, 1-Aero

		private final int MAX_AERO_TICKS = 15 * 20;
				
		private int ticksToChooseAI = 0; //Ticks in base state after an attack happened
		
		public EmeraldBluesGoal(PathfinderMob creature) {
			super(creature, true);
			ticksToChooseAI = 20;
		}
		
		@Override
		public boolean canContinueToUse() {
			if (this.mob.getTarget() != null) {
				IGlobalCapabilities globalData = ModCapabilities.getGlobal(mob);
				if(EntityHelper.getState(mob) == 1 && globalData.getAeroTicks() <= 0) {
					EntityHelper.setState(mob, 0);
					PacketHandler.syncToAllAround(mob, globalData);
				}
				
				//Set AI to use
				if(ticksToChooseAI <= 0 && EntityHelper.getState(mob) == 0) { //No random since it has only one attack
					setAero(mob);
					ticksToChooseAI = 150;
				} else {
					if(EntityHelper.getState(mob) == 0) {
						ticksToChooseAI-=2;
					}
				}

				if(isAero()) {
					aeroAI();
				}
				
				return true;
			} else { //If no target
				EntityHelper.setState(this.mob, 0);
			}
			return false;
		}

		private void aeroAI() {
			IGlobalCapabilities globalData = ModCapabilities.getGlobal(mob);
   		
			switch(globalData.getAeroLevel()) {
			case 0:
				
				break;
			case 1:
	            for(LivingEntity enemy : Utils.getLivingEntitiesInRadius(mob, 1F)) {
					enemy.hurt(DamageSource.mobAttack(mob), (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue() * 0.03F);
				}
				break;
			case 2:
	            for(LivingEntity enemy : Utils.getLivingEntitiesInRadius(mob, 1.2F)) {
					enemy.hurt(DamageSource.mobAttack(mob), (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue() * 0.04F);
				}
				break;
			}
		}
	
		public void setAero(Mob mob) {
			IGlobalCapabilities globalData = ModCapabilities.getGlobal(mob);
			globalData.setAeroTicks(MAX_AERO_TICKS, 1);
			PacketHandler.syncToAllAround(mob, globalData);
			EntityHelper.setState(mob, 1);
		}
		
		@Override
		public void start() {
			EntityHelper.setState(this.mob, 0);
		}

		private boolean isAero() {
			return EntityHelper.getState(this.mob) == 1;
		}
				
		@Override
		public boolean canUse() {
			return this.mob.getTarget() != null;
		}

	}