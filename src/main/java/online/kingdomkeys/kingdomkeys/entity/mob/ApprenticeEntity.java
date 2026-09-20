package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.ApprenticeCombatGoal;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.DuelGoal;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.world.DialogueHandler;

public class ApprenticeEntity extends PathfinderMob implements Dueller {

	private static final EntityDataAccessor<Integer> LEVEL = SynchedEntityData.defineId(ApprenticeEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Byte> UNION = SynchedEntityData.defineId(ApprenticeEntity.class, EntityDataSerializers.BYTE);

	private static final EntityDataAccessor<Boolean> SPARRING = SynchedEntityData.defineId(ApprenticeEntity.class, EntityDataSerializers.BOOLEAN);

	private static final ResourceLocation DIALOGUE = KingdomKeys.rl("apprentice");

	public static final int MIN_LEVEL = 1, MAX_LEVEL = 50;

	private static final int WANDER_RADIUS = 24;
	private static final double WATCH_RANGE = 32.0D;

	private BlockPos home;

	private int outfit;

	private int trim;

	private static final int[] TRIMS = {
			0xF9FFFE, // white
			0xE9ECEC, // bone
			0x9D9D97, // light grey
			0x5E5E5E, // slate
			0x1D1D21, // black
			0x8F5D3A, // leather
			0xC9A227, // brass
			0x7A5CA8  // plum
	};

	public ApprenticeEntity(EntityType<? extends PathfinderMob> type, Level level) {
		super(type, level);
		setPersistenceRequired();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(LEVEL, MIN_LEVEL);
		builder.define(UNION, Union.NONE.get());
		builder.define(SPARRING, false);
	}

	public int getApprenticeLevel() {
		return entityData.get(LEVEL);
	}

	private static final double BASE_DAMAGE = 2.0D, DAMAGE_PER_LEVEL = 0.12D;
	private static final double BASE_HEALTH = 20.0D, HEALTH_PER_LEVEL = 0.8D;

	public void setApprenticeLevel(int level) {
		int rank = Mth.clamp(level, MIN_LEVEL, MAX_LEVEL);
		entityData.set(LEVEL, rank);

		AttributeInstance damage = getAttribute(Attributes.ATTACK_DAMAGE);
		if (damage != null) {
			damage.setBaseValue(BASE_DAMAGE + DAMAGE_PER_LEVEL * (rank - MIN_LEVEL));
		}

		AttributeInstance health = getAttribute(Attributes.MAX_HEALTH);
		if (health != null) {
			boolean unhurt = getHealth() >= (float) health.getValue();
			health.setBaseValue(BASE_HEALTH + HEALTH_PER_LEVEL * (rank - MIN_LEVEL));

			if (unhurt) {
				setHealth(getMaxHealth());
			}
		}

		if (!level().isClientSide) {
			GlobalData globalData = GlobalData.get(this);

			if (globalData != null) {
				globalData.setLevel(rank);
			}
		}
	}

	public Union getUnion() {
		return Union.fromByte(entityData.get(UNION));
	}

	public void setUnion(Union union) {
		entityData.set(UNION, union.get());
	}

	public static Union rollUnion(RandomSource random) {
		Union[] choosable = Union.choosable();
		return choosable[random.nextInt(choosable.length)];
	}

	public BlockPos getHome() {
		return home == null ? blockPosition() : home;
	}

	public void setHome(BlockPos home) {
		this.home = home;
		restrictTo(home, WANDER_RADIUS);
	}

	private void moveRadius(boolean fighting) {
		if (home == null) {
			return;
		}

		int radius = fighting ? (int) WATCH_RANGE : WANDER_RADIUS;

		if (getRestrictRadius() != radius) {
			restrictTo(home, radius);
		}
	}

	public static int rollLevel(RandomSource random) {
		return MIN_LEVEL + random.nextInt(MAX_LEVEL - MIN_LEVEL + 1);
	}

	public void dress() {
		ensureOutfit();
		dressAs(this, outfit, trim, getUnion());
	}

	public static void dressAs(Mob wearer, int outfit, int trim, Union union) {
		int color = union.getColour();
		wearer.setItemSlot(EquipmentSlot.CHEST, ModItems.createApprenticeArmor(ArmorItem.Type.CHESTPLATE, outfit, color, trim));
		wearer.setItemSlot(EquipmentSlot.LEGS, ModItems.createApprenticeArmor(ArmorItem.Type.LEGGINGS, outfit, color, trim));
		wearer.setItemSlot(EquipmentSlot.FEET, ModItems.createApprenticeArmor(ArmorItem.Type.BOOTS, outfit, color, trim));

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			wearer.setDropChance(slot, 0.0F);
		}
	}

	public int getOutfit() {
		ensureOutfit();
		return outfit;
	}

	public int getTrim() {
		ensureOutfit();
		return trim;
	}

	public static int rollTrim(RandomSource random) {
		return TRIMS[random.nextInt(TRIMS.length)];
	}

	private void ensureOutfit() {
		if (outfit == 0) {
			outfit = random.nextInt(4) + 1;
		}
		if (trim == 0) {
			trim = rollTrim(random);
		}
	}

	public boolean isSparring() {
		return entityData.get(SPARRING);
	}

	private void setSparring(boolean sparring) {
		entityData.set(SPARRING, sparring);
	}

	private final KeybladeSummon summon = new KeybladeSummon(this);
	private final Duel duel = new Duel(this);

	private static final ResourceLocation DUEL_BOOST = KingdomKeys.rl("duel_boost");

	@Override
	public Duel duel() {
		return duel;
	}

	@Override
	public int getDuelLevel() {
		return getApprenticeLevel();
	}

	@Override
	public void beginDuel(Player pupil, int duelLevel) {
		if (pupil == null || level().isClientSide) {
			return;
		}

		bowOut();

		setSparring(true);

		setDuelist(pupil);
		setTarget(pupil);

		Dueller.lendWorth(this, duelLevel, DUEL_BOOST);
		setHealth(getMaxHealth());

		clearRestriction();
	}

	@Override
	public void bowOut() {
		setSparring(false);
		duel.clear();

		Dueller.takeBack(this, DUEL_BOOST);
		setNoAi(false);
		setInvulnerable(false);
		setTarget(null);

		setHealth(getMaxHealth());

		if (home != null) {
			restrictTo(home, WANDER_RADIUS);
		}
	}

	private void strip() {
		summon.cancel();

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			setItemSlot(slot, ItemStack.EMPTY);
		}
	}

	@Override
	public KeybladeSummon keybladeSummon() {
		return summon;
	}

	@Override
	public ItemStack keybladeToCall() {
		return new ItemStack(ModItems.starlight.get());
	}

	@Override
	public int callingRank() {
		return getApprenticeLevel();
	}

	private static final int STAND_DOWN_MIN = 50, STAND_DOWN_MAX = 110;

	private static final int DANGER_CHECK = 10;

	private int quiet = STAND_DOWN_MAX;

	private int rollStandDown() {
		return STAND_DOWN_MIN + random.nextInt(STAND_DOWN_MAX - STAND_DOWN_MIN + 1);
	}

	private void standDown(boolean fighting) {
		if (fighting) {
			quiet = rollStandDown();
			return;
		}

		if (!hasKeyblade()) {
			return;
		}

		if (tickCount % DANGER_CHECK == 0 && darknessNearby()) {
			quiet = rollStandDown();
			return;
		}

		if (--quiet <= 0) {
			dismissKeyblade();
		}
	}

	private boolean darknessNearby() {
		return !level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(WANDER_RADIUS),
				darkness -> darkness.isAlive() && isDarkness(darkness) && isWithinRestriction(darkness.blockPosition())).isEmpty();
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new DuelGoal<>(this));
		goalSelector.addGoal(2, new ApprenticeCombatGoal(this));

		goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.6D));
		goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(5, new RandomLookAroundGoal(this));

		targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, target -> !isSparring() && isDarkness(target)));
	}

	public static boolean isDarkness(LivingEntity target) {
		if (!(target instanceof IKHMob mob)) {
			return false;
		}

		EntityHelper.MobType type = mob.getKHMobType();
		return type == EntityHelper.MobType.HEARTLESS_PUREBLOOD || type == EntityHelper.MobType.HEARTLESS_EMBLEM || type == EntityHelper.MobType.NOBODY;
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createLivingAttributes()
				.add(Attributes.MAX_HEALTH, BASE_HEALTH)
				.add(Attributes.MOVEMENT_SPEED, 0.28D)
				.add(Attributes.FOLLOW_RANGE, WATCH_RANGE)
				.add(Attributes.ATTACK_DAMAGE, BASE_DAMAGE);
	}

	@Override
	public void aiStep() {
		updateSwingTime();
		super.aiStep();
	}

	private static final int MEND_INTERVAL = 40;

	@Override
	public void tick() {
		super.tick();

		if (level().isClientSide) {
			return;
		}

		summon.tick();
		duel.tick();

		if (isSparring()) {
			if (isDuelling() && getTarget() != getDuelist()) {
				setTarget(getDuelist());
			}
			return;
		}

		// Off the leash while there is something to run down, back on it once there is not
		boolean fighting = getTarget() != null && getTarget().isAlive();
		moveRadius(fighting);

		standDown(fighting);

		// They tend to their own wounds between fights rather than standing their post half dead forever
		if (!fighting && tickCount % MEND_INTERVAL == 0 && getHealth() < getMaxHealth()) {
			heal(1.0F);
		}
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (level().isClientSide || hand != InteractionHand.MAIN_HAND) {
			return InteractionResult.SUCCESS;
		}

		if (isSparring() || !(player instanceof ServerPlayer serverPlayer)) {
			return InteractionResult.FAIL;
		}

		PlayerData playerData = PlayerData.get(player);

		if (playerData == null || !playerData.hasUnion() || playerData.isOrgMember()) {
			return InteractionResult.FAIL;
		}

		DialogueHandler.start(serverPlayer, this, DIALOGUE);
		return InteractionResult.SUCCESS;
	}

	private boolean vulnerableTo(DamageSource source) {
		// So /kill and these still work on them
		if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			return true;
		}

		if (isSparring()) {
			return !isPreparing() && !isSettled() && source.getEntity() instanceof Player pupil && isDuelist(pupil);
		}

		return source.getEntity() instanceof LivingEntity attacker && isDarkness(attacker);
	}

	private boolean gettingHurt;

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (!vulnerableTo(source)) {
			return false;
		}

		gettingHurt = true;

		try {
			return super.hurt(source, amount);
		} finally {
			gettingHurt = false;
		}
	}

	@Override
	public void knockback(double strength, double x, double z) {
		if (gettingHurt) {
			super.knockback(strength, x, z);
		}
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return !vulnerableTo(source) || super.isInvulnerableTo(source);
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public void push(Entity entity) {
	}

	@Override
	protected void pushEntities() {
	}

	@Override
	public boolean removeWhenFarAway(double distance) {
		return false;
	}

	@Override
	public boolean requiresCustomPersistence() {
		return true;
	}

	@Override
	public boolean isAffectedByPotions() {
		return false;
	}

	@Override
	public boolean canBeLeashed() {
		return false;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putInt("apprentice_level", getApprenticeLevel());
		tag.putInt("apprentice_outfit", getOutfit());
		tag.putInt("apprentice_trim", getTrim());
		tag.putByte("union", getUnion().get());

		if (home != null) {
			tag.putLong("home", home.asLong());
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);

		if (tag.contains("home")) {
			setHome(BlockPos.of(tag.getLong("home")));
		}

		RandomSource seeded = RandomSource.create(getHome().asLong());

		setApprenticeLevel(tag.contains("apprentice_level") ? tag.getInt("apprentice_level") : rollLevel(seeded));
		outfit = tag.contains("apprentice_outfit") ? tag.getInt("apprentice_outfit") : seeded.nextInt(4) + 1;
		trim = tag.contains("apprentice_trim") ? tag.getInt("apprentice_trim") : rollTrim(seeded);
		setUnion(tag.contains("union") ? Union.fromByte(tag.getByte("union")) : rollUnion(seeded));

		setCustomName(null);

		setSparring(false);
		duel.clear();
		setNoAi(false);
		setInvulnerable(false);
		setInvisible(false);
		dress();
	}
}
