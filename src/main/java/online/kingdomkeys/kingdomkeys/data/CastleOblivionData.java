package online.kingdomkeys.kingdomkeys.data;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCastleOblivionInteriorData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomData;

import java.util.*;

public class CastleOblivionData {


    private CastleOblivionData() {}

    public static class InteriorData extends SavedData {

        List<Floor> floors = new ArrayList<>();
        //Value to check whether data needs to be updated.
        int dataVersion = 1;

        public static final int STORE_STRUCTURE_DIMS = 1;

        private static InteriorData create() {
            return new InteriorData();
        }

        public static Optional<InteriorData> get(ServerLevel level) {
            if (level.dimension().location().toString().contains("kingdomkeys:castle_oblivion_")) {
                return Optional.of(level.getDataStorage().computeIfAbsent(new Factory<>(InteriorData::create, InteriorData::load), "kingdomkeys_interior_data"));
            }
            return Optional.empty();
        }

        public static Optional<InteriorData> getClient(ClientLevel level) {
            if (clientCache.containsKey(level.dimension())) {
                return Optional.of(clientCache.get(level.dimension()));
            }
            return Optional.empty();
        }

        public static void setClientCache(ClientLevel level, InteriorData data) {
            clientCache.put(level.dimension(), data);
        }

        private static Map<ResourceKey<Level>, InteriorData> clientCache = new HashMap<>();

        public static void clearClientCache() {
            clientCache = new HashMap<>();
        }

        public int getDataVersion() {
            return dataVersion;
        }

        public boolean needsUpdate(int version) {
            return dataVersion < version;
        }

        public void appliedUpdate(int version) {
            this.dataVersion = version;
            setDirty();
        }

        public void sendToClient(Player player) {
            PacketHandler.sendTo(new SCSyncCastleOblivionInteriorData(this, player.level()), (ServerPlayer) player);
        }

        @Override
        public CompoundTag save(CompoundTag pTag, HolderLookup.Provider pRegistries) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("data_version", dataVersion);
            tag.putInt("floors_size", floors.size());
            for(int i = 0; i < floors.size(); i++) {
                tag.put("floors_" + i, floors.get(i).serializeNBT());
            }
            return tag;
        }

        public static InteriorData load(CompoundTag tag, HolderLookup.Provider provider) {
            InteriorData data = InteriorData.create();
            if (tag.contains("data_version")) {
                data.dataVersion = tag.getInt("data_version");
            } else {
                data.dataVersion = 0;
            }
            if (data.floors == null) {
                data.floors = new ArrayList<>();
            }
            data.floors.clear();
            int size = tag.getInt("floors_size");
            for (int i = 0; i < size; i++) {
                data.floors.add(new Floor((CompoundTag) tag.get("floors_" + i)));
            }
            return data;
        }

        public List<Floor> getFloors() {
            return floors;
        }

        public void addFloor(Floor floor) {
            this.floors.add(floor);
            setDirty();
        }

        public Room getRoomAtPos(BlockPos pos) {
            Floor floor = getFloorAtPos(pos);
            for (RoomData room : floor.getRooms()) {
                if (room.getGenerated().isPresent()) {
                    Room r = room.getGenerated().get();
                    if (r.inRoom(pos)) {
                        return r;
                    }
                }
            }
            return null;
        }

        //get floor from the closest lobby, not a perfect method but as long as the floors are far enough apart it won't be an issue (foreshadowing, maybe)
        public Floor getFloorAtPos(BlockPos pos) {
            if (floors.getFirst().getEntranceHall().getGenerated().isPresent()) {
                Room closestEntrance = floors.getFirst().getEntranceHall().getGenerated().get();
                for (Floor floor : getFloors()) {
                    if (floor.getEntranceHallPosition().getZ() < pos.getZ()) {
                        closestEntrance = floor.getEntranceHall().getGenerated().get();
                    }
                }
                return closestEntrance.getParent(this);
            }
            //if there is no room in the first floor nothing has generated yet
            return null;
        }

        public Floor getFloorByID(int id) {
            List<Floor> f = getFloors().stream().filter(floor -> floor.getFloorID() == id).toList();
            return !f.isEmpty() ? f.getFirst() : null;
        }

        public RoomData getRoomByData(RoomData data) {
            return getFloorByID(data.getParentID()).getRoom(data.pos);
        }

        public boolean isInRoom(BlockPos pos) {
            return false;
        }
    }

    public static class ExteriorData extends SavedData {
        //Storing dimension names by player uuid
        Map<UUID, ResourceLocation> interiors = new HashMap<>();

        private static ExteriorData create() {
            return new ExteriorData();
        }

        public static ExteriorData get(MinecraftServer server) {
            return server.overworld().getDataStorage().computeIfAbsent(new Factory<>(ExteriorData::create, ExteriorData::load), "kingdomkeys_exterior_data");
        }

        public static ExteriorData getClient() {
            return clientCache;
        }

        public static void setClientCache(ExteriorData data) {
            clientCache = data;
        }

        private static ExteriorData clientCache = new ExteriorData();

        @Override
        public CompoundTag save(CompoundTag pTag, HolderLookup.Provider pRegistries) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("interiors_size", interiors.size());
            List<Map.Entry<UUID, ResourceLocation>> entries = interiors.entrySet().stream().toList();
            for (int i = 0; i < interiors.size(); i++) {
                tag.putUUID("interior_uuid_" + i, entries.get(i).getKey());
                tag.putString("interior_dimensionrl_" + i, entries.get(i).getValue().toString());
            }
            return tag;
        }

        public static ExteriorData load(CompoundTag tag, HolderLookup.Provider provider) {
            ExteriorData data = ExteriorData.create();
            if (data.interiors == null) {
                data.interiors = new HashMap<>();
            }
            int size = tag.getInt("interiors_size");
            data.interiors.clear();
            for (int i = 0; i < size; i++) {
                data.interiors.put(tag.getUUID("interior_uuid_" + i), ResourceLocation.parse(tag.getString("interior_dimensionrl_" + i)));
            }
            return data;
        }

        public Map<UUID, ResourceLocation> getInteriors() {
            return interiors;
        }

        public void addInterior(UUID uuid, ResourceLocation dimension) {
            interiors.put(uuid, dimension);
            setDirty();
        }

        public ResourceLocation getInterior(UUID id) {
            return interiors.get(id);
        }
    }

}
