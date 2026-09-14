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
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.story.ForetellerVisit;
import online.kingdomkeys.kingdomkeys.story.StoryFlags;

import java.util.Collection;

/**
 * Story beats on demand, because the alternative is waiting for dawn every time you change a line.
 *
 * <p>{@code /kk story visit} puts the master in front of you now. {@code reset} forgets he ever came,
 * so the next keyblade starts the chain over.</p>
 */
public class StoryCommand extends BaseCommand {

	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("story").requires(source -> source.hasPermission(2));

		builder.then(Commands.literal("visit")
				.then(Commands.argument("targets", EntityArgument.players()).executes(StoryCommand::visit))
				.executes(StoryCommand::visit));

		builder.then(Commands.literal("reset")
				.then(Commands.argument("targets", EntityArgument.players()).executes(StoryCommand::reset))
				.executes(StoryCommand::reset));

		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
		return builder;
	}

	private static int visit(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		for (ServerPlayer player : getPlayers(context)) {
			PlayerData data = PlayerData.get(player);

			if (data == null) {
				continue;
			}

			String who = player.getDisplayName().getString();

			if (!data.hasUnion()) {
				context.getSource().sendFailure(Component.translatable("kingdomkeys.command.story.no_union", who));
				continue;
			}

			if (ForetellerVisit.force(player, data)) {
				context.getSource().sendSuccess(() -> Component.translatable("kingdomkeys.command.story.visit", who), true);
			} else {
				context.getSource().sendFailure(Component.translatable("kingdomkeys.command.story.no_room", who));
			}
		}

		return 1;
	}

	private static int reset(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context);

		for (ServerPlayer player : players) {
			PlayerData data = PlayerData.get(player);

			if (data == null) {
				continue;
			}

			data.removeFlag(StoryFlags.FORETELLER_OWED);
			data.removeFlag(StoryFlags.FORETELLER_VISITED);
			data.removeFlag(StoryFlags.INTRODUCTORY_TRAINING_DONE);
			PacketHandler.sendTo(new SCSyncPlayerData(player), player);

			String who = player.getDisplayName().getString();
			context.getSource().sendSuccess(() -> Component.translatable("kingdomkeys.command.story.reset", who), true);
		}

		return 1;
	}
}
