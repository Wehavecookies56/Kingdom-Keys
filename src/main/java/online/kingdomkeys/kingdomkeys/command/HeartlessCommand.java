package online.kingdomkeys.kingdomkeys.command;

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
import online.kingdomkeys.kingdomkeys.world.HeartlessForm;

// /kingdomkeys heartless <turn|restore> [targets]
public class HeartlessCommand extends BaseCommand {

	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("heartless").requires(source -> source.hasPermission(2));

		builder.then(Commands.literal("turn")
				.then(Commands.argument("targets", EntityArgument.players()).executes(context -> change(context, true)))
				.executes(context -> change(context, true)));

		builder.then(Commands.literal("restore")
				.then(Commands.argument("targets", EntityArgument.players()).executes(context -> change(context, false)))
				.executes(context -> change(context, false)));

		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
		return builder;
	}

	private static int change(CommandContext<CommandSourceStack> context, boolean turn) throws CommandSyntaxException {
		int changed = 0;

		for (ServerPlayer player : getPlayers(context)) {
			String who = player.getDisplayName().getString();
			boolean done = turn ? HeartlessForm.turn(player) : HeartlessForm.restore(player);

			if (done) {
				changed++;
				context.getSource().sendSuccess(() -> Component.translatable(turn ? "kingdomkeys.command.heartless.turned" : "kingdomkeys.command.heartless.restored", who), true);
			} else {
				context.getSource().sendFailure(Component.translatable(turn ? "kingdomkeys.command.heartless.already" : "kingdomkeys.command.heartless.not", who));
			}
		}

		return changed;
	}
}
