package online.kingdomkeys.kingdomkeys.world;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class SavePointStorage extends SavedData {

    public enum SavePointType implements StringRepresentable {
        NORMAL, LINKED, WARP;

        @Override
        public String getSerializedName() {
            return name().toLowerCase();
        }
    }

    public record SavePoint(UUID id, SavePointType type, String name, BlockPos pos, Pair<UUID, String> owner, ResourceKey<Level> dimension, boolean global, Instant timeCreated) {
        public SavePoint(CompoundTag nbt) {
            this(
                    nbt.getUUID("ID"),
                    SavePointType.values()[nbt.getInt("TYPE")],
                    nbt.getString("NAME"),
                    new BlockPos(nbt.getInt("POSX"), nbt.getInt("POSY"), nbt.getInt("POSZ")),
                    Pair.of(nbt.getUUID("OWNER_UUID"), nbt.getString("OWNER_NAME")),
                    ResourceKey.create(Registries.DIMENSION, new ResourceLocation(nbt.getString("DIM"))),
                    nbt.getBoolean("GLOBAL"),
                    getCreatedTime(nbt)
            );
        }

        private static Instant getCreatedTime(CompoundTag nbt) {
            if (nbt.contains("TIME_EPOCH") && nbt.contains("TIME_NANO")) {
                return Instant.ofEpochSecond(nbt.getLong("TIME_EPOCH"), nbt.getInt("TIME_NANO"));
            } else {
                return Instant.now();
            }
        }

        public CompoundTag serializeNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.putUUID("ID", id);
            nbt.putInt("TYPE", type.ordinal());
            nbt.putString("NAME", name);
            nbt.putInt("POSX", pos.getX());
            nbt.putInt("POSY", pos.getY());
            nbt.putInt("POSZ", pos.getZ());
            nbt.putUUID("OWNER_UUID", owner.getFirst());
            nbt.putString("OWNER_NAME", owner.getSecond());
            nbt.putString("DIM", dimension.location().toString());
            nbt.putBoolean("GLOBAL", global);
            nbt.putLong("TIME_EPOCH", timeCreated.getEpochSecond());
            nbt.putInt("TIME_NANO", timeCreated.getNano());
            return nbt;
        }
    }

    private final Map<UUID, SavePoint> savePointRegistry;

    public Map<UUID, SavePoint> getAllSavePoints() {
        return savePointRegistry;
    }

    public Map<UUID, SavePoint> getGlobalSavePoints() {
        return savePointRegistry.entrySet().stream().filter(uuidSavePointEntry -> uuidSavePointEntry.getValue().global()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<UUID, Pair<SavePoint, Instant>> getDiscoveredSavePoints(Player player) {
        Map<UUID, Pair<SavePoint, Instant>> filteredRegistry = new HashMap<>();
        //Filter out any save points that no longer exist and any global save points
        Map<UUID, Instant> uuids = ModCapabilities.getPlayer(player).discoveredSavePoints().entrySet().stream().filter(uuidInstantEntry -> savePointRegistry.containsKey(uuidInstantEntry.getKey()) && !savePointRegistry.get(uuidInstantEntry.getKey()).global).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        ModCapabilities.getPlayer(player).setDiscoveredSavePoints(uuids);
        List<SavePoint> savePointList = savePointRegistry.entrySet().stream().filter(uuidSavePointEntry -> uuids.containsKey(uuidSavePointEntry.getKey())).map(Map.Entry::getValue).toList();
        savePointList.forEach(savePoint -> filteredRegistry.put(savePoint.id, Pair.of(savePoint, uuids.get(savePoint.id))));
        getGlobalSavePoints().forEach((uuid, savePoint) -> filteredRegistry.put(uuid, Pair.of(savePoint, savePoint.timeCreated)));
        return filteredRegistry;
    }

    public void addSavePoint(SavePoint savePoint) {
        if (!savePointRegistry.containsKey(savePoint.id)) {
            savePointRegistry.put(savePoint.id, savePoint);
            setDirty();
        } else {
            KingdomKeys.LOGGER.error("Attempted to add duplicate save point with ID:{}", savePoint.id);
        }
    }

    public void removeSavePoint(UUID id) {
        if (savePointRegistered(id)) {
            savePointRegistry.remove(id);
            setDirty();
        }
    }

    public SavePointStorage() {
        savePointRegistry = new HashMap<>();
    }

    public SavePoint getSavePoint(UUID id) {
        return savePointRegistry.get(id);
    }

    public boolean savePointRegistered(UUID id) {
        return savePointRegistry.containsKey(id);
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        for (SavePoint savePoint : savePointRegistry.values()) {
            nbt.put(savePoint.id.toString(), savePoint.serializeNBT());
        }
        return nbt;
    }

    private static SavePointStorage load(CompoundTag nbt) {
        SavePointStorage data = SavePointStorage.create();
        for (String key : nbt.getAllKeys()) {
            data.addSavePoint(new SavePoint(nbt.getCompound(key)));
        }
        return data;
    }

    private static SavePointStorage create() {
        return new SavePointStorage();
    }

    public static SavePointStorage getStorage(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(SavePointStorage::load, SavePointStorage::create, KingdomKeys.MODID + "_savepoints");
    }
}
