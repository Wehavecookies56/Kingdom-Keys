package online.kingdomkeys.kingdomkeys.encounter;

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
    private Encounter.State state;
    private long activeTicks;
    private boolean isComplete;

    public EncounterInstance(RoomEncounter encounter, Encounter.State state) {
        this.encounter = encounter;
        this.state = state;
    }

    public RoomEncounter getEncounter() {
        return encounter;
    }

    public Encounter.State getState() {
        return state;
    }

    public <T extends Encounter.State> T getState(Class<T> clazz) {
        return clazz.cast(state);
    }

    public void setComplete() {
        this.isComplete = true;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setState(Encounter.State state) {
        this.state = state;
    }

    public long getActiveTicks() {
        return activeTicks;
    }

    public void start(EncounterContext context, ServerLevel level) {
        if (!isComplete) {
            context.getParticipants(level).forEach(player -> {
                CastleOblivionData.InteriorData.get(level).ifPresent(interiorData -> {
                    interiorData.setDirty();
                    interiorData.sendToClient(player);
                    PacketHandler.sendTo(new SCSyncCastleOblivionInteriorData(interiorData, level), (ServerPlayer) player);
                });
            });
            context.getRoom().ifPresent(room -> room.setDoorLocks(level, true));
            encounter.getHandler().start(encounter.getEncounter(), state, this, context, level);
        }
    }

    public void tick(EncounterContext context, ServerLevel level) {
        if (!isComplete) {
            encounter.getHandler().tick(encounter.getEncounter(), state, this, context, level);
            activeTicks++;
        } else {
            end(context, level);
        }
    }

    public void end(EncounterContext context, ServerLevel level) {
        context.getRoom().ifPresent(room -> room.setDoorLocks(level, false));
        encounter.getHandler().end(encounter.getEncounter(), state, this, context, level);
        List<Player> players = context.getParticipants(level);
        if (!encounter.getRewards().isEmpty()) {
            if (!context.getTreasurePoints().isEmpty()) {
                spawnRewardsChest(level, context.getTreasurePoints().getFirst(), getEncounter().getRewards());
            } else if (!context.getSpawnPoints().isEmpty()) {
                //fallback to spawn points
                spawnRewardsChest(level, new Room.TreasurePoint(context.getSpawnPoints().getFirst(), ModBlocks.treasureChest.get().defaultBlockState().setValue(TreasureChestBlock.FACING, Util.getRandom(Direction.Plane.HORIZONTAL.stream().toList(), RandomSource.create()))), getEncounter().getRewards());
            } else if (!players.isEmpty()) {
                //give reward to one player as fallback if room has no spawn point for a chest
                Utils.giveItems((ServerPlayer) players.getFirst(), true, getEncounter().getRewards());
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
