package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.CardDoorBlock;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.capability.CastleOblivionCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.card.WorldCardItem;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCastleOblivionInteriorCapability;
import online.kingdomkeys.kingdomkeys.network.stc.SCUpdateCORooms;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.DynamicDimensionManager;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.*;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomStructures;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModRoomTypes;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomData;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.Room;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomGenerator;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomPos;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomDirection;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

import java.util.List;

public class CastleOblivionHandler {

    //Ticking rooms that players are in, empty rooms should be inactive
    @SubscribeEvent
    public void tick(TickEvent.LevelTickEvent event) {
        if (isInterior(event.level.dimension())) {
            CastleOblivionCapabilities.ICastleOblivionInteriorCapability cap = ModCapabilities.getCastleOblivionInterior(event.level);
            if (cap != null) {
                cap.getFloors().forEach(floor -> {
                    if (floor.shouldTick()) {
                        floor.getPlayers().values().forEach(room -> room.getRoomData(event.level).getGenerated().tick());
                    }
                });
            }
        }
    }

    //Prevent card door from breaking in interior (there are probably ways around this
    @SubscribeEvent
    public void breakBlock(BlockEvent.BreakEvent event) {
        if (inInterior(event.getPlayer())) {
            if (event.getState().getBlock() == ModBlocks.cardDoor.get()) {
                if (event.getState().getValue(CardDoorBlock.GENERATED)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    public static final ResourceKey<Level> CASTLE_OBLIVION = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(KingdomKeys.MODID, "castle_oblivion"));

    public static final Utils.BlockPosBounds entranceBounds = new Utils.BlockPosBounds(-10, 87, 11, -1, 100, 11);
    public static final Utils.BlockPosBounds firstDoorBounds = new Utils.BlockPosBounds(15, 63, 67, 17, 66, 67);

    public static final BlockPos entrancePos = new BlockPos(16, 62, 2);
    public static final BlockPos exitPos = new BlockPos(-5, 90, 6);


    //Creates the interior dimension and teleports the player to it
    public static void enterCastleOblivion(Player player) {
        if (player.level().getServer() != null) {
            ResourceLocation dimName = new ResourceLocation(KingdomKeys.MODID, "castle_oblivion_interior_" + player.getStringUUID());
            ModCapabilities.getCastleOblivionExterior(player.level()).addInterior(player.getUUID(), dimName);
            RegistryAccess registryAccess = player.level().registryAccess();
            ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, dimName);
            Holder<DimensionType> type = registryAccess.registryOrThrow(Registries.DIMENSION_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(KingdomKeys.MODID, "castle_oblivion")));
            Holder<Biome> biome = registryAccess.registryOrThrow(Registries.BIOME).getHolderOrThrow(ResourceKey.create(Registries.BIOME, new ResourceLocation(KingdomKeys.MODID, Strings.castleOblivionInterior)));
            //Create new dimension if it doesn't exist
            ServerLevel level = DynamicDimensionManager.getOrCreateLevel(player.level().getServer(), dimension, ((minecraftServer, levelStemResourceKey) -> {
                ChunkGenerator generator = new CastleOblivionInteriorChunkGenerator(new FixedBiomeSource(biome));
                return new LevelStem(type, generator);
            }));
            player.changeDimension(level, new BaseTeleporter(entrancePos));
        }
    }

    //teleports the player outside the front of Castle Oblivion
    public static void exitCastleOblivion(Floor currentFloor, Room currentRoom, Player player) {
        if (player.level().getServer() != null) {
            player.changeDimension(player.level().getServer().getLevel(CASTLE_OBLIVION), new BaseTeleporter(exitPos));
            MinecraftForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeRoomEvent(currentRoom, null, player));
            MinecraftForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeFloorEvent(currentFloor, null, player));
        }
    }

    public static Room createFirstRoom(Player player, CardDoorTileEntity te) {
        CastleOblivionCapabilities.ICastleOblivionInteriorCapability cap = ModCapabilities.getCastleOblivionInterior(player.level());
        if (cap != null) {
            Floor floor = cap.getFloorByID(te.getParentRoom().getParentID());
            //check the room is actually the entrance hall
            if (te.getParentRoom().equals(floor.getEntranceHall())) {
                //if size is 1 only the entrance hall room exists
                if (floor.getGeneratedRooms().size() == 1) {
                    te.setDestinationRoom(floor.getRoom(new RoomPos(0, 1)));
                    for (Player playerFromList : player.level().players()) {
                        PacketHandler.sendTo(new SCUpdateCORooms(floor.getRooms()), (ServerPlayer) playerFromList);
                    }
                    //TODO possibly define room type by the floor type
                    return RoomGenerator.INSTANCE.generateRoom(player.level(), floor.getRoom(new RoomPos(0, 1)), ModRoomTypes.SLEEPING_DARKNESS.get(), te.getParentRoom().getGenerated(), RoomDirection.NORTH);
                } else {
                    return floor.getRoom(new RoomPos(0, 1)).getGenerated();
                }
            }
        }
        return null;
    }

    public static void enterFirstRoom(Player player) {
        //todo world card stuff, instead should open world card gui then generate room based on world and teleport afterwards
        CastleOblivionCapabilities.ICastleOblivionInteriorCapability cap = ModCapabilities.getCastleOblivionInterior(player.level());
        if (cap != null) {
            Room currentRoom = cap.getRoomAtPos(player.level(), player.blockPosition());
            Floor floor = cap.getFloorByID(currentRoom.parentFloor);
            if (!currentRoom.getType().isEntranceHall()) {
                KingdomKeys.LOGGER.info("something is wrong player should be in the entrance hall room");
            } else {
                //if size is 1 only the entrance hall room exists
                if (floor.getGeneratedRooms().size() > 1) {
                    Room firstRoom = floor.getRoom(new RoomPos(0, 1)).getGenerated();
                    BlockPos newPos = firstRoom.doors.get(RoomDirection.NORTH).pos();
                    CardDoorTileEntity te = (CardDoorTileEntity) player.level().getBlockEntity(newPos);
                    if (te != null) { //null check in case door is destroyed
                        if (!MinecraftForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeRoomEvent(currentRoom, firstRoom, player))) {
                            newPos = newPos.offset(te.getDirection().toMCDirection().getNormal().multiply(2));
                            te.openDoor(true);
                            player.teleportTo(newPos.getX(), newPos.getY(), newPos.getZ());
                            PacketHandler.sendTo(new SCSyncCastleOblivionInteriorCapability(ModCapabilities.getCastleOblivionInterior(player.level())), (ServerPlayer) player);
                        }
                    }
                }
            }
        }
    }

    public boolean isExterior(ResourceKey<Level> level) {
        return level.equals(CASTLE_OBLIVION);
    }

    public boolean isInterior(ResourceKey<Level> level) {
        return level.location().toString().contains("castle_oblivion_interior_");
    }

    public boolean inExterior(Player player) {
        return isExterior(player.level().dimension());
    }

    public boolean inInterior(Player player) {
        return isInterior(player.level().dimension());
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.level().isClientSide) {
            if (inExterior(event.player)) {
                //Enter interior
                if (Utils.isPlayerWithin(event.player, entranceBounds)) {
                    enterCastleOblivion(event.player);
                }
            }
        }
    }

    @SubscribeEvent
    public void changeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        //if player is entering the interior
        if (isExterior(event.getFrom())) {
            if (isInterior(event.getTo())) {
                SCSyncCastleOblivionInteriorCapability.syncClients(event.getEntity().level());
                event.getEntity().sendSystemMessage(Component.translatable("I REPEAT, CASTLE OBLIVION IS WORK IN PROGRESS DON'T REPORT ANY ISSUES WITH IT YET PLEASE"));
                event.getEntity().sendSystemMessage(Component.translatable("IF YOUR GAME CRASHES HERE IT'S EXPECTED, THE OUTSIDE PART IS PROBABLY SAFE FROM CRASHES BUT NOT HERE DEFINITELY NOT HERE"));
                event.getEntity().sendSystemMessage(Component.translatable("THANK YOU AGAIN - Estelle"));
                ServerLevel level = event.getEntity().level().getServer().getLevel(event.getTo());
                Floor startFloor = Floor.getOrCreateFirstFloor(level);
                MinecraftForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeFloorEvent(null, startFloor, event.getEntity()));
                PacketHandler.sendTo(new SCUpdateCORooms(getCurrentFloor(event.getEntity()).getRooms()), (ServerPlayer) event.getEntity());
            }
        } else {
            PacketHandler.sendTo(new SCUpdateCORooms(List.of()), (ServerPlayer) event.getEntity());
        }
    }

    @SubscribeEvent
    public void joinWorld(PlayerEvent.PlayerLoggedInEvent event) {
        if (inInterior(event.getEntity())) {
            PacketHandler.sendTo(new SCSyncCastleOblivionInteriorCapability(ModCapabilities.getCastleOblivionInterior(event.getEntity().level())), (ServerPlayer) event.getEntity());
        }
    }

    public static Floor getCurrentFloor(Player player) {
        return ModCapabilities.getCastleOblivionInterior(player.level()).getFloorAtPos(player.level(), player.blockPosition());
    }

    @SubscribeEvent
    public void changedRoom(CastleOblivionEvent.PlayerChangeRoomEvent event) {
        if (event.getNewRoom() != null) {
            System.out.println("Entered Room: " + event.getNewRoom().getPosition());
        }

    }

    @SubscribeEvent
    public void generatedRoom(CastleOblivionEvent.RoomGeneratedEvent event) {
        if (event.getGeneratedRoomData() != null) {
            System.out.println("Generated a new room: " + event.getGeneratedRoomData().getGenerated());
        }
    }
}
