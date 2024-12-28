package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.animal.TropicalFish;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseKHEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCAeroSoundPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class EmeraldBluesGoal extends TargetGoal {
	// 0-Normal, 1-Aero

		private final int MAX_AERO_TICKS = 15 * 20;
				
		private int ticksToChooseAI = 0; //Ticks in base state after an attack happened
		
		public EmeraldBluesGoal(PathfinderMob creature) {
			super(creature, true);
			ticksToChooseAI = 20;
			this.mob = (BaseKHEntity) creature;
		}
		private BaseKHEntity mob;

		@Override
		public boolean canContinueToUse() {
			if (this.mob.getTarget() != null) {
				GlobalData globalData = GlobalData.get(mob);
				if(mob.getState() == 1 && globalData.getAeroTicks() <= 0) {
					mob.setState(0);
					PacketHandler.syncToAllAround(mob, globalData);
				}
				
				//Set AI to use
				if(ticksToChooseAI <= 0 && mob.getState() == 0) { //No random since it has only one attack
					setAero(mob);
					ticksToChooseAI = 150;
				} else {
					if(mob.getState() == 0) {
						ticksToChooseAI-=2;
					}
				}

				if(isAero()) {
					aeroAI();
				}
				
				return true;
			} else { //If no target
				mob.setState(0);
			}
			return false;
		}

		private void aeroAI() {
			GlobalData globalData = GlobalData.get(mob);
   		
			switch(globalData.getAeroLevel()) {
			case 0:
				
				break;
			case 1:
	            for(LivingEntity enemy : Utils.getLivingEntitiesInRadius(mob, 1F)) {
					enemy.hurt(enemy.damageSources().mobAttack(mob), (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue() * 0.03F);
				}
				break;
			case 2:
	            for(LivingEntity enemy : Utils.getLivingEntitiesInRadius(mob, 1.2F)) {
					enemy.hurt(enemy.damageSources().mobAttack(mob), (float) mob.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue() * 0.04F);
				}
				break;
			}
		}
	
		public void setAero(BaseKHEntity mob) {
			GlobalData globalData = GlobalData.get(mob);
			globalData.setAeroTicks(MAX_AERO_TICKS, 1);
			PacketHandler.syncToAllAround(mob, globalData);
			mob.setState(1);
			PacketHandler.sendToAll(new SCAeroSoundPacket(this.mob.getId()));
		}
		
		@Override
		public void start() {
			mob.setState(0);
		}

		private boolean isAero() {
			return mob.getState() == 1;
		}
				
		@Override
		public boolean canUse() {
			return this.mob.getTarget() != null;
		}

	}