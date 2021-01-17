package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

public abstract class BaseElementalMusicalHeartlessEntity extends MonsterEntity implements IKHMob {

    public enum Element {
        FIRE, BLIZZARD, THUNDER, AERO, CURE
    }

    Element elementToUse;

    protected BaseElementalMusicalHeartlessEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public BaseElementalMusicalHeartlessEntity(EntityType<? extends MonsterEntity> type, FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(type, world);
    }
    
    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
    	return ModCapabilities.getWorld((World)worldIn).getHeartlessSpawnLevel() > 0;
    }

    protected abstract Goal goalToUse();

    @OnlyIn(Dist.CLIENT)
    public abstract ResourceLocation getTexture();

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, goalToUse());
        this.goalSelector.addGoal(1, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    protected abstract double getMaxHP();

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(getMaxHP());
        this.getAttributes().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0.0D);

    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(EntityHelper.STATE, 0);
    }

    public abstract Element getElementToUse();

    @Override
    public EntityHelper.MobType getMobType() {
        return EntityHelper.MobType.HEARTLESS_EMBLEM;
    }
}
