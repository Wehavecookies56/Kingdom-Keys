package online.kingdomkeys.kingdomkeys.command;

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
import net.minecraft.util.Mth;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

import java.util.Collection;

// /kingdomkeys synthlevel <set|add|remove> <amount> [players]
public class SynthLevelCommand extends BaseCommand {
	private static final int MAX = 7;
	private static final int MIN = 1;

	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("synthlevel").requires(source -> source.hasPermission(2));

		for (String action : new String[] { "set", "add", "remove" }) {
			builder.then(Commands.literal(action)
				.then(Commands.argument("level", IntegerArgumentType.integer(action.equals("set") ? MIN : -MAX, MAX))
					.then(Commands.argument("targets", EntityArgument.players())
						.executes(context -> apply(context, action))
					)
					.executes(context -> apply(context, action))
				)
			);
		}

		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
		return builder;
	}

	private static int apply(CommandContext<CommandSourceStack> context, String action) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context);
		int amount = IntegerArgumentType.getInteger(context, "level");

		for (ServerPlayer player : players) {
			PlayerData playerData = PlayerData.get(player);
			if (playerData == null) {
				continue;
			}

			int level = Mth.clamp(switch (action) {
				case "add" -> playerData.getSynthLevel() + amount;
				case "remove" -> playerData.getSynthLevel() - amount;
				default -> amount;
			}, MIN, MAX);

			playerData.setSynthLevel(level);
			playerData.setSynthExperience(PlayerData.getSynthExpForLevel(level - 1));
			PacketHandler.sendTo(new SCSyncPlayerData(player), player);

			context.getSource().sendSuccess(() -> Component.translatable("kingdomkeys.command.synthlevel.set", player.getDisplayName().getString(), level), true);
			player.sendSystemMessage(Component.translatable("kingdomkeys.command.synthlevel.set_self", level));
		}

		return 1;
	}

}
