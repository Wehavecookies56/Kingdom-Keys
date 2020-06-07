package online.kingdomkeys.kingdomkeys.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.WorldSavedData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class WorldSavedDataKingdomKeys extends WorldSavedData {

    private static final String DATA_NAME = KingdomKeys.MODID + "_WorldData";

    public boolean generated, spawnHeartless;

    public WorldSavedDataKingdomKeys() {
        super(DATA_NAME);
    }

    public boolean isGenerated() {
        return generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
        markDirty();
    }

    public boolean isSpawnHeartless() {
        return spawnHeartless;
    }

    public void setSpawnHeartless(boolean spawnHeartless) {
        this.spawnHeartless = spawnHeartless;
        markDirty();
    }

    public WorldSavedDataKingdomKeys(String name) {
        super(name);
    }

    @Override
	public void read(CompoundNBT nbt) {
        generated = nbt.getBoolean("generated");
        spawnHeartless = nbt.getBoolean("spawnheartless");
    }

    @Override
	public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("generated", generated);
        compound.putBoolean("spawnheartless", spawnHeartless);
        return compound;
    }

    /*public static WorldSavedDataKingdomKeys get(ServerWorld world) {
        DimensionSavedDataManager storage = world.getSavedData();
        WorldSavedDataKingdomKeys instance = (WorldSavedDataKingdomKeys) storage.get(WorldSavedDataKingdomKeys.class, DATA_NAME);

        if (instance == null) {
            instance = new WorldSavedDataKingdomKeys();
            storage.setData(DATA_NAME, instance);
        }
        return instance;
    }*/
    
}
