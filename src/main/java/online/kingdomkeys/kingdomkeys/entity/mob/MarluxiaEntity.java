package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.MarluxiaGoal;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import org.jetbrains.annotations.Nullable;

public class MarluxiaEntity extends BaseKHEntity {

	// How long he stands untouchable at the start, so the entrance can play out
	private static final int INTRO_TICKS = 100;

	// The armor's damage reduction and its fire weakness live in EntityEvents, alongside the rest of the mod's damage pipeline
	private static final EntityDataAccessor<Boolean> ARMOURED = SynchedEntityData.defineId(MarluxiaEntity.class, EntityDataSerializers.BOOLEAN);

	public MarluxiaGoal marluxiaGoal;

	public MarluxiaEntity(EntityType<? extends Monster> type, Level worldIn) {
		super(type, worldIn);
		xpReward = 25;
	}

	public MarluxiaEntity(Level world) {
		this(ModEntities.TYPE_MARLUXIA.get(), world);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(ARMOURED, false);
	}

	@Override
	protected void registerGoals() {
		// The boss script sits above the ordinary goals and holds MOVE and LOOK while an attack is playing, so nothing else can drag him around mid-animation
		marluxiaGoal = new MarluxiaGoal(this);
		this.goalSelector.addGoal(1, marluxiaGoal);

		this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createLivingAttributes()
				.add(Attributes.FOLLOW_RANGE, 40.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.3D)
				.add(Attributes.MAX_HEALTH, 800.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 1.0D)
				.add(Attributes.ATTACK_DAMAGE, 11.0D);
	}

	public boolean isArmored() {
		return getEntityData().get(ARMOURED);
	}

	public void setArmored(boolean armored) {
		getEntityData().set(ARMOURED, armored);
	}

	@Override
	public void tick() {
		super.tick();

		if (level().isClientSide()) {
			return;
		}

		if (tickCount < INTRO_TICKS) {
			setNoAi(true);
			setInvulnerable(true);
			setDeltaMovement(0, getDeltaMovement().y, 0);
		} else if (tickCount == INTRO_TICKS) {
			setNoAi(false);
			setInvulnerable(false);
		}

	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();

		if (marluxiaGoal != null) {
			marluxiaGoal.tickCooldown();
		}
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 1;
	}

	@Override
	public EntityHelper.MobType getKHMobType() {
		return EntityHelper.MobType.BOSS;
	}

	@Override
	protected ParticleOptions deathParticle() {
		return ParticleTypes.CHERRY_LEAVES;
	}

	@Override
	public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
		return false;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {
		this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.gracefulDahlia.get()));
		return super.finalizeSpawn(pLevel, pDifficulty, pSpawnType, pSpawnGroupData);
	}

	@Override
	public int getDefense() {
		return 200;
	}

}
