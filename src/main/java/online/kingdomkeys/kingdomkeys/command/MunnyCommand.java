package online.kingdomkeys.kingdomkeys.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

import java.util.Collection;

public class MunnyCommand extends BaseCommand { // kk_munny <give/take/set/pay> <amount> [player]
	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("munny").requires(source -> source.hasPermission(2));

		builder.then(Commands.literal("set")
				.then(Commands.argument("value", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
						.then(Commands.argument("targets", EntityArgument.players())
								.executes(MunnyCommand::setValue))
						.executes(MunnyCommand::setValue)));

		builder.then(Commands.literal("give")
				.then(Commands.argument("value", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
						.then(Commands.argument("targets", EntityArgument.players())
								.executes(MunnyCommand::addValue))
						.executes(MunnyCommand::addValue)));

		builder.then(Commands.literal("take")
				.then(Commands.argument("value", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
						.then(Commands.argument("targets", EntityArgument.players())
								.executes(MunnyCommand::removeValue))
						.executes(MunnyCommand::removeValue)));
		
	

		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
		return builder;
	}

	private static int setValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);
		int value = IntegerArgumentType.getInteger(context, "value");

		for (ServerPlayer player : players) {
			setValue(context, value, player);
		}
		return 1;
	}

	private static int setValue(CommandContext<CommandSourceStack> context, int value, ServerPlayer player) throws CommandSyntaxException {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setMunny(value);

		context.getSource().sendSuccess(new TranslatableComponent("Set " + player.getDisplayName().getString() + " munny to " + value), true);

		player.sendMessage(new TranslatableComponent("Your munny has been set to " + value), Util.NIL_UUID);
		return 1;
	}

	private static int addValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);
		int value = IntegerArgumentType.getInteger(context, "value");

		for (ServerPlayer player : players) {
			addValue(context, value, player);
		}
		return 1;
	}

	private static int addValue(CommandContext<CommandSourceStack> context, int value, ServerPlayer player) throws CommandSyntaxException {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setMunny(playerData.getMunny() + value);

		context.getSource().sendSuccess(new TranslatableComponent("Added " + value + " munny to " + player.getDisplayName().getString()), true);

		player.sendMessage(new TranslatableComponent("Your munny has been increased by " + value), Util.NIL_UUID);
		return 1;
	}

	private static int removeValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);
		int value = IntegerArgumentType.getInteger(context, "value");

		for (ServerPlayer player : players) {
			removeValue(context, value, player);
		}
		return 1;
	}

	private static int removeValue(CommandContext<CommandSourceStack> context, int value, ServerPlayer player) throws CommandSyntaxException {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setMunny(playerData.getMunny() - value);

		context.getSource().sendSuccess(new TranslatableComponent("Taken " + value + " munny from " + player.getDisplayName().getString()), true);

		player.sendMessage(new TranslatableComponent("Your munny has been decreased by " + value), Util.NIL_UUID);
		return 1;
	}
}
