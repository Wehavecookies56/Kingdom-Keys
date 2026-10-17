package online.kingdomkeys.kingdomkeys.world;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.drops.HeartEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.DuskEntity;
import online.kingdomkeys.kingdomkeys.entity.mob.IKHMob;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.leveling.Stat;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCHeartlessTransition;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowMessagesPacket;
import online.kingdomkeys.kingdomkeys.util.OpenSpot;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.CastleOblivionHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class HeartlessForm {
	public static final String FELL = "kingdomkeys.heartless_form.fell.intro", FELL_SUB = "kingdomkeys.heartless_form.fell.sub";
	public static final String RESTORED = "kingdomkeys.heartless_form.restored.intro", RESTORED_SUB = "kingdomkeys.heartless_form.restored.sub";
	public static final String NOBODY_OF = "kingdomkeys.entity.nobody_of";

	private static final String NOBODY_TAG = KingdomKeys.MODID + ":nobody_of";
	public static final String MEMORY_OWNER = "owner";
	private static final String STAT_LOSS = "heartless";

	private static final ResourceLocation SINK_SPEED = KingdomKeys.rl("heartless_sink");
	private static final double SINK_BONUS = 0.9D;

	// Tried in this order, as fractions of the configured distance
	private static final double[] LIGHT_SPREAD = {1D, 0.83D, 1.17D, 0.67D};
	private static final double[] LIGHT_FALLBACK = {8D, 6D, 10D, 4D};
	private static final double REACH = 1.8D;
	private static final double HEART_WATCH = 96D;
	private static final int UPKEEP = 40;
	private static final double NOBODY_RANGE = 48D;
	private static final int NOBODY_CHECK = 20, STALE_CHECK = 100;

	private static final EntityDimensions SHADOW_SIZE = EntityDimensions.scalable(0.6F, 0.9F).withEyeHeight(0.7F);
	private static final EntityDimensions SUNK_SIZE = EntityDimensions.scalable(0.6F, 0.3F).withEyeHeight(0.2F);

	public static boolean isHeartless(Player player) {
		return player != null && PlayerData.get(player).isHeartless();
	}

	// Only on the ground: in the air there is nothing to sink into, so a jump brings them out of it
	public static boolean isSunk(Player player) {
		return isHeartless(player) && player.isShiftKeyDown() && player.onGround();
	}

	/** Called instead of the death. Returns false if this player cannot be taken, and dies as usual */
	public static boolean fallIntoDarkness(LivingDeathEvent event, ServerPlayer player) {
		if (isHeartless(player) || player.isCreative() || player.isSpectator()) {
			return false;
		}

		event.setCanceled(true);
		player.setHealth(player.getMaxHealth());
		player.clearFire();
		turn(player);
		return true;
	}

	/** Turns them into a Heartless, with their heart sent off to wait as the light. Returns false if they already are one */
	public static boolean turn(ServerPlayer player) {
		PlayerData data = PlayerData.get(player);
		if (data.isHeartless()) {
			return false;
		}

		if (!DriveForm.NONE.equals(data.getActiveDriveForm())) {
			DriveForm form = ModDriveForms.registry.get(data.getActiveDriveForm());
			if (form != null) {
				form.endDrive(player);
			}
		}

		Utils.summonKeyblade(player, true, DriveForm.NONE);

		ServerLevel level = player.serverLevel();
		Vec3 fell = player.position();
		double distance = ModConfigs.SERVER.heartlessLightDistance.get();
		double[] distances = new double[LIGHT_SPREAD.length];
		for (int i = 0; i < distances.length; i++) {
			distances[i] = distance * LIGHT_SPREAD[i];
		}

		Vec3 light = OpenSpot.find(level, null, fell, level.random.nextFloat() * 360F, distances, ModEntities.TYPE_SHADOW.get(), 0.2D);
		if (light == null) {
			light = OpenSpot.find(level, player.getEyePosition(), fell, player.getYRot() + 180F, LIGHT_FALLBACK, ModEntities.TYPE_SHADOW.get(), 0.1D);
		}
		if (light == null) {
			light = fell;
		}

		data.setHeartless(true);
		data.setLight(level.dimension(), BlockPos.containing(light));
		loseStats(data);
		level.addFreshEntity(HeartEntity.guide(level, fell.add(0D, 1D, 0D), light, player.getUUID()));

		// The Heartless that did it forget about them: one of their own now
		for (Mob mob : level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(32D), mob -> mob.getTarget() == player && isHeartlessMob(mob))) {
			mob.setTarget(null);
		}

		level.sendParticles(ParticleTypes.LARGE_SMOKE, player.getX(), player.getY() + 1D, player.getZ(), 40, 0.4D, 0.8D, 0.4D, 0.02D);
		level.playSound(null, player.blockPosition(), SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 0.4F, 1.6F);

		PacketHandler.sendTo(new SCHeartlessTransition(), player);
		PacketHandler.sendTo(new SCShowMessagesPacket(List.of(new Utils.Title(FELL, FELL_SUB, 10, 70, 20))), player);
		PacketHandler.syncToAllAround(player, data);
		return true;
	}

	// Back to human, with their Nobody now loose somewhere. The sneak bonus comes off on the next tick, with the hitbox
	private static void clear(PlayerData data) {
		data.setHeartless(false);
		data.setLight(null, null);
		data.setNobodyOwed(true);
		data.setNobodyId(null);
		data.setNobodyMissingTicks(0);
	}

	/** Makes them human again, as reaching their heart would. Returns false if they were not a Heartless */
	public static boolean restore(ServerPlayer player) {
		PlayerData data = PlayerData.get(player);
		if (!data.isHeartless()) {
			return false;
		}
		restore(player, data);
		return true;
	}

	private static void restore(ServerPlayer player, PlayerData data) {
		clear(data);

		ServerLevel level = player.serverLevel();
		level.getEntitiesOfClass(HeartEntity.class, player.getBoundingBox().inflate(8D), heart -> heart.isGuideFor(player.getUUID())).forEach(Entity::discard);
		level.sendParticles(ParticleTypes.END_ROD, player.getX(), player.getY() + 1D, player.getZ(), 60, 0.3D, 0.8D, 0.3D, 0.08D);
		level.playSound(null, player.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.PLAYERS, 1F, 1.4F);
		level.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS, 1.5F, 1F);

		PacketHandler.sendTo(new SCShowMessagesPacket(List.of(new Utils.Title(RESTORED, RESTORED_SUB, 10, 70, 20))), player);
		PacketHandler.syncToAllAround(player, data);
	}

	private static boolean isHeartlessMob(Entity entity) {
		return entity instanceof IKHMob mob && (mob.getKHMobType() == MobType.HEARTLESS_EMBLEM || mob.getKHMobType() == MobType.HEARTLESS_PUREBLOOD);
	}

	// Last form each player's hitbox was sized for. One map per side: in singleplayer the client and the integrated
	// server tick on different threads, and sharing one unsynchronised map dropped the client's resize
	private final Map<Player, Integer> sizedAsClient = new WeakHashMap<>(), sizedAsServer = new WeakHashMap<>();
	private static final int HUMAN = 0, SHADOW = 1, SUNK = 2;

	@SubscribeEvent
	public void tick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		boolean heartless = isHeartless(player);

		// Only when the form changes (or the player is new to this side): both sides resize the hitbox, the server
		// swaps the sneak bonus, which reaches the client like any other attribute
		// Sinking and surfacing (leaving the ground counts) resize the hitbox too
		Map<Player, Integer> sizedAs = player.level().isClientSide ? sizedAsClient : sizedAsServer;
		int form = !heartless ? HUMAN : isSunk(player) ? SUNK : SHADOW;
		Integer was = sizedAs.put(player, form);
		if (was == null || was != form) {
			player.refreshDimensions();
		}
		if (was == null || (was == HUMAN) != (form == HUMAN)) {
			if (!player.level().isClientSide) {
				if (heartless) {
					addSink(player);
				} else {
					removeSink(player);
				}
			}
		}

		if (!heartless && player instanceof ServerPlayer owner && player.tickCount % NOBODY_CHECK == 0) {
			watchNobody(owner);
		}

		if (!heartless || !(player instanceof ServerPlayer serverPlayer)) {
			return;
		}

		PlayerData data = PlayerData.get(player);

		BlockPos lightPos = data.getLightPos();
		ResourceKey<Level> lightDim = data.getLightDimension();
		if (lightPos == null || lightDim == null) {
			restore(serverPlayer, data);
			return;
		}

		if (!player.level().dimension().equals(lightDim)) {
			return;
		}

		Vec3 light = Vec3.atBottomCenterOf(lightPos).add(0D, 1D, 0D);
		if (player.position().add(0D, player.getBbHeight() / 2D, 0D).distanceTo(light) <= REACH) {
			restore(serverPlayer, data);
			return;
		}

		// The heart does not survive being unloaded or saved, so whoever it belongs to keeps one burning while they are near
		if (player.tickCount % UPKEEP == 0 && player.position().distanceTo(light) <= HEART_WATCH) {
			ServerLevel level = serverPlayer.serverLevel();
			boolean burning = !level.getEntitiesOfClass(HeartEntity.class, new AABB(lightPos).inflate(HEART_WATCH), heart -> heart.isGuideFor(player.getUUID())).isEmpty();
			if (!burning) {
				level.addFreshEntity(HeartEntity.guide(level, light, Vec3.atBottomCenterOf(lightPos), player.getUUID()));
			}
		}
	}

	private static void addSink(Player player) {
		AttributeInstance sneaking = player.getAttribute(Attributes.SNEAKING_SPEED);
		if (sneaking != null && !sneaking.hasModifier(SINK_SPEED)) {
			sneaking.addTransientModifier(new AttributeModifier(SINK_SPEED, SINK_BONUS, AttributeModifier.Operation.ADD_VALUE));
		}
	}

	private static void removeSink(Player player) {
		AttributeInstance sneaking = player.getAttribute(Attributes.SNEAKING_SPEED);
		if (sneaking != null && sneaking.hasModifier(SINK_SPEED)) {
			sneaking.removeModifier(SINK_SPEED);
		}
	}

	@SubscribeEvent
	public void size(EntityEvent.Size event) {
		if (event.getEntity() instanceof Player player && isHeartless(player) && event.getPose() != Pose.SLEEPING) {
			event.setNewSize(event.getPose() == Pose.CROUCHING && player.onGround() ? SUNK_SIZE : SHADOW_SIZE);
		}
	}

	// Struck down by a Heartless with no name of its own: they become one instead of dying. Early, so nothing else treats it as a death
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void struckDown(LivingDeathEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer player) || !ModConfigs.playerSpawnHeartless) {
			return;
		}
		Entity killer = event.getSource().getEntity();
		if (killer != null && !killer.hasCustomName() && isHeartlessMob(killer)) {
			fallIntoDarkness(event, player);
		}
	}

	// A shadow dying of something else comes back human with their Nobody out there
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void died(LivingDeathEvent event) {
		LivingEntity entity = event.getEntity();
		if (event.isCanceled() || entity.level().isClientSide) {
			return;
		}

		if (entity instanceof ServerPlayer player) {
			PlayerData data = PlayerData.get(player);
			if (data != null && data.isHeartless()) {
				clear(data);
			}
		}

		UUID owner = nobodyOwner(entity);
		if (owner != null) {
			ItemStack memory = new ItemStack(ModItems.memoryFromAnotherSelf.get());
			if (entity.getCustomName() != null) {
				memory.set(DataComponents.LORE, new ItemLore(List.of(entity.getCustomName())));
			}
			CompoundTag ownerTag = new CompoundTag();
			ownerTag.putUUID(MEMORY_OWNER, owner);
			memory.set(DataComponents.CUSTOM_DATA, CustomData.of(ownerTag));

			// Whoever or whatever brings it down, the memory finds its way back to them
			ServerPlayer ownerPlayer = entity.getServer() == null ? null : entity.getServer().getPlayerList().getPlayer(owner);
			if (ownerPlayer != null) {
				PlayerData ownerData = PlayerData.get(ownerPlayer);
				if (ownerData != null && entity.getUUID().equals(ownerData.getNobodyId())) {
					ownerData.setNobodyId(null);
					ownerData.setNobodyMissingTicks(0);
				}
				Utils.giveItems(ownerPlayer, false, memory);
			} else {
				entity.level().addFreshEntity(new ItemEntity(entity.level(), entity.getX(), entity.getY() + 0.5D, entity.getZ(), memory));
			}
		}
	}

	private static final double SUNK_JUMP = 1.8D;

	// Springing up out of the ground, as Shadows do. The jump is the client's to make, and this runs on its side too
	@SubscribeEvent
	public void jump(LivingEvent.LivingJumpEvent event) {
		if (event.getEntity() instanceof Player player && isSunk(player)) {
			Vec3 motion = player.getDeltaMovement();
			player.setDeltaMovement(motion.x, motion.y * SUNK_JUMP, motion.z);
		}
	}

	@SubscribeEvent
	public void incoming(LivingIncomingDamageEvent event) {
		// Sunk into the ground nothing reaches them, short of falling out of the world
		if (event.getEntity() instanceof Player hurt && isSunk(hurt) && !event.getSource().is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
			event.setCanceled(true);
			return;
		}

		// A shadow fights with its claws, whatever it happens to be holding, and not at all while sunk
		if (event.getSource().getEntity() instanceof Player attacker && event.getSource().getDirectEntity() == attacker && isHeartless(attacker)) {
			if (attacker.isShiftKeyDown() && attacker.onGround()) {
				event.setCanceled(true);
				return;
			}
			event.setAmount(2F + PlayerData.get(attacker).getLevel() / 20F);
		}
	}

	// No using items
	@SubscribeEvent
	public void useItem(PlayerInteractEvent.RightClickItem event) {
		if (isHeartless(event.getEntity())) {
			event.setCanceled(true);
		}
	}

	/*@SubscribeEvent
	public void useBlock(PlayerInteractEvent.RightClickBlock event) {
		if (!isHeartless(event.getEntity())) {
			return;
		}
		// In case we want to restrict a Shadow from interacting with blocks.
		BlockState state = event.getLevel().getBlockState(event.getPos());
		if (state.is(BlockTags.DOORS) || state.is(BlockTags.TRAPDOORS) || state.is(BlockTags.FENCE_GATES) || state.is(BlockTags.BUTTONS) || state.getBlock() instanceof LeverBlock) {
			event.setUseItem(TriState.FALSE);
		} else {
			event.setCanceled(true);
		}
	}*/

	@SubscribeEvent
	public void useEntity(PlayerInteractEvent.EntityInteract event) {
		if (isHeartless(event.getEntity())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void target(LivingChangeTargetEvent event) {
		if (event.getNewAboutToBeSetTarget() instanceof Player player && isHeartless(player) && isHeartlessMob(event.getEntity())) {
			event.setCanceled(true);
		}
	}

	// Their Nobody does not wait where they fell: it takes the place of the next Dusk that comes into being near them
	@SubscribeEvent
	public void nobodyArrives(EntityJoinLevelEvent event) {
		if (event.getLevel().isClientSide() || event.loadedFromDisk() || !(event.getEntity() instanceof DuskEntity dusk) || dusk.hasCustomName()
				|| !(event.getLevel() instanceof ServerLevel level) || CastleOblivionHandler.isInterior(level.dimension())) {
			return;
		}

		for (ServerPlayer player : level.players()) {
			PlayerData data = PlayerData.get(player);
			if (data == null || !data.isNobodyOwed() || player.distanceTo(dusk) > NOBODY_RANGE) {
				continue;
			}

			becomeNobodyOf(dusk, player, data);
			data.setNobodyOwed(false);
			PacketHandler.syncToAllAround(player, data);
			return;
		}
	}

	private static void becomeNobodyOf(DuskEntity dusk, ServerPlayer player, PlayerData data) {
		String chosen = data.getNobodyName();
		String playerName = player.getGameProfile().getName();
		dusk.setCustomName(isValidNobodyName(playerName, chosen) ? Component.literal(chosen) : Component.translatable(NOBODY_OF, player.getDisplayName().getString()));
		dusk.setPersistenceRequired();

		String[] nobody = ModConfigs.playerSpawnHeartlessData.get(1).split(",");
		dusk.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Math.max(player.getMaxHealth() * Double.parseDouble(nobody[1]) / 100, dusk.getMaxHealth()));
		dusk.setHealth(dusk.getMaxHealth());
		dusk.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Math.max(data.getStrength(true) * Double.parseDouble(nobody[2]) / 100, dusk.getAttribute(Attributes.ATTACK_DAMAGE).getBaseValue()));

		GlobalData global = GlobalData.get(dusk);
		if (global != null) {
			global.setLevel(data.getLevel());
		}

		CompoundTag tag = dusk.getPersistentData();
		tag.putUUID(NOBODY_TAG, player.getUUID());
		data.setNobodyId(dusk.getUUID());
		data.setNobodyMissingTicks(0);
	}

	// A Nobody that stays out of reach long enough is given up on: the next Dusk to turn up near them takes its place
	private static void watchNobody(ServerPlayer owner) {
		PlayerData data = PlayerData.get(owner);
		if (data == null || data.getNobodyId() == null) {
			return;
		}

		if (findLoaded(owner.server, data.getNobodyId()) != null) {
			data.setNobodyMissingTicks(0);
			return;
		}

		data.setNobodyMissingTicks(data.getNobodyMissingTicks() + NOBODY_CHECK);
		if (data.getNobodyMissingTicks() >= ModConfigs.SERVER.nobodyLostMinutes.get() * 1200) {
			data.setNobodyId(null);
			data.setNobodyMissingTicks(0);
			data.setNobodyOwed(true);
			PacketHandler.syncToAllAround(owner, data);
		}
	}

	private static Entity findLoaded(MinecraftServer server, UUID id) {
		for (ServerLevel level : server.getAllLevels()) {
			Entity entity = level.getEntity(id);
			if (entity != null) {
				return entity;
			}
		}
		return null;
	}

	// One given up on, or left over from an earlier fall. Only told apart while its owner is online
	private static boolean isStaleNobody(Entity entity) {
		UUID owner = nobodyOwner(entity);
		if (owner == null || entity.getServer() == null) {
			return false;
		}
		ServerPlayer player = entity.getServer().getPlayerList().getPlayer(owner);
		PlayerData data = player == null ? null : PlayerData.get(player);
		if (data == null) {
			return false;
		}
		// Made before they were tracked: it is the one they have
		if (data.getNobodyId() == null && !data.isNobodyOwed()) {
			data.setNobodyId(entity.getUUID());
			return false;
		}
		return !entity.getUUID().equals(data.getNobodyId());
	}

	@SubscribeEvent
	public void staleNobodyLoaded(EntityJoinLevelEvent event) {
		if (!event.getLevel().isClientSide() && event.loadedFromDisk() && event.getEntity() instanceof DuskEntity && isStaleNobody(event.getEntity())) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void staleNobodyTick(EntityTickEvent.Post event) {
		Entity entity = event.getEntity();
		if (entity instanceof DuskEntity && entity.tickCount % STALE_CHECK == 0 && entity.level() instanceof ServerLevel level && isStaleNobody(entity)) {
			level.sendParticles(ParticleTypes.LARGE_SMOKE, entity.getX(), entity.getY() + 1D, entity.getZ(), 30, 0.3D, 0.6D, 0.3D, 0.02D);
			entity.discard();
		}
	}

	/** Organization style */
	public static boolean isValidNobodyName(String playerName, String nobodyName) {
		if (playerName == null || nobodyName == null || nobodyName.isEmpty()) {
			return false;
		}
		char[] expected = (playerName + "x").toLowerCase(Locale.ROOT).toCharArray();
		char[] given = nobodyName.toLowerCase(Locale.ROOT).toCharArray();
		Arrays.sort(expected);
		Arrays.sort(given);
		return Arrays.equals(expected, given);
	}

	private static void loseStats(PlayerData data) {
		int percent = ModConfigs.SERVER.heartlessStatLoss.get();
		addLoss(data.getStrengthStat(), percent);
		addLoss(data.getMagicStat(), percent);
		addLoss(data.getDefenseStat(), percent);
	}

	private static void addLoss(Stat stat, int percent) {
		if (percent <= 0 || stat.hasModifier(STAT_LOSS)) {
			return;
		}
		stat.addModifier(STAT_LOSS, -percent, false, true);
	}

	public static boolean hasLostStats(PlayerData data) {
		return data.getStrengthStat().hasModifier(STAT_LOSS) || data.getMagicStat().hasModifier(STAT_LOSS) || data.getDefenseStat().hasModifier(STAT_LOSS);
	}

	public static void recoverStats(PlayerData data) {
		data.getStrengthStat().removeModifier(STAT_LOSS);
		data.getMagicStat().removeModifier(STAT_LOSS);
		data.getDefenseStat().removeModifier(STAT_LOSS);
	}

	public static UUID nobodyOwner(Entity entity) {
		return entity.getPersistentData().hasUUID(NOBODY_TAG) ? entity.getPersistentData().getUUID(NOBODY_TAG) : null;
	}
}
