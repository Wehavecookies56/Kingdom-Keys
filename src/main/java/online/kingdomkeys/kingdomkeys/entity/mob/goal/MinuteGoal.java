package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.world.Explosion;
import net.minecraftforge.event.ForgeEventFactory;
import online.kingdomkeys.kingdomkeys.entity.mob.*;

public class MinuteGoal extends TargetGoal {

    private float strength;
    private BaseBombEntity bomb;

    public MinuteGoal(CreatureEntity creature) {
        super(creature, true);
        if (creature instanceof MinuteBombEntity) {
            strength = 2F;
        } else if (creature instanceof SkaterBombEntity) {
            strength = 3F;
        } else if (creature instanceof StormBombEntity) {
            strength = 4F;
        } else if (creature instanceof DetonatorEntity) {
            strength = 5F;
        }

        if(creature instanceof BaseBombEntity){
            this.bomb = (BaseBombEntity) creature;
        }
    }

    @Override
    public boolean shouldExecute() {
        return this.goalOwner.getAttackTarget() != null && this.goalOwner.getDistanceSq(this.goalOwner.getAttackTarget()) < 1024;
    }

    public boolean shouldContinueExecuting() {
        if (this.goalOwner.getAttackTarget() != null) {
            LivingEntity target = this.goalOwner.getAttackTarget();

            if (goalOwner.getDistance(target) < 10) {
                if (bomb.ticksToExplode > 0) {
                    bomb.ticksToExplode--;
                    return true;
                } else {
                    Explosion.Mode explosion$mode = ForgeEventFactory.getMobGriefingEvent(goalOwner.world, goalOwner) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
                    goalOwner.world.createExplosion(goalOwner, goalOwner.getPosX(), goalOwner.getPosY(), goalOwner.getPosZ(), strength, false, explosion$mode);
                    goalOwner.remove();
                    return false;
                }
            }
        }
        return false;
    }


}
