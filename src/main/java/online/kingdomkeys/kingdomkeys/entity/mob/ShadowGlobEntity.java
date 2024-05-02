package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class ShadowGlobEntity extends BaseKHEntity {

    public ShadowGlobEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
        xpReward = 2;
    }

    public ShadowGlobEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(ModEntities.TYPE_SHADOW_GLOB.get(), world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 0.0D, false));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1000.0D)
                .add(Attributes.FOLLOW_RANGE, 0.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 1.0D)
                ;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 4;
    }

    @Override
    protected void defineSynchedData() {
    	super.defineSynchedData();
    	this.entityData.define(EntityHelper.STATE, 0);
    }

    @Override
    public boolean isNoAi() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        this.yBodyRot = 0;
    }

    @Override
    public EntityHelper.MobType getKHMobType() {
        return EntityHelper.MobType.HEARTLESS_PUREBLOOD;
    }
}
