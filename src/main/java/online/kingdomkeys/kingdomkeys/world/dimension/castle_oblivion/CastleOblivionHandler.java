package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
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
import online.kingdomkeys.kingdomkeys.network.stc.SCUpdateCORooms;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.world.dimension.DynamicDimensionManager;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.floor.Floor;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CastleOblivionHandler {

    //Ticking rooms that players are in, empty rooms should be inactive
    @SubscribeEvent
    public void tick(LevelTickEvent.Pre event) {
        if (!event.getLevel().isClientSide()) {
            if(event.getLevel().dimension().location().getNamespace().equals(KingdomKeys.MODID)) {//Attempt to alleviate load
                CastleOblivionData.InteriorData.get((ServerLevel) event.getLevel()).ifPresent(interiorData -> {
                    interiorData.getFloors().forEach(floor -> {
                        floor.getRooms().forEach(roomData -> {
                            roomData.getGenerated().ifPresent(room -> {
                                List<Player> players = Room.getPlayersInRoom((ServerLevel) event.getLevel(), room);
                                floor.getType().getGlobalModifiers().forEach(roomModifier -> {
                                    roomModifier.tick(room, players);
                                });
                                room.tick((ServerLevel) event.getLevel(), players);
                            });
                        });
                    });
                });
            }
        }
    }

    @SubscribeEvent
    public void tick(PlayerTickEvent.Pre event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity().level().getDifficulty() == Difficulty.PEACEFUL) {
            if(event.getEntity().level().dimension().location().getNamespace().equals(KingdomKeys.MODID)) {//Attempt to alleviate load
                CastleOblivionData.InteriorData.get((ServerLevel) event.getEntity().level()).ifPresent(interiorData -> {
                    Room currentRoom = interiorData.getRoomAtPos(event.getEntity().blockPosition());
                    Floor floor = null;
                    if (currentRoom != null) {
                        floor = currentRoom.getParent(interiorData);
                    }
                    exitCastleOblivion(floor, currentRoom, event.getEntity());
                    event.getEntity().sendSystemMessage(Component.translatable("kingdomkeys.castle_oblivion.peaceful"));
                });
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
            if (player.level().getDifficulty() != Difficulty.PEACEFUL) {
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
                player.changeDimension(new DimensionTransition(level, new Vec3(entrancePos.getX(), entrancePos.getY(), entrancePos.getZ()), Vec3.ZERO, player.getYRot(), player.getXRot(), entity -> {
                }));

                if (player instanceof ServerPlayer sPlayer) {
                    Utils.showTutorial(sPlayer, Constants.TUTORIALS.get(Constants.TUTORIAL_CO_LOBBY));
                }
            } else {
                player.sendSystemMessage(Component.translatable("kingdomkeys.castle_oblivion.peaceful"));
                player.teleportTo(entranceBounds.min().getX() + ((entranceBounds.max().getX() - entranceBounds.min().getX()) / 2F), entranceBounds.min().getY() + 1, entranceBounds.min().getZ() - 2);
            }
        }
    }

    //teleports the player outside the front of Castle Oblivion
    public static void exitCastleOblivion(Floor currentFloor, Room currentRoom, Player player) {
        if (player.level().getServer() != null) {
            // Leaving the room and floor is handled when the dimension changes, the same as any other way out
            player.changeDimension(new DimensionTransition(player.level().getServer().getLevel(CASTLE_OBLIVION), new Vec3(exitPos.getX(), exitPos.getY(), exitPos.getZ()), Vec3.ZERO, player.getYRot(), player.getXRot(), entity -> {}));
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
                    Utils.giveItems((ServerPlayer) player, true, new ItemStack(ModItems.keyOfBeginnings.get()));
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

    // Last room each player was in, so leaving an instance by any means (door, wayfinder, command...) takes its modifiers off
    private static final Map<UUID, Room> LAST_ROOM = new HashMap<>();

    @SubscribeEvent
    public void changeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }

        if (isInterior(event.getFrom()) && !event.getFrom().equals(event.getTo())) {
            leaveInstance(server, player, event.getFrom());
        }

        //if player is entering the interior
        if (isInterior(event.getTo())) {
            ServerLevel level = server.getLevel(event.getTo());
            if (level == null) {
                return;
            }

            Floor startFloor = Floor.getOrCreateFirstFloor(level);
            CastleOblivionData.InteriorData interiorData = CastleOblivionData.InteriorData.get(level).orElseThrow();
            interiorData.sendToClient(player);

            // Not always the entrance: a wayfinder drops you wherever its owner is standing
            Floor floor = interiorData.getFloorAtPos(player.blockPosition());
            Room room = interiorData.getRoomAtPos(player.blockPosition());
            if (floor == null) {
                floor = startFloor;
            }

            NeoForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeFloorEvent(null, floor, null, room != null ? room : floor.getRoom(RoomPos.ZERO).getGenerated().orElse(null), player));

            if (room != null && !room.getType().isEntranceHall()) {
                NeoForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeRoomEvent(null, room, player));
            }
        }
    }

    private static void leaveInstance(MinecraftServer server, Player player, ResourceKey<Level> from) {
        Room room = LAST_ROOM.remove(player.getUUID());
        ServerLevel level = server.getLevel(from);
        Floor floor = room == null || level == null ? null : CastleOblivionData.InteriorData.get(level).map(room::getParent).orElse(null);

        if (room != null) {
            NeoForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeRoomEvent(room, null, player));
        }
        if (floor != null) {
            NeoForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeFloorEvent(floor, null, room, null, player));
        }
    }

    @SubscribeEvent
    public void loggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        LAST_ROOM.remove(event.getEntity().getUUID());
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
            LAST_ROOM.put(event.getPlayer().getUUID(), newRoom);
        } else {
            LAST_ROOM.remove(event.getPlayer().getUUID());
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
