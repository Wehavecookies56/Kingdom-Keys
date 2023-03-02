package online.kingdomkeys.kingdomkeys.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.mob.MarluxiaEntity;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

public class WhisperInMyEarPinkHairMan extends BaseCommand { // kk_wisperinmyearpinkhairman
	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("whisperinmyearpinkhairman").requires(source -> source.hasPermission(2));

		builder.executes(WhisperInMyEarPinkHairMan::spawn);
		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
		return builder;
	}

	private static int spawn(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ServerPlayer player = context.getSource().getPlayerOrException();
		if(player.level.dimension() == ModDimensions.STATION_OF_SORROW) {
			ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("overworld"));
			BlockPos coords = DimensionCommand.getWorldCoords(player, dimension);
			player.changeDimension(player.getServer().getLevel(dimension), new BaseTeleporter(coords.getX(), coords.getY(), coords.getZ()));
			player.sendSystemMessage(Component.translatable("You have been teleported to " + dimension.location()));
		} else {
			ResourceKey<Level> dimension = ModDimensions.STATION_OF_SORROW;
			BlockPos coords = DimensionCommand.getWorldCoords(player, dimension);
			player.changeDimension(player.getServer().getLevel(dimension), new BaseTeleporter(coords.getX(), coords.getY(), coords.getZ()));
			player.sendSystemMessage(Component.translatable("You have been returned back to " + dimension.location()));
			MarluxiaEntity marluxia = new MarluxiaEntity(player.level);
			marluxia.finalizeSpawn((ServerLevel)player.level, player.level.getCurrentDifficultyAt(marluxia.blockPosition()), MobSpawnType.COMMAND, null, null);
			player.level.addFreshEntity(marluxia);
			marluxia.setPos(player.getX(), player.getY(), player.getZ() - 6);
		}
		return 1;
	}
}
