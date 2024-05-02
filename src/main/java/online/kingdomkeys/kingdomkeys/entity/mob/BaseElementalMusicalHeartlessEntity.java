package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;

public abstract class BaseElementalMusicalHeartlessEntity extends BaseKHEntity {

    public enum Element {
        FIRE, BLIZZARD, THUNDER, AERO, CURE
    }

    Element elementToUse;

    protected BaseElementalMusicalHeartlessEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
        xpReward = 8;
    }

    public BaseElementalMusicalHeartlessEntity(EntityType<? extends Monster> type, PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(type, world);
    }

    protected abstract Goal goalToUse();

    @OnlyIn(Dist.CLIENT)
    public abstract ResourceLocation getTexture();

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, goalToUse());
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5D, true));
        this.goalSelector.addGoal(1, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
            .add(Attributes.FOLLOW_RANGE, 35.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.2D)
            .add(Attributes.ATTACK_DAMAGE, 0.0D)
			.add(Attributes.ATTACK_KNOCKBACK, 1.0D)
            ;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(EntityHelper.STATE, 0);
    }

    public abstract Element getElementToUse();

    @Override
    public EntityHelper.MobType getKHMobType() {
        return EntityHelper.MobType.HEARTLESS_EMBLEM;
    }
}
