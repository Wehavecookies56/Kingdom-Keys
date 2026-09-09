package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.DyedItemColor;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
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

	public ApprenticeEntity(EntityType<? extends PathfinderMob> type, Level level) {
		super(type, level);
		setPersistenceRequired();
		setInvulnerable(true);
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

	public void setApprenticeLevel(int level) {
		int rank = Mth.clamp(level, MIN_LEVEL, MAX_LEVEL);
		entityData.set(LEVEL, rank);

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
		setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.starlight.get()));
		setItemSlot(EquipmentSlot.CHEST, dyed(Items.LEATHER_CHESTPLATE));
		setItemSlot(EquipmentSlot.LEGS, dyed(Items.LEATHER_LEGGINGS));
		setItemSlot(EquipmentSlot.FEET, dyed(Items.LEATHER_BOOTS));

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			setDropChance(slot, 0.0F);
		}
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

	private ItemStack dyed(Item item) {
		ItemStack stack = new ItemStack(item);
		stack.set(DataComponents.DYED_COLOR, new DyedItemColor(getUnion().getColour(), false));
		return stack;
	}

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(0, new FloatGoal(this));
		goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 0.6D));
		goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(3, new RandomLookAroundGoal(this));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createLivingAttributes()
				.add(Attributes.MAX_HEALTH, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.28D)
				.add(Attributes.FOLLOW_RANGE, 16.0D);
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
		setUnion(tag.contains("union") ? Union.fromByte(tag.getByte("union")) : rollUnion(seeded));

		// Dropped so the mod writes it again from the level above; an old one is a stale number
		setCustomName(null);

		// A world saved mid-spar would reload them hidden and stripped, so they come back as themselves; if their copy is somehow still out there the next check hides them again
		sparring = false;
		setInvisible(false);
		dress();
	}
}
