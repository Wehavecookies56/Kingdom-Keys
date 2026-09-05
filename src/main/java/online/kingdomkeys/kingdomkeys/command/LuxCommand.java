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
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

import java.util.Collection;

// /kingdomkeys lux <set|add|remove> <amount> [players]
public class LuxCommand extends BaseCommand {

	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("lux").requires(source -> source.hasPermission(2));

		for (String action : new String[] { "set", "add", "remove" }) {
			builder.then(Commands.literal(action)
				.then(Commands.argument("value", IntegerArgumentType.integer(action.equals("set") ? 0 : 1, Integer.MAX_VALUE))
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
		int amount = IntegerArgumentType.getInteger(context, "value");
		String key = "kingdomkeys.command.lux." + action;

		for (ServerPlayer player : players) {
			PlayerData playerData = PlayerData.get(player);
			if (playerData == null) {
				continue;
			}

			int lux = switch (action) {
				case "add" -> playerData.getLux() + amount;
				case "remove" -> playerData.getLux() - amount;
				default -> amount;
			};

			playerData.setLux(lux);
			PacketHandler.sendTo(new SCSyncPlayerData(player), player);

			String who = player.getDisplayName().getString();
			context.getSource().sendSuccess(() -> action.equals("set") ? Component.translatable(key, who, amount) : Component.translatable(key, amount, who), true);

			player.sendSystemMessage(Component.translatable(key + "_self", amount));
		}

		return 1;
	}
}
