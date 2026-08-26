package online.kingdomkeys.kingdomkeys.entity.worldmap;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.world.worldmap.GummiWorld;
import online.kingdomkeys.kingdomkeys.world.worldmap.GummiWorldLoader;

// A world as seen from out in the worldmap. It holds nothing but the id of its data entry - the
// texture, the size and where you land are all read back out of that, so editing the json is enough
// to change a marker without touching the entity that is already in the world.
public class WorldMarkerEntity extends Entity {

	private static final EntityDataAccessor<String> WORLD = SynchedEntityData.defineId(WorldMarkerEntity.class, EntityDataSerializers.STRING);

	public WorldMarkerEntity(EntityType<?> type, Level level) {
		super(type, level);
		this.noPhysics = true;
		this.noCulling = true;
	}

	public String getWorldId() {
		return entityData.get(WORLD);
	}

	public void setWorldId(String id) {
		entityData.set(WORLD, id);
	}

	public GummiWorld getWorld() {
		return GummiWorldLoader.get(getWorldId());
	}

	@Override
	public void tick() {
		super.tick();

		// Its entry was removed from the datapack: the marker has nothing left to point at.
		if (!level().isClientSide && getWorld() == null) {
			discard();
		}
	}

	@Override
	protected double getDefaultGravity() {
		return 0;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		builder.define(WORLD, "");
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compound) {
		setWorldId(compound.getString("World"));
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compound) {
		compound.putString("World", getWorldId());
	}

	@Override
	public boolean isPickable() {
		return false;
	}
}
