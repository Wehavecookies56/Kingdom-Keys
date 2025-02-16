package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.BaseKHEntity;

public class WhiteMushroomGoal extends TargetGoal {
	// 0-Normal, 1-Fire, 2-Blizzard, 3- Thunder
	private final int MAX_NO_CHARADE_DURATION = 6 * 20;
	private final int MAX_CHARADE_DURATION = 4 * 20;
	private int charadeDuration = 0;

	private int ticksToChooseCharade = 0; //Ticks in base state after an attack happened

	public WhiteMushroomGoal(PathfinderMob creature) {
		super(creature, true);
		ticksToChooseCharade = 0;
		this.mob = (BaseKHEntity) creature;
	}
	private final BaseKHEntity mob;


	@Override
	public boolean canContinueToUse() {
		if (this.mob.getTarget() != null) {
			//Set AI to use
			//-1, -2 and -3 for satisfied, angry and victory
			switch(getCharade(mob)){
				case 0:
					if(ticksToChooseCharade <= 0) {
						int randomCharade = mob.level().getRandom().nextInt(3) + 1;
						setCharade(mob, randomCharade);
						ticksToChooseCharade = MAX_NO_CHARADE_DURATION;
					} else {
						ticksToChooseCharade-=2;
					}
					break;
				case -1: //Satisfied
					if(charadeDuration <= 0) {
						charadeDuration = MAX_CHARADE_DURATION;
						setCharade(mob,0);
					} else {
						charadeDuration-=2; //Time to complain
					}
					break;
				case -2: //Failed
					if(charadeDuration <= 0) {
						charadeDuration = MAX_CHARADE_DURATION;
						mob.remove(Entity.RemovalReason.KILLED);
					} else {
						charadeDuration-=4; //Time to complain
					}
					break;
				case -3: //Victory
					if(charadeDuration <= 0) {
						charadeDuration = MAX_CHARADE_DURATION;
						mob.remove(Entity.RemovalReason.KILLED);
					} else {
						charadeDuration-=2; //Time to complain
					}
					break;
				default:
					if(charadeDuration <= 0) {
						setCharade(mob, 0);
						charadeDuration = MAX_CHARADE_DURATION;
					} else {
						charadeDuration-=2;
					}
					break;
			}
			return true;
		} else { //If no target
			mob.setState(0);
		}
		return false;
	}

	public void setCharade(BaseKHEntity mob, int charadeID) {
		mob.setState(charadeID);
	}

	public int getCharade(BaseKHEntity mob){
		return mob.getState();
	}

	@Override
	public void start() {
		mob.setState(0);
	}

	@Override
	public boolean canUse() {
		return this.mob.getTarget() != null;
	}

}