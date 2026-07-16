package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.CastleOblivionEvent;
import online.kingdomkeys.kingdomkeys.block.ModBlocks;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.entity.block.CardDoorTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.Constants;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCastleOblivionInteriorData;
import online.kingdomkeys.kingdomkeys.network.stc.SCUpdateCORooms;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.DynamicDimensionManager;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.*;

public class CastleOblivionHandler {

    //Ticking rooms that players are in, empty rooms should be inactive
    @SubscribeEvent
    public void tick(LevelTickEvent.Pre event) {
        if (!event.getLevel().isClientSide()) {
            if(event.getLevel().dimension().location().getNamespace().equals(KingdomKeys.MODID)) {//Attempt to alleviate load
                if (event.getLevel().dimension().toString().contains(KingdomKeys.MODID + ":castle_oblivion_interior_")) {
                    CastleOblivionData.InteriorData.get((ServerLevel) event.getLevel()).ifPresent(interiorData -> {
                        interiorData.getFloors().forEach(floor -> {
                            floor.getRooms().forEach(roomData -> {
                                roomData.getGenerated().ifPresent(room -> {
                                    floor.getType().getGlobalModifiers().forEach(roomModifier -> {
                                        roomModifier.tick(room, Room.getPlayersInRoom(event.getLevel().getServer(), room));
                                    });
                                    room.tick((ServerLevel) event.getLevel());
                                });
                            });
                        });
                    });
                }
            }
        }
    }

    //Prevent card door from breaking in interior (there are probably ways around this
    @SubscribeEvent
    public void breakBlock(BlockEvent.BreakEvent event) {
        if (event.getPlayer().level().dimension().location().toString().contains("castle_oblivion_interior_")) {
            if (event.getState().getBlock() == ModBlocks.cardDoor.get()) {
                event.setCanceled(true);
            }
        }
    }

    public static final ResourceKey<Level> CASTLE_OBLIVION = ResourceKey.create(Registries.DIMENSION, KingdomKeys.rl("castle_oblivion"));

    public static final Utils.BlockPosBounds entranceBounds = new Utils.BlockPosBounds(-10, 85, 11, -1, 100, 11);
    public static final Utils.BlockPosBounds firstDoorBounds = new Utils.BlockPosBounds(15, 63, 67, 17, 66, 67);

    public static final BlockPos entrancePos = new BlockPos(16, 62, 3);
    public static final BlockPos exitPos = new BlockPos(-5, 90, 6);

    //Creates the interior dimension and teleports the player to it
    public static void enterCastleOblivion(Player player) {
        if (player.level().getServer() != null) {
            ResourceLocation dimName = KingdomKeys.rl("castle_oblivion_interior_" + player.getStringUUID());
            CastleOblivionData.ExteriorData.get(player.getServer()).addInterior(player.getUUID(), dimName);
            RegistryAccess registryAccess = player.level().registryAccess();
            ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, dimName);
            Holder<DimensionType> type = registryAccess.registryOrThrow(Registries.DIMENSION_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DIMENSION_TYPE, KingdomKeys.rl("castle_oblivion")));
            Holder<Biome> biome = registryAccess.registryOrThrow(Registries.BIOME).getHolderOrThrow(ResourceKey.create(Registries.BIOME, KingdomKeys.rl(Strings.castleOblivionInterior)));
            //Create new dimension if it doesn't exist
            ServerLevel level = DynamicDimensionManager.getOrCreateLevel(player.level().getServer(), dimension, ((minecraftServer, levelStemResourceKey) -> {
                ChunkGenerator generator = new CastleOblivionInteriorChunkGenerator(new FixedBiomeSource(biome));
                return new LevelStem(type, generator);
            }));
            player.changeDimension(new DimensionTransition(level, new Vec3(entrancePos.getX(), entrancePos.getY(), entrancePos.getZ()), Vec3.ZERO, player.getYRot(), player.getXRot(), entity -> {}));

            if(player instanceof ServerPlayer sPlayer) {
                Utils.showTutorial(sPlayer, Constants.TUTORIALS.get(Constants.TUTORIAL_CO_LOBBY));
            }
        }
    }

    //teleports the player outside the front of Castle Oblivion
    public static void exitCastleOblivion(Floor currentFloor, Room currentRoom, Player player) {
        if (player.level().getServer() != null) {
            player.changeDimension(new DimensionTransition(player.level().getServer().getLevel(CASTLE_OBLIVION), new Vec3(exitPos.getX(), exitPos.getY(), exitPos.getZ()), Vec3.ZERO, player.getYRot(), player.getXRot(), entity -> {}));
            NeoForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeRoomEvent(currentRoom, null, player));
            NeoForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeFloorEvent(currentFloor, null, currentRoom, null, player));
        }
    }

    public static Room createFirstRoom(Player player, CardDoorTileEntity te) {
        if (CastleOblivionData.InteriorData.get((ServerLevel) player.level()).isPresent()) {
            CastleOblivionData.InteriorData interiorData = CastleOblivionData.InteriorData.get((ServerLevel) player.level()).get();
            Floor floor = interiorData.getFloorByID(te.getParentRoom().getParentID());
            //check the room is actually the entrance hall
            if (te.getParentRoom().equals(floor.getEntranceHall())) {
                //if size is 1 only the entrance hall room exists
                if (floor.getGeneratedRooms().size() == 1) {
                    RoomData destRoom = floor.getRoom(new RoomPos(0, 1));
                    te.setDestinationRoom(destRoom);
                    Room room = RoomGenerator.INSTANCE.generateRoom((ServerLevel) player.level(), destRoom, floor.getType().getStartingRoom(), te.getParentRoom().getGenerated().orElse(null), RoomDirection.NORTH, 0);
                    for (Player playerFromList : player.level().players()) {
                        PacketHandler.sendTo(new SCUpdateCORooms(floor.getRooms()), (ServerPlayer) playerFromList);
                    }
                    Utils.giveItems((ServerPlayer) player, new ItemStack(ModItems.keyOfBeginnings.get()));
                    return room;
                } else {
                    return floor.getRoom(new RoomPos(0, 1)).getGenerated().orElse(null);
                }
            }
        }
        return null;
    }

    public static boolean isExterior(ResourceKey<Level> level) {
        return level.equals(CASTLE_OBLIVION);
    }

    public static boolean isInterior(ResourceKey<Level> level) {
        return level.location().toString().contains("castle_oblivion_interior_");
    }

    public static boolean inExterior(Player player) {
        return isExterior(player.level().dimension());
    }

    public static boolean inInterior(Player player) {
        return isInterior(player.level().dimension());
    }

    @SubscribeEvent
    public void playerTick(PlayerTickEvent.Pre event) {
        if (!event.getEntity().level().isClientSide) {
            if (inExterior(event.getEntity())) {
                //Enter interior
                if (entranceBounds.isPlayerWithin(event.getEntity())) {
                    enterCastleOblivion(event.getEntity());
                }
            }
        }
    }

    @SubscribeEvent
    public void changeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        //if player is entering the interior
        if (isInterior(event.getTo())) {
            SCSyncCastleOblivionInteriorData.syncClients((ServerLevel) event.getEntity().level());
            ServerLevel level = event.getEntity().level().getServer().getLevel(event.getTo());
            Floor startFloor = Floor.getOrCreateFirstFloor(level);
            NeoForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeFloorEvent(null, startFloor, null, startFloor.getRoom(RoomPos.ZERO).getGenerated().orElse(null), event.getEntity()));
            PacketHandler.sendTo(new SCUpdateCORooms(getCurrentFloor(event.getEntity()).getRooms()), (ServerPlayer) event.getEntity());
        }
    }

    @SubscribeEvent
    public void joinWorld(PlayerEvent.PlayerLoggedInEvent event) {
        if (inInterior(event.getEntity())) {
            CastleOblivionData.InteriorData interiorData = CastleOblivionData.InteriorData.get((ServerLevel) event.getEntity().level()).orElseThrow();
            //backwards compatibility to store structure dimensions in room
            if (interiorData.needsUpdate(CastleOblivionData.InteriorData.STORE_STRUCTURE_DIMS)) {
                KingdomKeys.LOGGER.info("Updating outdated data");
                interiorData.getFloors().forEach(floor -> {
                    floor.getRooms().forEach(roomData -> {
                        roomData.getGenerated().ifPresent(room -> {
                            if (room.getDimensions().isEmpty()) {
                                room.readDimensionsFromStructure((ServerLevel) event.getEntity().level());
                            }
                        });
                    });
                });
                interiorData.appliedUpdate(CastleOblivionData.InteriorData.STORE_STRUCTURE_DIMS);
            }
            interiorData.sendToClient(event.getEntity());
        }
    }

    public static Floor getCurrentFloor(Player player) {
        return CastleOblivionData.InteriorData.get((ServerLevel) player.level()).orElseThrow().getFloorAtPos(player.blockPosition());
    }

    @SubscribeEvent
    public void changedRoom(CastleOblivionEvent.PlayerChangeRoomEvent event) {
        Room newRoom = event.getNewRoom();
        Room currentRoom = event.getCurrentRoom();
        if (currentRoom != null) {
            currentRoom.modifierOnExit(event.getPlayer());
            CastleOblivionData.InteriorData.get(event.getInteriorLevel()).ifPresent(interiorData -> {
                Floor floor = interiorData.getFloorByID(currentRoom.parentFloor);
                floor.getType().getGlobalModifiers().forEach(roomModifier -> roomModifier.onExit(currentRoom, event.getPlayer()));
            });
        }
        if (newRoom != null) {
            KingdomKeys.LOGGER.debug("Entered Room: {}", newRoom.getPosition());
            newRoom.modifierOnEnter(event.getPlayer());
            if (!newRoom.getType().isEntranceHall()) {
                Floor floor = CastleOblivionData.InteriorData.get(event.getInteriorLevel()).orElseThrow().getFloorByID(newRoom.parentFloor);
                floor.getType().getGlobalModifiers().forEach(roomModifier -> roomModifier.onEnter(newRoom, event.getPlayer()));
            }
        }
    }

    @SubscribeEvent
    public void generatedRoom(CastleOblivionEvent.RoomGeneratedEvent event) {
        event.getGeneratedRoomData().getGenerated().ifPresent(room -> {
            KingdomKeys.LOGGER.debug("Generated a new room: {}={}", room.getType(), room.getStructure().getRegistryName());
        });
    }

    @SubscribeEvent
    public void changeFloor(CastleOblivionEvent.PlayerChangeFloorEvent event) {
        if (event.getCurrentFloor() != null) {
            event.getCurrentFloor().getType().getGlobalModifiers().forEach(roomModifier -> roomModifier.onExit(event.getCurrentRoom(), event.getPlayer()));
        }
        if(event.getNewFloor() != null) {
            PacketHandler.sendTo(new SCUpdateCORooms(event.getNewFloor().getRooms()), (ServerPlayer) event.getPlayer());
        }
    }
}
