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
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;

public class PayMunnyCommand extends BaseCommand { // kk_paymunny <player> <value>
	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("paymunny").requires(source -> source.hasPermission(0));
		
		builder.then(Commands.argument("targets", EntityArgument.players())
				.then(Commands.argument("value", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
					.executes(PayMunnyCommand::payValue)));

		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
		return builder;
	}

	private static int payValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		ServerPlayer user = context.getSource().getPlayerOrException();
		IPlayerData userData = ModData.getPlayer(user);

		Collection<ServerPlayer> players = EntityArgument.getPlayers(context, "targets");
		int value = IntegerArgumentType.getInteger(context, "value");
		if(userData.getMunny() - (value * players.size()) >= 0) {
			for (ServerPlayer target : players) {
				IPlayerData targetData = ModData.getPlayer(target);
				userData.setMunny(userData.getMunny() - value);
				targetData.setMunny(targetData.getMunny() + value);
				user.sendSystemMessage(Component.translatable("You paid " + value + " munny to " + target.getDisplayName().getString()));
				target.sendSystemMessage(Component.translatable("You got " + value + " munny from " + user.getDisplayName().getString()));
			}
		} else {
			user.sendSystemMessage(Component.translatable("You don't have enough munny (" + value + ") to pay " + getPlayersString(players)));	
		}
		return 1;
	}

	public static String getPlayersString(Collection<ServerPlayer> players) {
		String line = "";
		for(Player p : players) {
			line += p.getDisplayName().getString()+", ";
		}
		return line.substring(0,line.length()-2);
	}
}
