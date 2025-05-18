package online.kingdomkeys.kingdomkeys.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.storage.LevelResource;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.CastleOblivionData;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncGlobalData;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class ConvertOldForgeDataCommand extends BaseCommand {

    public static boolean run = false;

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("convert_old_forge_data").requires(source -> source.hasPermission(2)).executes(ConvertOldForgeDataCommand::convertData);
        KingdomKeys.LOGGER.info("Registered command {}", builder.getLiteral());
        return builder;
    }

    public static int convertData(CommandContext<CommandSourceStack> context) {
        if (!run) {
            context.getSource().sendFailure(Component.literal("WARNING This command overwrites the KK world and player data with any existing KK world and player data from Forge, run this command again to confirm you want to overwrite it"));
            run = true;
            return 0;
        } else {
            AtomicBoolean converted = new AtomicBoolean();
            Path worldDataFolder = context.getSource().getServer().getWorldPath(new LevelResource("data"));
            File capabilities = new File(worldDataFolder.toFile(), "capabilities.dat");
            Path castleOblivionData = context.getSource().getServer().getWorldPath(new LevelResource("dimensions/kingdomkeys/castle_oblivion/data"));
            File coCaps = new File(castleOblivionData.toFile(), "capabilities.dat");
            if (capabilities.exists()) {
                try (FileInputStream fis = new FileInputStream(capabilities)) {
                    DataInputStream inputStream = new DataInputStream(new PushbackInputStream(fis, 2));
                    CompoundTag main = NbtIo.readCompressed(inputStream, NbtAccounter.unlimitedHeap());
                    CompoundTag data = main.getCompound("data");
                    if (data.contains("kingdomkeys:world_capabilities")) {
                        WorldData.load(data.getCompound("kingdomkeys:world_capabilities"), context.getSource().registryAccess());
                        converted.set(true);
                        capabilities.delete();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (coCaps.exists()) {
                try (FileInputStream fis = new FileInputStream(coCaps)) {
                    DataInputStream inputStream = new DataInputStream(new PushbackInputStream(fis, 2));
                    CompoundTag main = NbtIo.readCompressed(inputStream, NbtAccounter.unlimitedHeap());
                    CompoundTag data = main.getCompound("data");
                    if (data.contains("kingdomkeys:castle_oblivion_exterior_capability")) {
                        CastleOblivionData.ExteriorData.load(data.getCompound("kingdomkeys:castle_oblivion_exterior_capability"), context.getSource().registryAccess());
                        converted.set(true);
                        capabilities.delete();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            CastleOblivionData.ExteriorData.get(context.getSource().getServer()).getInteriors().forEach((uuid, resourceLocation) -> {
                Path interiorData = context.getSource().getServer().getWorldPath(new LevelResource("dimensions/kingdomkeys/castle_oblivion_interior_" + uuid.toString() + "/data"));
                File interiorCap = new File(interiorData.toFile(), "capabilities.dat");
                if (interiorCap.exists()) {
                    try (FileInputStream fis = new FileInputStream(coCaps)) {
                        DataInputStream inputStream = new DataInputStream(new PushbackInputStream(fis, 2));
                        CompoundTag main = NbtIo.readCompressed(inputStream, NbtAccounter.unlimitedHeap());
                        CompoundTag data = main.getCompound("data");
                        if (data.contains("kingdomkeys:castle_oblivion_interior_capability")) {
                            CastleOblivionData.InteriorData.load(data.getCompound("kingdomkeys:castle_oblivion_interior_capability"), context.getSource().registryAccess());
                            converted.set(true);
                            capabilities.delete();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            Path playerDataFolder = context.getSource().getServer().getWorldPath(new LevelResource("playerdata"));
            PlayerList playerList = context.getSource().getServer().getPlayerList();
            if (Files.isDirectory(playerDataFolder)) {
                try (Stream<Path> files = Files.list(playerDataFolder)) {
                    files.forEach(path -> {
                        File playerDat = path.toFile();
                        ServerPlayer player = playerList.getPlayer(UUID.fromString(playerDat.getName().split("\\.")[0]));
                        try (FileInputStream fis = new FileInputStream(playerDat)) {
                            DataInputStream inputStream = new DataInputStream(new PushbackInputStream(fis, 2));
                            CompoundTag main = NbtIo.readCompressed(inputStream, NbtAccounter.unlimitedHeap());
                            if (main.contains("ForgeCaps")) {
                                CompoundTag forgeCaps = main.getCompound("ForgeCaps");
                                if (forgeCaps.contains("kingdomkeys:player_capabilities")) {
                                    CompoundTag playerCap = forgeCaps.getCompound("kingdomkeys:player_capabilities");
                                    PlayerData.get(player).deserializeNBT(context.getSource().registryAccess(), playerCap);
                                    PacketHandler.sendTo(new SCSyncPlayerData(player), player);
                                }
                                if (forgeCaps.contains("kingdomkeys:global_capabilities")) {
                                    CompoundTag globalCap = forgeCaps.getCompound("kingdomkeys:global_capabilities");
                                    GlobalData.get(player).deserializeNBT(context.getSource().registryAccess(), globalCap);
                                    PacketHandler.sendTo(new SCSyncGlobalData(player), player);
                                }
                                main.remove("ForgeCaps");
                                NbtIo.writeCompressed(main, path);
                                converted.set(true);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (!converted.get()) {
                context.getSource().sendFailure(Component.literal("No old data was found to convert"));
                return 0;
            } else {
                context.getSource().sendSuccess(() -> Component.literal("Successfully converted data"), true);
                return 1;
            }
        }
    }

}
