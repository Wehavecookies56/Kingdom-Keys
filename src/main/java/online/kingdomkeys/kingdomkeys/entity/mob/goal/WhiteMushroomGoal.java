package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.phys.AABB;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

import java.util.List;

public class WhiteMushroomGoal extends TargetGoal {
	// 0-Normal, 1-Fire, 2-Blizzard, 3- Thunder
	private final int MAX_NO_CHARADE_DURATION = 6 * 20;
	private final int MAX_CHARADE_DURATION = 4 * 20;
	private int charadeDuration = 0;

	private int ticksToChooseCharade = 0; //Ticks in base state after an attack happened

	public WhiteMushroomGoal(PathfinderMob creature) {
		super(creature, true);
		ticksToChooseCharade = 20;
	}

	@Override
	public boolean canContinueToUse() {
		if (this.mob.getTarget() != null) {
			//Set AI to use
			//-1 and -2 for victory and angry ?
			if(getCharade(mob) == 0){ //If is not posing wait and pose a random charade from 1-3
				if(ticksToChooseCharade <= 0) {
					int randomCharade = mob.level().getRandom().nextInt(3) + 1;
					setCharade(mob, randomCharade);
					ticksToChooseCharade = MAX_NO_CHARADE_DURATION;
				} else {
					ticksToChooseCharade-=2;
				}
			} else { //If is posing and charade time reaches 0 return to normal
				if(charadeDuration <= 0) {
					setCharade(mob, 0);
					charadeDuration = MAX_CHARADE_DURATION;
				} else {
					charadeDuration-=2;
				}
			}
			return true;
		} else { //If no target
			EntityHelper.setState(this.mob, 0);
		}
		return false;
	}

	public void setCharade(Mob mob, int charadeID) {
		EntityHelper.setState(mob, charadeID);
	}

	public int getCharade(Mob mob){
		return EntityHelper.getState(mob);
	}

	@Override
	public void start() {
		EntityHelper.setState(this.mob, 0);
	}

	@Override
	public boolean canUse() {
		return this.mob.getTarget() != null;
	}

}