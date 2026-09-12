package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.ApprenticeCombatGoal;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.world.DialogueHandler;

public class ApprenticeEntity extends PathfinderMob {

	private static final EntityDataAccessor<Integer> LEVEL = SynchedEntityData.defineId(ApprenticeEntity.class, EntityDataSerializers.INT);
	private static final EntityDataAccessor<Byte> UNION = SynchedEntityData.defineId(ApprenticeEntity.class, EntityDataSerializers.BYTE);

	private static final ResourceLocation DIALOGUE = KingdomKeys.rl("apprentice");

	public static final int MIN_LEVEL = 1, MAX_LEVEL = 50;

	private static final int WANDER_RADIUS = 24;

	private static final double SPAR_WATCH_RANGE = 32.0D;
	private static final int SPAR_CHECK_INTERVAL = 20;

	private BlockPos home;

	private boolean sparring;
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
	}

	public int getApprenticeLevel() {
		return entityData.get(LEVEL);
	}

	private static final double BASE_DAMAGE = 2.0D, DAMAGE_PER_LEVEL = 0.12D;

	public void setApprenticeLevel(int level) {
		int rank = Mth.clamp(level, MIN_LEVEL, MAX_LEVEL);
		entityData.set(LEVEL, rank);

		AttributeInstance damage = getAttribute(Attributes.ATTACK_DAMAGE);
		if (damage != null) {
			damage.setBaseValue(BASE_DAMAGE + DAMAGE_PER_LEVEL * (rank - MIN_LEVEL));
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

	public static int rollLevel(RandomSource random) {
		return MIN_LEVEL + random.nextInt(MAX_LEVEL - MIN_LEVEL + 1);
	}

	public void dress() {
		ensureOutfit();
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.starlight.get()));

		int color = getUnion().getColour();
		setItemSlot(EquipmentSlot.CHEST, ModItems.createApprenticeArmor(ArmorItem.Type.CHESTPLATE, outfit, color, trim));
		setItemSlot(EquipmentSlot.LEGS, ModItems.createApprenticeArmor(ArmorItem.Type.LEGGINGS, outfit, color, trim));
		setItemSlot(EquipmentSlot.FEET, ModItems.createApprenticeArmor(ArmorItem.Type.BOOTS, outfit, color, trim));

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			setDropChance(slot, 0.0F);
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
		return sparring;
	}

	public void standAside() {
		sparring = true;
		setInvisible(true);
		strip();
	}

	private void strip() {
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			setItemSlot(slot, ItemStack.EMPTY);
		}
	}


	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new ApprenticeCombatGoal(this));

		goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.6D));
		goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(4, new RandomLookAroundGoal(this));

		targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Mob.class, 10, true, false, target -> !sparring && isDarkness(target)));
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
				.add(Attributes.MAX_HEALTH, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.28D)
				.add(Attributes.FOLLOW_RANGE, 16.0D)
				.add(Attributes.ATTACK_DAMAGE, BASE_DAMAGE);
	}

	@Override
	public void tick() {
		super.tick();

		if (level().isClientSide || tickCount % SPAR_CHECK_INTERVAL != 0) {
			return;
		}

		// Out of sight while their sparring copy is on the floor, the way the masters do it
		boolean copyOut = !level().getEntitiesOfClass(ApprenticeDuelEntity.class, getBoundingBox().inflate(SPAR_WATCH_RANGE), copy -> copy.isAlive() && copy.isCopyOf(this)).isEmpty();

		if (copyOut == sparring) {
			return;
		}

		sparring = copyOut;
		setInvisible(sparring);

		// An invisible mob still shows what it wears and holds, so it all has to actually come off
		if (sparring) {
			strip();
		} else {
			dress();
		}
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		if (level().isClientSide || hand != InteractionHand.MAIN_HAND) {
			return InteractionResult.SUCCESS;
		}

		if (sparring || !(player instanceof ServerPlayer serverPlayer)) {
			return InteractionResult.FAIL;
		}

		PlayerData playerData = PlayerData.get(player);

		if (playerData == null || !playerData.hasUnion() || playerData.isOrgMember()) {
			return InteractionResult.FAIL;
		}

		DialogueHandler.start(serverPlayer, this, DIALOGUE);
		return InteractionResult.SUCCESS;
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		return false;
	}

	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return true;
	}

	@Override
	public boolean canBeHitByProjectile() {
		return false;
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

		// A world saved mid-spar would reload them hidden and stripped, so they come back as themselves; if their copy is somehow still out there the next check hides them again
		sparring = false;
		setInvisible(false);
		dress();
	}
}
