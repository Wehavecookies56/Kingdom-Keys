package online.kingdomkeys.kingdomkeys.command;

import java.util.Collection;
import java.util.List;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

public class DimensionCommand extends BaseCommand {

	public static SuggestionProvider<CommandSourceStack> SUGGEST_DIMENSIONS = (p_198296_0_, p_198296_1_) -> {
		List<String> list = ServerLifecycleHooks.getCurrentServer().levelKeys().stream().map(rk -> rk.location().toString()).toList();
		return SharedSuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
	};

	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("dimension").requires(source -> source.hasPermission(2));
		builder.then(Commands.argument("dim", StringArgumentType.string()).suggests(SUGGEST_DIMENSIONS).then(Commands.argument("targets", EntityArgument.players()).executes(DimensionCommand::changeDim)).executes(DimensionCommand::changeDim));

		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
		return builder;
	}

	private static int changeDim(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 3);
		String dim = StringArgumentType.getString(context, "dim");
		ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dim));

		if (dimension == null) {
			context.getSource().sendSuccess(() -> Component.translatable("Invalid dimension " + dim), true);
			return 1;
		}
		for (ServerPlayer player : players) {
			BlockPos coords = getWorldCoords(player, dimension);
			player.changeDimension(player.getServer().getLevel(dimension), new BaseTeleporter(coords.getX(), coords.getY(), coords.getZ()));
			context.getSource().sendSuccess(() -> Component.translatable("Teleported " + player.getDisplayName().getString() + " to dimension " + dimension.location().toString()), true);
			player.sendSystemMessage(Component.translatable("You have been teleported to " + dimension.location().toString()));
		}
		return 1;
	}

	public static BlockPos getWorldCoords(Player player, ResourceKey<Level> dimension) {
		if (dimension.location().toString().contains("castle_oblivion_interior_")) {
			return new BlockPos(8, 62, 8);
		}
		if (dimension == ModDimensions.DIVE_TO_THE_HEART) {
			return new BlockPos(0, 26, 0);
		}
		if (dimension == ModDimensions.STATION_OF_SORROW) {
			return new BlockPos(0, 26, 0);
		}
		if(dimension.location().toString().contains("realm_of_darkness")) {
			return player.getServer().getLevel(dimension).getSharedSpawnPos();
		}

		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		if (dimension == playerData.getReturnDimension()) {
			Vec3 vec3 = playerData.getReturnLocation();
			//TODO fix cast
			return new BlockPos((int)vec3.x, (int)vec3.y, (int)vec3.z);
		}
		return new BlockPos(0, 64, 0);
	}
}
