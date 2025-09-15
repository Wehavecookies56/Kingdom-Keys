package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

public class WhiteMushroomGoal extends Goal {
	private final Mob mob;

	// 0-Normal, 1-Fire, 2-Blizzard, 3- Thunder
	private final int MAX_NO_CHARADE_DURATION = 6 * 20;
	private final int MAX_CHARADE_DURATION = 4 * 20;
	private int charadeDuration = 0;

	private int ticksToChooseCharade = 0; //Ticks in base state after an attack happened

	public WhiteMushroomGoal(Mob creature) {
		this.mob = creature;
		ticksToChooseCharade = 0;
		EntityHelper.setState(this.mob, 0);
	}

	@Override
	public boolean canContinueToUse() {
		return true;
	}

	@Override
	public void start() {
		EntityHelper.setState(this.mob, 0);
	}

	@Override
	public boolean canUse() {
		return true;
	}

	@Override
	public void tick() {
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
				if(charadeDuration <= -80) { // -80 cause we need extra time in case it's thunder
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
		super.tick();
	}

	public void setCharade(Mob mob, int charadeID) {
		EntityHelper.setState(mob, charadeID);
	}

	public int getCharade(Mob mob){
		return EntityHelper.getState(mob);
	}
}