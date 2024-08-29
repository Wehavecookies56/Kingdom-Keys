package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.WhiteMushroomGoal;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;
import online.kingdomkeys.kingdomkeys.item.ModItems;

public class WhiteMushroomEntity extends BaseKHEntity {

    public WhiteMushroomEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
        xpReward = 2;
    }

    int satisfied = 0;

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(!level().isClientSide()){
            if(source.getMsgId().equals(KKResistanceType.fire.toString())){
                extinguishFire();
                checkSatisfy(1);
            } else if(source.getMsgId().equals(KKResistanceType.ice.toString())) {
                checkSatisfy(2);
            } else if(source.getMsgId().equals(KKResistanceType.lightning.toString())) {
                checkSatisfy(3);
            } else {
                EntityHelper.setState(this, -2);
            }
        }

        //TODO angry animation and despawn
        return false;
    }

    private void checkSatisfy(int i) {
        if(EntityHelper.getState(this) >= 0) { //Prevents multiple hitting attacks from counting at the wrong moment
            if (EntityHelper.getState(this) == i) { //If the magic is right
                //System.out.println("Happy");
                EntityHelper.setState(this, -1); //Set to satisfied pose
                satisfied++;
                if (satisfied >= 3) { //If it's the 3rd time in a row
                    //System.out.println("Drop smth");
                    EntityHelper.setState(this, -3); //Set to victory pose

                    ItemEntity ie = new ItemEntity(level(), getX(), getY(), getZ(), new ItemStack(ModItems.orichalcum.get()));
                    level().addFreshEntity(ie);
                }
            } else { //If magic is wrong set to angry pose
                //System.out.println("Not happy");
                EntityHelper.setState(this, -2);

            }
        }
    }

    @Override
    protected void registerGoals() {
       // this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
       // this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
       // this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		//this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
       // this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AnimalEntity.class, true));
        this.goalSelector.addGoal(4, new WhiteMushroomGoal(this));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
            .add(Attributes.FOLLOW_RANGE, 15.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.2D)
            .add(Attributes.MAX_HEALTH, 50.0D)
            .add(Attributes.ATTACK_DAMAGE, 0)
			.add(Attributes.ATTACK_KNOCKBACK, 0);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 4;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
        pBuilder.define(EntityHelper.STATE, 0);
        pBuilder.define(EntityHelper.ANIMATION, 0);
    }

    @Override
    public EntityHelper.MobType getKHMobType() {
        return EntityHelper.MobType.HEARTLESS_EMBLEM;
    }

}
