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
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

public class FocusPointsCommand extends BaseCommand{ //kingdomkeys focus <give/take/set> <amount> [player]
	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("focus").requires(source -> source.hasPermission(2));
		
		builder.then(Commands.literal("set")
			.then(Commands.argument("value", IntegerArgumentType.integer(1,Integer.MAX_VALUE))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(FocusPointsCommand::setValue)
				)
				.executes(FocusPointsCommand::setValue)
			)
		);
		
		builder.then(Commands.literal("give")
			.then(Commands.argument("value", IntegerArgumentType.integer(1,Integer.MAX_VALUE))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(FocusPointsCommand::addValue)
				)
				.executes(FocusPointsCommand::addValue)
			)
		);
		
		builder.then(Commands.literal("take")
			.then(Commands.argument("value", IntegerArgumentType.integer(1,Integer.MAX_VALUE))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(FocusPointsCommand::removeValue)
				)
				.executes(FocusPointsCommand::removeValue)
			)
		);
		
		KingdomKeys.LOGGER.warn("Registered command "+builder.getLiteral());
		return builder;
	}

	private static int setValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);
		int value = IntegerArgumentType.getInteger(context, "value");
		
		for (ServerPlayer player : players) {
			setValue(context,value,player);
		}
		return 1;
	}
	
	private static int setValue(CommandContext<CommandSourceStack> context, int value, ServerPlayer player) throws CommandSyntaxException {
		PlayerData playerData = PlayerData.get(player);
		playerData.setFocus(value);
		context.getSource().sendSuccess(() -> Component.translatable("Set "+player.getDisplayName().getString()+" focus to "+value), true);
		player.sendSystemMessage(Component.translatable("Your focus has been set to "+value));
		PacketHandler.sendTo(new SCSyncPlayerData(player), player);
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
		playerData.addFocus(value);
		context.getSource().sendSuccess(() -> Component.translatable("Added "+value+" focus to "+player.getDisplayName().getString()), true);
		player.sendSystemMessage(Component.translatable("Your focus has been increased by "+value));
		PacketHandler.sendTo(new SCSyncPlayerData(player), player);
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
		playerData.remFocus(value);
		context.getSource().sendSuccess(() -> Component.translatable("Taken "+value+" focus from "+player.getDisplayName().getString()), true);
		player.sendSystemMessage(Component.translatable("Your focus has been decreased by "+value));
		PacketHandler.sendTo(new SCSyncPlayerData(player), player);
		return 1;
	}
}
