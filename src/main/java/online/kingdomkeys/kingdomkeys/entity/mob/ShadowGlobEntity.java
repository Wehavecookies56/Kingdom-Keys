package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class ShadowGlobEntity extends CreatureEntity implements IKHMob {

    public ShadowGlobEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public ShadowGlobEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(ModEntities.TYPE_SHADOW_GLOB.get(), world);
    }
    
    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
    	return ModCapabilities.getWorld((World)worldIn).getHeartlessSpawn();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 0.0D, false));
        this.goalSelector.addGoal(1, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
        this.getAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000.0D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(0.0D);
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0.0D);
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 4;
    }

    @Override
    protected void registerData() {
    	super.registerData();
    	this.dataManager.register(EntityHelper.STATE, 0);
    }

    @Override
    public boolean isAIDisabled() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        this.renderYawOffset = 0;
    }

    @Override
    public EntityHelper.MobType getMobType() {
        return EntityHelper.MobType.HEARTLESS_PUREBLOOD;
    }
}
