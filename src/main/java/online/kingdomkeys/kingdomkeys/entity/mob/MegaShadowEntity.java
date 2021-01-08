package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.ShadowGoal;

public class MegaShadowEntity extends CreatureEntity implements IKHMob {

    public MegaShadowEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public MegaShadowEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(ModEntities.TYPE_MEGA_SHADOW.get(), world);
    }
    
    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
    	return ModCapabilities.getWorld((World)worldIn).getHeartlessSpawnLevel() > 0;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AgeableEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AnimalEntity.class, true));
        this.targetSelector.addGoal(4, new ShadowGoal(this));
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MobEntity.registerAttributes()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 35.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.28D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 80.0D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D)
                ;
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 4;
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(EntityHelper.STATE, 0);
        this.dataManager.register(EntityHelper.ANIMATION, 0);
    }

    @Override
    public EntityHelper.MobType getMobType() {
        return EntityHelper.MobType.HEARTLESS_PUREBLOOD;
    }

}
