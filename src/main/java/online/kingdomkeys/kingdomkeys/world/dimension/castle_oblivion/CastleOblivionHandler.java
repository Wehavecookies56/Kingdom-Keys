package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.CastleOblivionCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.world.dimension.DynamicDimensionManager;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.*;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

import java.util.UUID;

public class CastleOblivionHandler {

    //Ticking rooms that players are in, empty rooms should be inactive
    @SubscribeEvent
    public void tick(TickEvent.WorldTickEvent event) {
        if (event.world.dimension().getRegistryName().toString().contains(KingdomKeys.MODID + ":castle_oblivion_interior_")) {
            CastleOblivionCapabilities.ICastleOblivionInteriorCapability cap = ModCapabilities.getCastleOblivionInterior(event.world);
            if (cap != null) {
                cap.getFloors().forEach(floor -> {
                    if (floor.shouldTick()) {
                        floor.getPlayers().values().forEach(roompos -> floor.getRoom(roompos).getGenerated().tick());
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.level.isClientSide) {
            if (event.player.level.dimension().equals(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(KingdomKeys.MODID, "castle_oblivion")))) {
                BlockPos pos = event.player.blockPosition();
                //Enter interior
                if (pos.getZ() == 132 && pos.getX() >= 145 && pos.getX() <= 154 && pos.getY() == 87) {
                    if (event.player.level.getServer() != null) {
                        ResourceLocation dimName = new ResourceLocation(KingdomKeys.MODID, "castle_oblivion_interior_" + event.player.getStringUUID());
                        ModCapabilities.getCastleOblivionExterior(event.player.level).addInterior(event.player.getUUID(), dimName);
                        RegistryAccess registryAccess = event.player.level.registryAccess();
                        ResourceKey<Level> dimension = ResourceKey.create(Registry.DIMENSION_REGISTRY, dimName);
                        Holder<DimensionType> type = registryAccess.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getHolderOrThrow(ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation(KingdomKeys.MODID, "castle_oblivion")));
                        ServerLevel level = DynamicDimensionManager.getOrCreateLevel(event.player.level.getServer(), dimension, ((minecraftServer, levelStemResourceKey) -> {
                            ChunkGenerator generator = new CastleOblivionInteriorChunkGenerator(
                                    registryAccess.registryOrThrow(Registry.STRUCTURE_SET_REGISTRY),
                                    registryAccess.registryOrThrow(Registry.BIOME_REGISTRY)
                            );
                            return new LevelStem(type, generator, true);
                        }));
                        event.player.changeDimension(level, new BaseTeleporter(16, 62, 2));
                    }
                }
            } else if (event.player.level.dimension().location().toString().contains("castle_oblivion_interior_")) {
                BlockPos pos = event.player.blockPosition();
                //Exit from first floor lobby
                if (pos.getZ() == 1 && pos.getX() >= 13 && pos.getX() <= 19 && pos.getY() >= 60 && pos.getY() <= 70) {
                    if (event.player.level.getServer() != null) {
                        event.player.changeDimension(event.player.level.getServer().getLevel(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(KingdomKeys.MODID, "castle_oblivion"))), new BaseTeleporter(150, 90, 134));
                    }
                }
                //Set world card for first room and enter
                if (pos.getZ() == 67 && pos.getX() >= 15 && pos.getX() <= 17 && pos.getY() >= 63 && pos.getY() <= 66) {
                    //todo world card stuff, instead should open world card gui then generate room based on world and telport afterwards
                    CastleOblivionCapabilities.ICastleOblivionInteriorCapability cap = ModCapabilities.getCastleOblivionInterior(event.player.level);
                    if (cap != null) {
                        Room currentRoom = cap.getRoomAtPos(pos);
                        if (currentRoom == null) {
                            KingdomKeys.LOGGER.info("something is wrong player should be in the lobby room");
                        } else {
                            Floor floor = cap.getFloors().get(0);
                            if (floor != null) {
                                //if size is 1 only the lobby room exists
                                if (floor.getGeneratedRooms().size() == 1) {
                                    RoomData data = floor.getRoom(new RoomUtils.RoomPos(0, 1));
                                    Room newRoom = RoomGenerator.INSTANCE.generateRoom(data, ModRoomTypes.TRANQUIL_DARKNESS.get(), event.player, currentRoom, RoomUtils.Direction.NORTH, false);
                                }
                                Room firstRoom = floor.getRoom(new RoomUtils.RoomPos(0, 1)).getGenerated();
                                BlockPos newPos = firstRoom.doorPositions.get(RoomUtils.Direction.NORTH);
                                event.player.moveTo(new Vec3(newPos.getX(), newPos.getY(), newPos.getZ()+1));
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void changeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getFrom().equals(ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(KingdomKeys.MODID, "castle_oblivion")))) {
            if (event.getTo().location().toString().contains(KingdomKeys.MODID + ":castle_oblivion_interior_")) {
                ServerLevel level = event.getPlayer().level.getServer().getLevel(event.getTo());
                CastleOblivionCapabilities.ICastleOblivionInteriorCapability cap = ModCapabilities.getCastleOblivionInterior(level);
                if (cap.getFloors().isEmpty()) {
                    Floor startFloor = new Floor();
                    Room lobby = new Room(startFloor.getFloorID());
                    Room.createDefaultLobby(lobby);
                    startFloor.setWorldCardProps(7, 0);
                    startFloor.generateLayout();
                    startFloor.getRoom(new RoomUtils.RoomPos(0, 0)).setGenerated(lobby);
                    startFloor.createLobby(lobby.position);
                    cap.addFloor(startFloor);
                }
            }
        }
    }

    public static Floor getCurrentFloor(Player player) {
        //TODO do a right proper check
        return ModCapabilities.getCastleOblivionInterior(player.level).getFloors().get(0);
    }
}
