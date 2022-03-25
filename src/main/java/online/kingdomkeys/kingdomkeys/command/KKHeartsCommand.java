package online.kingdomkeys.kingdomkeys.command;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class KKHeartsCommand extends BaseCommand{ //kk_hearts <give/take/set> <amount> [player]
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("kk_hearts").requires(source -> source.hasPermission(2));
		
		builder.then(Commands.literal("set")
			.then(Commands.argument("value", IntegerArgumentType.integer(1,Integer.MAX_VALUE))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(KKHeartsCommand::setValue)
				)
				.executes(KKHeartsCommand::setValue)
			)
		);
		
		builder.then(Commands.literal("give")
			.then(Commands.argument("value", IntegerArgumentType.integer(1,Integer.MAX_VALUE))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(KKHeartsCommand::addValue)
				)
				.executes(KKHeartsCommand::addValue)
			)
		);
		
		builder.then(Commands.literal("take")
			.then(Commands.argument("value", IntegerArgumentType.integer(1,Integer.MAX_VALUE))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(KKHeartsCommand::removeValue)
				)
				.executes(KKHeartsCommand::removeValue)
			)
		);
		
		dispatcher.register(builder);
		KingdomKeys.LOGGER.warn("Registered command "+builder.getLiteral());
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
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setHearts(value);
		
			context.getSource().sendSuccess(new TranslatableComponent("Set "+player.getDisplayName().getString()+" hearts to "+value), true);
		
		player.sendMessage(new TranslatableComponent("Your hearts have been set to "+value),Util.NIL_UUID);
		return 1;
	}
	
	private static int addValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 3);
		int value = IntegerArgumentType.getInteger(context, "value");
		
		for (ServerPlayer player : players) {
			addValue(context,value,player);
		}
		return 1;
	}
	private static int addValue(CommandContext<CommandSourceStack> context, int value, ServerPlayer player) throws CommandSyntaxException {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.addHearts(value);
		
			context.getSource().sendSuccess(new TranslatableComponent("Added "+value+" hearts to "+player.getDisplayName().getString()), true);
		
		player.sendMessage(new TranslatableComponent("Your hearts have been increased by "+value),Util.NIL_UUID);
		return 1;
	}
	
	private static int removeValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 3);
		int value = IntegerArgumentType.getInteger(context, "value");
		
		for (ServerPlayer player : players) {
			removeValue(context,value,player);
		}
		return 1;
	}
	
	private static int removeValue(CommandContext<CommandSourceStack> context, int value, ServerPlayer player) throws CommandSyntaxException {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.removeHearts(value);
		
			context.getSource().sendSuccess(new TranslatableComponent("Taken "+value+" hearts from "+player.getDisplayName().getString()), true);
		
		player.sendMessage(new TranslatableComponent("Your hearts have been decreased by "+value),Util.NIL_UUID);
		return 1;
	}
}
