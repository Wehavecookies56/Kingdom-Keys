package online.kingdomkeys.kingdomkeys.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Collection;

public class BaseCommand {
	/**
	 * 
	 * @param context
	 * @param numOfParams (has to be the same amounts of params (counting command) as when the function is called without target selector ("/kingdomkeys munny set 500" is 4)
	 * @return
	 * @throws CommandSyntaxException
	 */
	public static Collection<ServerPlayer> getPlayers(CommandContext<CommandSourceStack> context, int numOfParams) throws CommandSyntaxException {
		Collection<ServerPlayer> players = new ArrayList<>();
		if(context.getInput().split(" ").length == numOfParams) {
			players.add(context.getSource().getPlayerOrException());
		} else {
			players = EntityArgument.getPlayers(context, "targets");
		}
		return players;
	}
}
