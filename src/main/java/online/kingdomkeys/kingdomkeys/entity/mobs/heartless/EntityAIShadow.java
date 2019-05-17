package online.kingdomkeys.kingdomkeys.entity.mobs.heartless;


import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITarget;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

public class EntityAIShadow extends EntityAITarget {

    // 1 - in Shadow ; 0 - in Overworld

    private final int MAX_DISTANCE_FOR_AI = 100, MAX_DISTANCE_FOR_LEAP = 10, MAX_DISTANCE_FOR_DASH = 25, MAX_DISTANCE_FOR_ATTACK = 5, TIME_BEFORE_NEXT_ATTACK = 70, TIME_OUTSIDE_THE_SHADOW = 70;
    private int outsideShadowMaxTicks = 70, oldAi = -1, ticksUntilNextAttack;
    private boolean canUseNextAttack = true;

    public EntityAIShadow(EntityCreature creature) {
        super(creature, true);
        ticksUntilNextAttack = TIME_BEFORE_NEXT_ATTACK;
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (this.taskOwner.getAttackTarget() != null && this.taskOwner.getDistanceSq(this.taskOwner.getAttackTarget()) < MAX_DISTANCE_FOR_AI) {

            if(EntityHelper.getState(taskOwner) == 1)
                //System.out.println("" + EntityHelper.getState(taskOwner));

                if(!this.taskOwner.onGround) {
                    EntityHelper.setState(this.taskOwner, 0);
                    this.taskOwner.setInvulnerable(false);
                }
                else {
                    if(!isInShadow()) {
                        outsideShadowMaxTicks--;
                        if(outsideShadowMaxTicks <= 0) {
                            EntityHelper.setState(this.taskOwner, 1);
                            outsideShadowMaxTicks = TIME_OUTSIDE_THE_SHADOW;
                            canUseNextAttack = false;
                        }
                    } else {
                        this.taskOwner.setInvisible(false);
                    }
                }

            if (isInShadow()){
                this.taskOwner.setInvulnerable(true);
                this.taskOwner.setInvisible(true);
                canUseNextAttack = false;
                outsideShadowMaxTicks++;
                if (outsideShadowMaxTicks >= TIME_OUTSIDE_THE_SHADOW) {
                    EntityHelper.setState(this.taskOwner, 0);
                    this.taskOwner.setInvulnerable(false);
                    this.taskOwner.setInvisible(false);
                    canUseNextAttack = true;
                }
            }

            EntityHelper.Dir dir = EntityHelper.get8Directions(this.taskOwner);
            int currentAi = this.taskOwner.world.rand.nextInt(2);

            if(!canUseNextAttack) {
                ticksUntilNextAttack--;
                if(ticksUntilNextAttack <= 0) {
                    canUseNextAttack = true;
                    ticksUntilNextAttack = TIME_BEFORE_NEXT_ATTACK;
                }
            }

            if(oldAi != -1 && canUseNextAttack) {
                if(currentAi == 0 && oldAi == 0) currentAi = 1;
                if(currentAi == 1 && oldAi == 1) currentAi = 0;
            }

            //Leaping
            if(this.taskOwner.onGround && this.taskOwner.getDistanceSq(this.taskOwner.getAttackTarget()) <= MAX_DISTANCE_FOR_LEAP && currentAi == 0 && canUseNextAttack) {
                oldAi = 0;

                this.taskOwner.motionY += 0.5;
                if(dir == EntityHelper.Dir.NORTH) taskOwner.motionZ -= 0.7;
                if(dir == EntityHelper.Dir.NORTH_WEST) {taskOwner.motionZ -= 0.7;taskOwner.motionX -= 0.7;}
                if(dir == EntityHelper.Dir.SOUTH) taskOwner.motionZ += 0.7;
                if(dir == EntityHelper.Dir.NORTH_EAST) {taskOwner.motionZ -= 0.7;taskOwner.motionX += 0.7;}
                if(dir == EntityHelper.Dir.WEST) taskOwner.motionX -= 0.7;
                if(dir == EntityHelper.Dir.SOUTH_WEST) {taskOwner.motionZ += 0.7;taskOwner.motionX -= 0.7;}
                if(dir == EntityHelper.Dir.EAST) taskOwner.motionX += 0.7;
                if(dir == EntityHelper.Dir.SOUTH_EAST) {taskOwner.motionZ += 0.7;taskOwner.motionX += 0.7;}

                if(this.taskOwner.world.rand.nextInt(2) == 0) {
                    EntityHelper.setState(this.taskOwner, 0);
                    this.taskOwner.setInvulnerable(false);
                }
                else {
                    EntityHelper.setState(this.taskOwner, 1);
                    this.taskOwner.setInvulnerable(true);
                }

                canUseNextAttack = false;
            }

            //System.out.println(this.taskOwner.onGround);

            //Dash
            if(this.taskOwner.onGround && this.taskOwner.getDistanceSq(this.taskOwner.getAttackTarget()) <= MAX_DISTANCE_FOR_DASH && currentAi == 1 && canUseNextAttack) {
                oldAi = 1;

                this.taskOwner.motionY += 0.2;
                if(dir == EntityHelper.Dir.NORTH) taskOwner.motionZ -= 1.0;
                if(dir == EntityHelper.Dir.NORTH_WEST) {taskOwner.motionZ -= 1;taskOwner.motionX -= 1;}
                if(dir == EntityHelper.Dir.SOUTH) taskOwner.motionZ += 1;
                if(dir == EntityHelper.Dir.NORTH_EAST) {taskOwner.motionZ -= 1;taskOwner.motionX += 1;}
                if(dir == EntityHelper.Dir.WEST) taskOwner.motionX -= 1;
                if(dir == EntityHelper.Dir.SOUTH_WEST) {taskOwner.motionZ += 1;taskOwner.motionX -= 1;}
                if(dir == EntityHelper.Dir.EAST) taskOwner.motionX += 1;
                if(dir == EntityHelper.Dir.SOUTH_EAST) {taskOwner.motionZ += 1;taskOwner.motionX += 1;}

                if(this.taskOwner.world.rand.nextInt(2) == 0) {
                    EntityHelper.setState(this.taskOwner, 0);
                    this.taskOwner.setInvulnerable(false);
                }
                else {
                    EntityHelper.setState(this.taskOwner, 1);
                    this.taskOwner.setInvulnerable(true);
                }

                canUseNextAttack = false;
            }

            return true;
        }
        EntityHelper.setState(this.taskOwner, 0);
        this.taskOwner.setInvulnerable(false);
        return false;
    }

    @Override
    public void startExecuting() {
        EntityHelper.setState(this.taskOwner, 0);
        this.taskOwner.setInvulnerable(false);
    }

    private boolean isInShadow() { return EntityHelper.getState(this.taskOwner) == 1; }

    @Override
    public boolean shouldExecute() {
        return this.taskOwner.getAttackTarget() != null && this.taskOwner.getDistanceSq(this.taskOwner.getAttackTarget()) < MAX_DISTANCE_FOR_AI;
    }

}
