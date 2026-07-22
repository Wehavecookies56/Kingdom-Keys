package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter;

import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.block.TreasureChestBlock;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.entity.block.TreasureChestTileEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowMessagesPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCastleOblivionInteriorData;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomEncounters;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;

import java.util.List;

public class EncounterInstance {
    private final RoomEncounter encounter;
    private EncounterState state;
    private long activeTicks;
    private boolean isComplete;

    public EncounterInstance(RoomEncounter encounter, EncounterState state) {
        this.encounter = encounter;
        this.state = state;
    }

    public RoomEncounter getEncounter() {
        return encounter;
    }

    public EncounterState getState() {
        return state;
    }

    public <T extends EncounterState> T getState(Class<T> clazz) {
        return clazz.cast(state);
    }

    public void setComplete() {
        this.isComplete = true;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setState(EncounterState state) {
        this.state = state;
    }

    public long getActiveTicks() {
        return activeTicks;
    }

    public void start(Room room, ServerLevel level) {
        if (!isComplete) {
            Room.getPlayersInRoom(level.getServer(), room).forEach(player -> {
                CastleOblivionData.InteriorData.get(level).ifPresent(interiorData -> {
                    interiorData.setDirty();
                    interiorData.sendToClient(player);
                });
                PacketHandler.sendTo(new SCSyncCastleOblivionInteriorData(CastleOblivionData.InteriorData.get(level).get(), level), (ServerPlayer) player);
            });
            room.setDoorLocks(level, true);
            encounter.getHandler().start(encounter.getEncounter(), getEncounter().getHandler().createState(), this, room, level);
        }
    }

    public void tick(Room room, ServerLevel level) {
        if (!isComplete) {
            encounter.getHandler().tick(encounter.getEncounter(), state, this, room, level);
            activeTicks++;
        } else {
            end(room, level);
        }
    }

    public void end(Room room, ServerLevel level) {
        room.setDoorLocks(level, false);
        encounter.getHandler().end(encounter.getEncounter(), state, this, room, level);
        List<Player> players = Room.getPlayersInRoom(level.getServer(),  room);
        if (!encounter.getRewards().isEmpty()) {
            if (!room.getTreasurePoints().isEmpty()) {
                spawnRewardsChest(level, room.getTreasurePoints().getFirst(), getEncounter().getRewards());
            } else if (!room.getSpawnPoints().isEmpty()) {
                //fallback to spawn points
                spawnRewardsChest(level, new Room.TreasurePoint(room.getSpawnPoints().getFirst(), ModBlocks.treasureChest.get().defaultBlockState().setValue(TreasureChestBlock.FACING, Util.getRandom(Direction.Plane.HORIZONTAL.stream().toList(), RandomSource.create()))), getEncounter().getRewards());
            } else if (!players.isEmpty()) {
                //give reward to one player as fallback if room has no spawn point for a chest
                Utils.giveItems((ServerPlayer) players.getFirst(), getEncounter().getRewards());
            }
        }

        players.forEach(player -> {
            List<Utils.Title> message = List.of(
                    new Utils.Title("co.encounter.end", "")
            );
            PacketHandler.sendTo(new SCShowMessagesPacket(message), (ServerPlayer) player);
            CastleOblivionData.InteriorData.get(level).ifPresent(interiorData -> {
                interiorData.setDirty();
                interiorData.sendToClient(player);
            });
        });
    }

    public void spawnRewardsChest(ServerLevel level, Room.TreasurePoint treasurePoint, List<ItemStack> treasure) {
        if (!treasure.isEmpty() && level.getBlockState(treasurePoint.pos()).is(Blocks.AIR)) {

            BlockState chest = treasurePoint.state();
            TreasureChestTileEntity treasureChestTileEntity = new TreasureChestTileEntity(treasurePoint.pos(), chest);
            treasureChestTileEntity.setTreasure(treasure);
            level.setBlock(treasurePoint.pos(), chest, Block.UPDATE_ALL);
            level.setBlockEntity(treasureChestTileEntity);
        }
    }

    public CompoundTag serializeNBT(){
        CompoundTag tag = new CompoundTag();
        tag.putString("room_encounter", encounter.getRegistryName().toString());
        EncounterType<?, ?> type = encounter.encounter.type();
        tag.put("state", type.encodeStart(NbtOps.INSTANCE, state).getOrThrow());
        tag.putLong("active_ticks", activeTicks);
        tag.putBoolean("complete", isComplete);
        return tag;
    }

    public EncounterInstance(CompoundTag tag) {
        encounter = ModRoomEncounters.registry.get().getValue(KingdomKeys.rl(tag.getString("room_encounter")));
        state = encounter.encounter.type().decodeStart(NbtOps.INSTANCE, tag.getCompound("state")).getOrThrow();
        activeTicks = tag.getLong("active_ticks");
        isComplete = tag.getBoolean("complete");
    }
}
