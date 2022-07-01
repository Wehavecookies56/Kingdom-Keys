package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.Size2i;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.Room;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomData;

import java.util.*;

public class CastleOblivionCapabilities {

    public interface ICastleOblivionInteriorCapability extends INBTSerializable<CompoundTag> {
        List<Floor> getFloors();
        void addFloor(Floor floor);
        Room getRoomAtPos(BlockPos pos);
        Floor getFloorByID(UUID id);
        boolean isInRoom(BlockPos pos);
    }

    public static class InteriorImplementation implements ICastleOblivionInteriorCapability {

        List<Floor> floors = new ArrayList<>();

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("floors_size", floors.size());
            for(int i = 0; i < floors.size(); i++) {
                tag.put("floors_" + i, floors.get(i).serializeNBT());
            }
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            if (floors == null) {
                floors = new ArrayList<>();
            }
            floors.clear();
            int size = tag.getInt("floors_size");
            for (int i = 0; i < size; i++) {
                floors.add(Floor.deserialize((CompoundTag) tag.get("floors_" + i)));
            }
        }


        @Override
        public List<Floor> getFloors() {
            return floors;
        }

        @Override
        public void addFloor(Floor floor) {
            this.floors.add(floor);
        }

        @Override
        public Room getRoomAtPos(BlockPos pos) {
            //TODO get floor properly
            Floor floor = getFloors().get(0);
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

        @Override
        public Floor getFloorByID(UUID id) {
            List<Floor> f = getFloors().stream().filter(floor -> floor.getFloorID() == id).toList();
            return f.size() > 0 ? f.get(0) : null;
        }

        @Override
        public boolean isInRoom(BlockPos pos) {
            return false;
        }
    }

    public static class InteriorProvider implements ICapabilitySerializable<CompoundTag> {

        private final ICastleOblivionInteriorCapability instance = new InteriorImplementation();

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return ModCapabilities.CASTLE_OBLIVION_INTERIOR_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> instance));
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.deserializeNBT(nbt);
        }
    }

    public interface ICastleOblivionExteriorCapability extends INBTSerializable<CompoundTag> {
        Map<UUID, ResourceLocation> getInteriors();
        void addInterior(UUID id, ResourceLocation dimension);
        ResourceLocation getInterior(UUID id);
    }

    public static class ExteriorImplementation implements ICastleOblivionExteriorCapability {
        //Storing dimension names by player uuid
        Map<UUID, ResourceLocation> interiors = new HashMap<>();

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("interiors_size", interiors.size());
            List<Map.Entry<UUID, ResourceLocation>> entries = interiors.entrySet().stream().toList();
            for (int i = 0; i < interiors.size(); i++) {
                tag.putUUID("interior_uuid_" + i, entries.get(i).getKey());
                tag.putString("interior_dimensionrl_" + i, entries.get(i).getValue().toString());
            }
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundTag tag) {
            if (interiors == null) {
                interiors = new HashMap<>();
            }
            int size = tag.getInt("interiors_size");
            interiors.clear();
            for (int i = 0; i < size; i++) {
                interiors.put(tag.getUUID("interior_uuid_" + i), new ResourceLocation(tag.getString("interior_dimensionrl_" + i)));
            }
        }

        @Override
        public Map<UUID, ResourceLocation> getInteriors() {
            return interiors;
        }

        @Override
        public void addInterior(UUID uuid, ResourceLocation dimension) {
            interiors.put(uuid, dimension);
        }

        @Override
        public ResourceLocation getInterior(UUID id) {
            return interiors.get(id);
        }
    }

    public static class ExteriorProvider implements ICapabilitySerializable<CompoundTag> {

        private final ICastleOblivionExteriorCapability instance = new ExteriorImplementation();

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return ModCapabilities.CASTLE_OBLIVION_EXTERIOR_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> instance));
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.deserializeNBT(nbt);
        }
    }

}
