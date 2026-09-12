package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

import java.util.UUID;

public class LightPortalEntity extends Entity implements IEntityWithComplexSpawn {
	private static final double REACH = 1D;
	public static final int OPENING = 20;

	private Vec3 destination = Vec3.ZERO;
	private ResourceKey<Level> destinationDim = Level.OVERWORLD;

	private float arrivalYaw;

	private UUID owner;

	private ResourceLocation closesOn;

	private static final int CLOSE_CHECK = 40;

	private boolean ownerInside;

	private UUID greeter;

	public void setGreeter(Entity entity) {
		this.greeter = entity.getUUID();
	}

	public LightPortalEntity(EntityType<? extends Entity> type, Level level) {
		super(type, level);
		this.blocksBuilding = false;
		this.noPhysics = true;
	}

	public LightPortalEntity(Level level, Vec3 where, Vec3 destination, ResourceKey<Level> destinationDim, float arrivalYaw, UUID owner) {
		this(ModEntities.TYPE_LIGHT_PORTAL.get(), level);
		setPos(where.x, where.y, where.z);
		this.destination = destination;
		this.destinationDim = destinationDim;
		this.arrivalYaw = arrivalYaw;
		this.owner = owner;
	}

	private boolean recordsOrigin;

	public void recordsOrigin() {
		this.recordsOrigin = true;
	}

	/** Makes it stay until the owner has earned this and come back through. */
	public void staysUntil(ResourceLocation flag) {
		this.closesOn = flag;
	}

	public UUID getOwner() {
		return owner;
	}

	public boolean isOwnedBy(Entity entity) {
		return owner != null && owner.equals(entity.getUUID());
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
	}

	@Override
	public void tick() {
		super.tick();

		if (level().isClientSide || tickCount < OPENING) {
			return;
		}

		ServerPlayer standing = null;

		for (Entity touching : level().getEntities(this, getBoundingBox().inflate(REACH))) {
			if (touching instanceof ServerPlayer player && isOwnedBy(player)) {
				standing = player;
				break;
			}
		}

		// Only the moment of stepping in counts. Arriving inside one does not: the way home comes out
		// exactly where its owner last crossed the way out, which on the first visit is the middle of
		// the way out, still standing there. Reading the overlap as an entry sent them straight back
		if (standing != null && !ownerInside && !standing.isOnPortalCooldown()) {
			ownerInside = true;
			cross(standing);
			return;
		}

		ownerInside = standing != null;

		if (closesOn != null && tickCount % CLOSE_CHECK == 0) {
			closeIfDone();
		}
	}

	private void closeIfDone() {
		if (owner == null || !(level() instanceof ServerLevel server)) {
			return;
		}

		if (!(server.getEntity(owner) instanceof ServerPlayer player)) {
			return;
		}

		PlayerData data = PlayerData.get(player);

		if (data != null && data.hasFlag(closesOn)) {
			discard();
		}
	}

	private void cross(ServerPlayer player) {
		ServerLevel destinationLevel = level().getServer().getLevel(destinationDim);

		if (destinationLevel == null) {
			return;
		}

		PlayerData data = PlayerData.get(player);

		if (data != null && recordsOrigin) {
			data.setReturnDimension(player);
			data.setReturnLocation(player);
			PacketHandler.sendTo(new SCSyncPlayerData(player), player);
		}

		if (player.level().dimension().equals(destinationDim)) {
			player.teleportTo(destinationLevel, destination.x, destination.y, destination.z, arrivalYaw, 0F);
		} else {
			player.changeDimension(new DimensionTransition(destinationLevel, destination, Vec3.ZERO, arrivalYaw, 0F, entity -> {}));
		}

		// The same grace vanilla gives its own doorways, so whatever is standing at the far end lets
		// them get their bearings before it considers taking them anywhere
		player.setPortalCooldown();

		dismissGreeter();

		// A door with nothing to wait for is spent; one that is waiting stays for the trip back
		if (closesOn == null) {
			discard();
		}
	}

	/** His errand ended when the pupil stepped through. */
	private void dismissGreeter() {
		if (greeter != null && level() instanceof ServerLevel server && server.getEntity(greeter) != null) {
			server.getEntity(greeter).discard();
			greeter = null;
		}
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		return distance < 4096D;
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putDouble("destination_x", destination.x);
		tag.putDouble("destination_y", destination.y);
		tag.putDouble("destination_z", destination.z);
		tag.putFloat("arrival_yaw", arrivalYaw);
		tag.putString("dimension", destinationDim.location().toString());
		tag.putBoolean("records_origin", recordsOrigin);

		if (owner != null) {
			tag.putUUID("owner", owner);
		}

		if (greeter != null) {
			tag.putUUID("greeter", greeter);
		}

		if (closesOn != null) {
			tag.putString("closes_on", closesOn.toString());
		}
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		destination = new Vec3(tag.getDouble("destination_x"), tag.getDouble("destination_y"), tag.getDouble("destination_z"));
		arrivalYaw = tag.getFloat("arrival_yaw");

		ResourceLocation dimension = ResourceLocation.tryParse(tag.getString("dimension"));
		destinationDim = dimension == null ? Level.OVERWORLD : ResourceKey.create(Registries.DIMENSION, dimension);

		recordsOrigin = tag.getBoolean("records_origin");
		owner = tag.hasUUID("owner") ? tag.getUUID("owner") : null;
		greeter = tag.hasUUID("greeter") ? tag.getUUID("greeter") : null;
		closesOn = tag.contains("closes_on") ? ResourceLocation.tryParse(tag.getString("closes_on")) : null;
	}

	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf buffer) {
	}
}
