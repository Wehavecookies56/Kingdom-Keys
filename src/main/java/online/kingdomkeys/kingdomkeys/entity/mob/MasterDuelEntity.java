package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.MasterDuelGoal;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowMessagesPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class MasterDuelEntity extends BaseKHEntity {
	private static final EntityDataAccessor<Byte> UNION = SynchedEntityData.defineId(MasterDuelEntity.class, EntityDataSerializers.BYTE);
	private static final EntityDataAccessor<Integer> DUEL_LEVEL = SynchedEntityData.defineId(MasterDuelEntity.class, EntityDataSerializers.INT);

	public static final int MIN_LEVEL = 1;
	public static final int MAX_LEVEL = 100;

	public static final int DEFAULT_LEVEL = 25;

	public static final int MAGIC_FROM = 20;

	public static final int SECOND_GRADE_FROM = 40;
	public static final int THIRD_GRADE_FROM = 60;

	public static final int BEAT_TICKS = 20;
	public static final int COUNTDOWN_BEATS = 3;
	public static final int READY_TICKS = BEAT_TICKS * COUNTDOWN_BEATS;

	public static final double LEAVE_RANGE = 48.0D;

	public static final int LEAVE_GRACE = 60;

	public static final int CURE_AT = 25;
	public static final int ENDING_TICKS = 60;

	// Level one, and what each level past it adds. Level 60 lands around the mod's own bosses.
	private static final double BASE_HEALTH = 80.0D, HEALTH_PER_LEVEL = 12.0D;
	private static final double BASE_DAMAGE = 3.0D, DAMAGE_PER_LEVEL = 0.18D;
	private static final double BASE_SPEED = 0.25D, SPEED_PER_LEVEL = 0.0013D;
	private static final double ARMOUR_PER_LEVEL = 0.1D;
	private static final double KNOCKBACK_RESIST_PER_LEVEL = 0.01D;

	@Nullable
	private UUID duelist;

	private int leavingTicks;

	private int endingTicks = -1;

	public MasterDuelEntity(EntityType<? extends Monster> type, Level level) {
		super(type, level);
		this.xpReward = 0;
	}

	public MasterDuelEntity(Level level, Union union, int duelLevel) {
		this(ModEntities.TYPE_MASTER_DUEL.get(), level);
		setUnion(union);
		setDuelLevel(duelLevel);
		equipForUnion();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(UNION, Union.NONE.get());
		builder.define(DUEL_LEVEL, DEFAULT_LEVEL);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new MasterDuelGoal(this));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 12.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

		this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createLivingAttributes()
				.add(Attributes.FOLLOW_RANGE, 40.0D)
				.add(Attributes.MOVEMENT_SPEED, BASE_SPEED)
				.add(Attributes.MAX_HEALTH, BASE_HEALTH)
				.add(Attributes.ARMOR, 0.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 1.0D)
				.add(Attributes.ATTACK_DAMAGE, BASE_DAMAGE);
	}

	public Union getUnion() {
		return Union.fromByte(getEntityData().get(UNION));
	}

	public void setUnion(Union union) {
		getEntityData().set(UNION, union.get());

		if (!level().isClientSide) {
			equipForUnion();
		}
	}

	public int getDuelLevel() {
		return getEntityData().get(DUEL_LEVEL);
	}

	public void setDuelLevel(int duelLevel) {
		int level = Mth.clamp(duelLevel, MIN_LEVEL, MAX_LEVEL);
		getEntityData().set(DUEL_LEVEL, level);

		int steps = level - MIN_LEVEL;
		setBase(Attributes.MAX_HEALTH, BASE_HEALTH + HEALTH_PER_LEVEL * steps);
		setBase(Attributes.ATTACK_DAMAGE, BASE_DAMAGE + DAMAGE_PER_LEVEL * steps);
		setBase(Attributes.MOVEMENT_SPEED, BASE_SPEED + SPEED_PER_LEVEL * steps);
		setBase(Attributes.ARMOR, ARMOUR_PER_LEVEL * steps);
		setBase(Attributes.KNOCKBACK_RESISTANCE, Math.min(0.9D, KNOCKBACK_RESIST_PER_LEVEL * steps));

		setHealth(getMaxHealth());
		syncLevel();
	}

	private void syncLevel() {
		if (level().isClientSide) {
			return;
		}

		GlobalData data = GlobalData.get(this);
		if (data != null) {
			data.setLevel(getDuelLevel());
			PacketHandler.syncToAllAround(this, data);
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (level().isClientSide) {
			return;
		}

		// The level is usually set while he is still being built, before anyone can be tracking
		// him, and that first packet is dropped on the floor. This is the one that lands.
		if (tickCount == 2) {
			syncLevel();
		}

		// Once it is settled he is only seeing to the pupil, not fighting them
		if (endingTicks >= 0) {
			endingTicks++;

			if (endingTicks == CURE_AT) {
				tendTo();
			}

			if (endingTicks >= ENDING_TICKS) {
				dismiss();
			}
			return;
		}

		tickPreparation();
		tickDuelWatch();
	}

	public boolean isDuelist(Player pupil) {
		return duelist != null && pupil != null && duelist.equals(pupil.getUUID());
	}

	public void loseDuel() {
		if (endingTicks >= 0) {
			return;
		}

		endingTicks = 0;
		setTarget(null);
		setNoAi(true);
		setDeltaMovement(0.0D, getDeltaMovement().y, 0.0D);
		announce(Strings.Duel_Lost, Strings.Duel_Lost_Sub);
	}

	private void tendTo() {
		ServerPlayer pupil = getDuelist();

		if (pupil == null) {
			return;
		}

		lookAt(EntityAnchorArgument.Anchor.EYES, pupil.getEyePosition());
		setYBodyRot(getYRot());
		setYHeadRot(getYRot());

		ModMagic.CURAGA.get().castFromMob(pupil, this, null);
	}

	public boolean isSettled() {
		return endingTicks >= 0;
	}

	private void tickDuelWatch() {
		if (duelist == null) {
			return;
		}

		ServerPlayer pupil = getDuelist();

		if (pupil == null || !pupil.isAlive() || pupil.isSpectator() || pupil.level() != level()) {
			dismiss();
			return;
		}

		if (distanceToSqr(pupil) > LEAVE_RANGE * LEAVE_RANGE) {
			setTarget(null);
			if (++leavingTicks >= LEAVE_GRACE) {
				dismiss();
			}
			return;
		}

		leavingTicks = 0;
	}

	public void dismiss() {
		if (level() instanceof ServerLevel server) {
			server.sendParticles(deathParticle(), getX(), getY() + getBbHeight() * 0.5D, getZ(),
					40, getBbWidth() * 0.5D, getBbHeight() * 0.4D, getBbWidth() * 0.5D, 0.05D);
		}

		level().playSound(null, blockPosition(), ModSounds.portal.get(), SoundSource.HOSTILE, 0.8F, 1.2F);
		discard();
	}

	private int age() {
		return tickCount - 1;
	}

	public boolean isPreparing() {
		return age() < READY_TICKS;
	}

	private void tickPreparation() {
		int age = age();

		if (age > READY_TICKS) {
			return;
		}

		if (age % BEAT_TICKS == 0) {
			int left = (READY_TICKS - age) / BEAT_TICKS;
			announce(left > 0 ? String.valueOf(left) : Strings.Duel_Begin);
		}

		if (age == READY_TICKS) {
			setNoAi(false);
			setInvulnerable(false);
			return;
		}

		setNoAi(true);
		setInvulnerable(true);

		setDeltaMovement(0.0D, getDeltaMovement().y, 0.0D);
	}

	private void announce(String key) {
		announce(key, "", 2, 12, 4);
	}

	private void announce(String key, String subtitle) {
		announce(key, subtitle, 4, 50, 10);
	}

	private void announce(String key, String subtitle, int fadeIn, int stay, int fadeOut) {
		ServerPlayer pupil = getDuelist();
		if (pupil != null) {
			PacketHandler.sendTo(new SCShowMessagesPacket(List.of(new Utils.Title(key, subtitle, fadeIn, stay, fadeOut))), pupil);
		}
	}

	@Nullable
	public ServerPlayer getDuelist() {
		if (duelist != null && level().getServer() != null) {
			ServerPlayer player = level().getServer().getPlayerList().getPlayer(duelist);
			if (player != null) {
				return player;
			}
		}

		// Fallen back on so a master summoned by other means still counts someone down
		return getTarget() instanceof ServerPlayer player ? player : null;
	}

	public void setDuelist(Player pupil) {
		this.duelist = pupil == null ? null : pupil.getUUID();
	}

	private void setBase(Holder<Attribute> attribute, double value) {
		AttributeInstance instance = getAttribute(attribute);
		if (instance != null) {
			instance.setBaseValue(value);
		}
	}

	public void equipForUnion() {
		Union union = getUnion();
		Item[] robes = ForetellerEntity.robesFor(union);

		if (robes != null) {
			setItemSlot(EquipmentSlot.HEAD, new ItemStack(robes[0]));
			setItemSlot(EquipmentSlot.CHEST, new ItemStack(robes[1]));
			setItemSlot(EquipmentSlot.LEGS, new ItemStack(robes[2]));
			setItemSlot(EquipmentSlot.FEET, new ItemStack(robes[3]));
		}

		Item keyblade = ForetellerEntity.keybladeFor(union);
		if (keyblade != null) {
			setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(keyblade));
		}

		for (EquipmentSlot slot : EquipmentSlot.values()) {
			setDropChance(slot, 0.0F);
		}
	}

	@Override
	public Component getName() {
		Union union = getUnion();
		return Component.translatable("kingdomkeys.entity.master_duel", Component.translatable(ForetellerEntity.nameKeyFor(union)))
				.withStyle(style -> style.withColor(TextColor.fromRgb(union.getColour())));
	}

	@Override
	public EntityHelper.MobType getKHMobType() {
		return EntityHelper.MobType.BOSS;
	}

	@Override
	protected ParticleOptions deathParticle() {
		return ParticleTypes.END_ROD;
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 1;
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
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putByte("union", getUnion().get());
		tag.putInt("duelLevel", getDuelLevel());

		if (duelist != null) {
			tag.putUUID("duelist", duelist);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		setUnion(Union.fromByte(tag.getByte("union")));
		duelist = tag.hasUUID("duelist") ? tag.getUUID("duelist") : null;

		if (tag.contains("duelLevel")) {
			float health = getHealth();
			setDuelLevel(tag.getInt("duelLevel"));
			setHealth(Math.min(health, getMaxHealth()));
		}
	}
}
