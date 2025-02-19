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
import online.kingdomkeys.kingdomkeys.world.dimension.DynamicDimensionManager;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.*;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

import java.util.List;

public class CastleOblivionHandler {

    //Ticking rooms that players are in, empty rooms should be inactive
    @SubscribeEvent
    public void tick(TickEvent.LevelTickEvent event) {
        if (event.level.dimension().toString().contains(KingdomKeys.MODID + ":castle_oblivion_interior_")) {
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
        if (event.getPlayer().level().dimension().location().toString().contains("castle_oblivion_interior_")) {
            if (event.getState().getBlock() == ModBlocks.cardDoor.get()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (!event.player.level().isClientSide) {
            if (event.player.level().dimension().equals(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(KingdomKeys.MODID, "castle_oblivion")))) {
                BlockPos pos = event.player.blockPosition();
                //Enter interior
                if (pos.getZ() == 11 && pos.getX() >= -10 && pos.getX() <= -1 && pos.getY() >= 87 && pos.getY() <= 100) {
                    if (event.player.level().getServer() != null) {
                        ResourceLocation dimName = new ResourceLocation(KingdomKeys.MODID, "castle_oblivion_interior_" + event.player.getStringUUID());
                        ModCapabilities.getCastleOblivionExterior(event.player.level()).addInterior(event.player.getUUID(), dimName);
                        RegistryAccess registryAccess = event.player.level().registryAccess();
                        ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, dimName);
                        Holder<DimensionType> type = registryAccess.registryOrThrow(Registries.DIMENSION_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DIMENSION_TYPE, new ResourceLocation(KingdomKeys.MODID, "castle_oblivion")));
                        Holder<Biome> biome = registryAccess.registryOrThrow(Registries.BIOME).getHolderOrThrow(ResourceKey.create(Registries.BIOME, new ResourceLocation(KingdomKeys.MODID, Strings.castleOblivionInterior)));
                        ServerLevel level = DynamicDimensionManager.getOrCreateLevel(event.player.level().getServer(), dimension, ((minecraftServer, levelStemResourceKey) -> {
                            ChunkGenerator generator = new CastleOblivionInteriorChunkGenerator(new FixedBiomeSource(biome));
                            return new LevelStem(type, generator);
                        }));
                        event.player.changeDimension(level, new BaseTeleporter(16, 62, 2));
                    }
                }
            } else if (event.player.level().dimension().location().toString().contains("castle_oblivion_interior_")) {
                BlockPos pos = event.player.blockPosition();
                //Exit from first floor lobby
                if (pos.getZ() == 1 && pos.getX() >= 13 && pos.getX() <= 19 && pos.getY() >= 60 && pos.getY() <= 70) {
                    if (event.player.level().getServer() != null) {
                        event.player.changeDimension(event.player.level().getServer().getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(KingdomKeys.MODID, "castle_oblivion"))), new BaseTeleporter(-5, 90, 6));
                    }
                }
                //Set world card for first room and enter
                if (pos.getZ() == 67 && pos.getX() >= 15 && pos.getX() <= 17 && pos.getY() >= 63 && pos.getY() <= 66) {
                    //todo world card stuff, instead should open world card gui then generate room based on world and telport afterwards
                    CastleOblivionCapabilities.ICastleOblivionInteriorCapability cap = ModCapabilities.getCastleOblivionInterior(event.player.level());
                    if (cap != null) {
                        Room currentRoom = cap.getRoomAtPos(event.player.level(), pos);
                        if (currentRoom == null) {
                            KingdomKeys.LOGGER.info("something is wrong player should be in the lobby room");
                        } else {
                            Floor floor = cap.getFloors().get(0);
                            if (floor != null) {
                                //if size is 1 only the lobby room exists
                                if (floor.getGeneratedRooms().size() == 1) {
                                    floor.setWorldCard((WorldCardItem) ModItems.netherCard.get());
                                    for (Player playerFromList : event.player.level().players()) {
                                        PacketHandler.sendTo(new SCUpdateCORooms(floor.getRooms()), (ServerPlayer) playerFromList);
                                    }
                                    RoomData data = floor.getRoom(new RoomUtils.RoomPos(0, 1));
                                    Room newRoom = RoomGenerator.INSTANCE.generateRoom(data, ModRoomTypes.SLEEPING_DARKNESS.get(), event.player, currentRoom, RoomUtils.Direction.NORTH, false);
                                }
                                Room firstRoom = floor.getRoom(new RoomUtils.RoomPos(0, 1)).getGenerated();
                                BlockPos newPos = firstRoom.doorPositions.get(RoomUtils.Direction.NORTH);
                                CardDoorTileEntity te = (CardDoorTileEntity) event.player.level().getBlockEntity(newPos);
                                if (te != null) { //null check in case door is destroyed
                                    if (!MinecraftForge.EVENT_BUS.post(new CastleOblivionEvent.PlayerChangeRoomEvent(currentRoom, firstRoom, event.player))) {
                                        newPos = newPos.offset(te.getDirection().toMCDirection().getNormal().multiply(2));
                                        te.openDoor(RoomUtils.Direction.NORTH);
                                        event.player.teleportTo(newPos.getX(), newPos.getY(), newPos.getZ());
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void changeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getFrom().equals(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(KingdomKeys.MODID, "castle_oblivion")))) {
            if (event.getTo().location().toString().contains(KingdomKeys.MODID + ":castle_oblivion_interior_")) {
                SCSyncCastleOblivionInteriorCapability.syncClients(event.getEntity().level());
                event.getEntity().sendSystemMessage(Component.translatable("I REPEAT, CASTLE OBLIVION IS WORK IN PROGRESS DON'T REPORT ANY ISSUES WITH IT YET PLEASE"));
                event.getEntity().sendSystemMessage(Component.translatable("IF YOUR GAME CRASHES HERE IT'S EXPECTED, THE OUTSIDE PART IS PROBABLY SAFE FROM CRASHES BUT NOT HERE DEFINITELY NOT HERE"));
                event.getEntity().sendSystemMessage(Component.translatable("THANK YOU AGAIN - Estelle"));
                ServerLevel level = event.getEntity().level().getServer().getLevel(event.getTo());
                CastleOblivionCapabilities.ICastleOblivionInteriorCapability cap = ModCapabilities.getCastleOblivionInterior(level);
                if (cap.getFloors().isEmpty()) {
                    Floor startFloor = new Floor();
                    Room lobby = new Room(startFloor.getFloorID(), new RoomUtils.RoomPos(0, 0));
                    Room.createDefaultLobby(lobby);
                    startFloor.getRoom(new RoomUtils.RoomPos(0, 0)).setGenerated(lobby);
                    startFloor.createLobby(lobby.position);
                    cap.addFloor(startFloor);
                } else {
                    PacketHandler.sendTo(new SCUpdateCORooms(getCurrentFloor(event.getEntity()).getRooms()), (ServerPlayer) event.getEntity());
                }
            }
        } else {
            PacketHandler.sendTo(new SCUpdateCORooms(List.of()), (ServerPlayer) event.getEntity());
        }
    }

    public static Floor getCurrentFloor(Player player) {
        return ModCapabilities.getCastleOblivionInterior(player.level()).getFloorAtPos(player.level(), player.blockPosition());
    }

    @SubscribeEvent
    public void changedRoom(CastleOblivionEvent.PlayerChangeRoomEvent event) {
        System.out.println("Entered Room: " + event.getNewRoom().position);
    }

    @SubscribeEvent
    public void generatedRoom(CastleOblivionEvent.RoomGeneratedEvent event) {
        System.out.println("Generated a new room: " + event.getGeneratedRoomData().getGenerated());
    }
}
