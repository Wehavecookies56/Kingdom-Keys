package online.kingdomkeys.kingdomkeys.command;

import java.util.ArrayList;
import java.util.Collection;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

public class BaseCommand {
	/**
	 * 
	 * @param context
	 * @param numOfParams (has to be the same amounts of params (counting command) as when the function is called without target selector ("/munny set 500" is 3)
	 * @return
	 * @throws CommandSyntaxException
	 */
	public static Collection<ServerPlayerEntity> getPlayers(CommandContext<CommandSource> context, int numOfParams) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = new ArrayList<>();
		if(context.getInput().split(" ").length == numOfParams) {
			players.add(context.getSource().asPlayer());
		} else {
			players = EntityArgument.getPlayers(context, "targets");
		}
		return players;
	}
}
