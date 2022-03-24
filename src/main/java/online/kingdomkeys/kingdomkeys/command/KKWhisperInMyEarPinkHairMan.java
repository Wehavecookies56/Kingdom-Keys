package online.kingdomkeys.kingdomkeys.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.mob.MarluxiaEntity;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

public class KKWhisperInMyEarPinkHairMan extends BaseCommand { // kk_wisperinmyearpinkhairman
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> builder = Commands.literal("kk_wisperinmyearpinkhairman").requires(source -> source.hasPermissionLevel(2));

		builder.executes(KKWhisperInMyEarPinkHairMan::spawn);
		dispatcher.register(builder);
		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
	}

	private static int spawn(CommandContext<CommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = context.getSource().asPlayer();
		if(player.world.getDimensionKey() == ModDimensions.STATION_OF_SORROW) {
			RegistryKey<World> dimension = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation("overworld"));
			BlockPos coords = KKDimensionCommand.getWorldCoords(player, dimension);
			player.changeDimension(player.getServer().getWorld(dimension), new BaseTeleporter(coords.getX(), coords.getY(), coords.getZ()));
			player.sendMessage(new TranslationTextComponent("You have been teleported to " + dimension.getLocation().toString()), Util.DUMMY_UUID);
		} else {
			RegistryKey<World> dimension = ModDimensions.STATION_OF_SORROW;
			BlockPos coords = KKDimensionCommand.getWorldCoords(player, dimension);
			player.changeDimension(player.getServer().getWorld(dimension), new BaseTeleporter(coords.getX(), coords.getY(), coords.getZ()));
			player.sendMessage(new TranslationTextComponent("You have been returned back to " + dimension.getLocation().toString()), Util.DUMMY_UUID);
			MarluxiaEntity marluxia = new MarluxiaEntity(player.world);
			marluxia.onInitialSpawn((ServerWorld)player.world, player.world.getDifficultyForLocation(marluxia.getPosition()), SpawnReason.COMMAND, (ILivingEntityData)null, (CompoundNBT)null);
			player.world.addEntity(marluxia);
			marluxia.setPosition(player.getPosX(), player.getPosY(), player.getPosZ() - 6);
		}
		return 1;
	}
}
