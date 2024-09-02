package online.kingdomkeys.kingdomkeys.command;

import java.util.Collection;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;

public class HeartsCommand extends BaseCommand{ //kk_hearts <give/take/set> <amount> [player]
	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("hearts").requires(source -> source.hasPermission(2));
		
		builder.then(Commands.literal("set")
			.then(Commands.argument("value", IntegerArgumentType.integer(1,Integer.MAX_VALUE))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(HeartsCommand::setValue)
				)
				.executes(HeartsCommand::setValue)
			)
		);
		
		builder.then(Commands.literal("give")
			.then(Commands.argument("value", IntegerArgumentType.integer(1,Integer.MAX_VALUE))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(HeartsCommand::addValue)
				)
				.executes(HeartsCommand::addValue)
			)
		);
		
		builder.then(Commands.literal("take")
			.then(Commands.argument("value", IntegerArgumentType.integer(1,Integer.MAX_VALUE))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(HeartsCommand::removeValue)
				)
				.executes(HeartsCommand::removeValue)
			)
		);
		
		KingdomKeys.LOGGER.warn("Registered command "+builder.getLiteral());
		return builder;
	}

	private static int setValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 3);
		int value = IntegerArgumentType.getInteger(context, "value");
		
		for (ServerPlayer player : players) {
			setValue(context,value,player);
		}
		return 1;
	}
	
	private static int setValue(CommandContext<CommandSourceStack> context, int value, ServerPlayer player) throws CommandSyntaxException {
		PlayerData playerData = PlayerData.get(player);
		playerData.setHearts(value);
		
			context.getSource().sendSuccess(() -> Component.translatable("Set "+player.getDisplayName().getString()+" hearts to "+value), true);
		
		player.sendSystemMessage(Component.translatable("Your hearts have been set to "+value));
		return 1;
	}
	
	private static int addValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);
		int value = IntegerArgumentType.getInteger(context, "value");
		
		for (ServerPlayer player : players) {
			addValue(context,value,player);
		}
		return 1;
	}
	private static int addValue(CommandContext<CommandSourceStack> context, int value, ServerPlayer player) throws CommandSyntaxException {
		PlayerData playerData = PlayerData.get(player);
		playerData.addHearts(value);
		
			context.getSource().sendSuccess(() -> Component.translatable("Added "+value+" hearts to "+player.getDisplayName().getString()), true);
		
		player.sendSystemMessage(Component.translatable("Your hearts have been increased by "+value));
		return 1;
	}
	
	private static int removeValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);
		int value = IntegerArgumentType.getInteger(context, "value");
		
		for (ServerPlayer player : players) {
			removeValue(context,value,player);
		}
		return 1;
	}
	
	private static int removeValue(CommandContext<CommandSourceStack> context, int value, ServerPlayer player) throws CommandSyntaxException {
		PlayerData playerData = PlayerData.get(player);
		playerData.removeHearts(value);
		
			context.getSource().sendSuccess(() -> Component.translatable("Taken "+value+" hearts from "+player.getDisplayName().getString()), true);
		
		player.sendSystemMessage(Component.translatable("Your hearts have been decreased by "+value));
		return 1;
	}
}
