package online.kingdomkeys.kingdomkeys.data;

import java.util.*;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.Room;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomData;

public class CastleOblivionData {


    private CastleOblivionData() {}

    public static class InteriorData extends SavedData {

        List<Floor> floors = new ArrayList<>();

        private static InteriorData create() {
            return new InteriorData();
        }

        public static InteriorData get(ServerLevel level) {
            if (level.dimension().location().toString().contains("kingdomkeys:castle_oblivion_")) {
                return level.getDataStorage().computeIfAbsent(new Factory<>(InteriorData::create, InteriorData::load), "kingdomkeys_interior_data");
            }
            return null;
        }

        public static InteriorData getClient(ClientLevel level) {
            if (clientCache.containsKey(level.dimension())) {
                return clientCache.get(level.dimension());
            }
            return null;
        }

        public static void setClientCache(ClientLevel level, InteriorData data) {
            clientCache.put(level.dimension(), data);
        }

        private static Map<ResourceKey<Level>, InteriorData> clientCache = new HashMap<>();

        @Override
        public CompoundTag save(CompoundTag pTag, HolderLookup.Provider pRegistries) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("floors_size", floors.size());
            for(int i = 0; i < floors.size(); i++) {
                tag.put("floors_" + i, floors.get(i).serializeNBT());
            }
            return tag;
        }

        private static InteriorData load(CompoundTag tag, HolderLookup.Provider provider) {
            InteriorData data = InteriorData.create();
            if (data.floors == null) {
                data.floors = new ArrayList<>();
            }
            data.floors.clear();
            int size = tag.getInt("floors_size");
            for (int i = 0; i < size; i++) {
                data.floors.add(Floor.deserialize((CompoundTag) tag.get("floors_" + i)));
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

        public Room getRoomAtPos(Level level, BlockPos pos) {
            Floor floor = getFloorAtPos(level, pos);
            for (RoomData room : floor.getRooms()) {
                Room r = room.getGenerated();
                if (r != null) {
                    if (r.inRoom(pos)) {
                        return r;
                    }
                }
            }
            return null;
        }

        //get floor from the closest lobby, not a perfect method but as long as the floors are far enough apart it won't be an issue (foreshadowing, maybe)
        public Floor getFloorAtPos(Level level, BlockPos pos) {
            Room closestLobby = floors.get(0).getLobbyRoom();
            if (closestLobby != null) {
                double closestDistance = closestLobby.position.distSqr(pos);
                for (Floor floor : getFloors()) {
                    if (floor.getLobbyPosition().distSqr(pos) < closestDistance) {
                        closestLobby = floor.getLobbyRoom();
                        closestDistance = floor.getLobbyPosition().distSqr(pos);
                    }
                }
                return closestLobby.getParent(level);
            }
            //if there is no room in the first floor nothing has generated yet
            return null;
        }

        public Floor getFloorByID(UUID id) {
            List<Floor> f = getFloors().stream().filter(floor -> floor.getFloorID().equals(id)).toList();
            return f.size() > 0 ? f.get(0) : null;
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

        private static ExteriorData load(CompoundTag tag, HolderLookup.Provider provider) {
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
