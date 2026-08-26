package online.kingdomkeys.kingdomkeys.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.List;

public class BaseCommand {

	/**
	 * Whoever the command is aimed at, or whoever ran it if no targets were given.
	 *
	 * <p>This asks the parsed command whether the argument is there rather than counting the words that
	 * were typed, which is how it used to be done. Counting breaks the moment any argument before the
	 * targets becomes optional, and it counted the name of the command too, so an alias of a different
	 * length would have thrown it off as well.
	 */
	public static Collection<ServerPlayer> getPlayers(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		if (!hasArgument(context, "targets")) {
			return List.of(context.getSource().getPlayerOrException());
		}

		return EntityArgument.getPlayers(context, "targets");
	}

	/** Whether an optional argument was actually typed */
	public static boolean hasArgument(CommandContext<CommandSourceStack> context, String name) {
		try {
			context.getArgument(name, Object.class);
			return true;
		} catch (IllegalArgumentException missing) {
			return false;
		}
	}
}
