package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.SoldierGoal;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.WhiteMushroomGoal;
import online.kingdomkeys.kingdomkeys.item.KKResistanceType;

public class WhiteMushroomEntity extends BaseKHEntity {

    public WhiteMushroomEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
        xpReward = 2;
    }

    public WhiteMushroomEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(ModEntities.TYPE_WHITE_MUSHROOM.get(), world);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(!level().isClientSide()){
            if(source.getMsgId().equals(KKResistanceType.fire.toString())){
                System.out.println("Fire");
                extinguishFire();
            }
            if(source.getMsgId().equals(KKResistanceType.ice.toString())) {
                System.out.println("Blizzard");
            }
            if(source.getMsgId().equals(KKResistanceType.lightning.toString())) {
                System.out.println("Thunder");
            }
        }

        //TODO angry animation and despawn
        return false;
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(EntityHelper.STATE, 0);
        this.entityData.define(EntityHelper.ANIMATION, 0);
    }

    @Override
    public EntityHelper.MobType getKHMobType() {
        return EntityHelper.MobType.HEARTLESS_EMBLEM;
    }

}
