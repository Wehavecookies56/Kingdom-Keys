package online.kingdomkeys.kingdomkeys.command;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class KKPayMunnyCommand extends BaseCommand { // kk_paymunny <player> <value>
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> builder = Commands.literal("kk_paymunny").requires(source -> source.hasPermissionLevel(0));
		
		builder.then(Commands.argument("targets", EntityArgument.players())
				.then(Commands.argument("value", IntegerArgumentType.integer(1, Integer.MAX_VALUE))
					.executes(KKPayMunnyCommand::payValue)));

		dispatcher.register(builder);
		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
	}

	private static int payValue(CommandContext<CommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity user = context.getSource().asPlayer();
		IPlayerCapabilities userData = ModCapabilities.getPlayer(user);

		Collection<ServerPlayerEntity> players = getPlayers(context, 3);
		int value = IntegerArgumentType.getInteger(context, "value");

		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

			if(userData.getMunny() - (value * players.size()) >= 0) {
				userData.setMunny(userData.getMunny() - value);
				playerData.setMunny(playerData.getMunny() + value);
				user.sendMessage(new TranslationTextComponent("You paid " + value + " munny to " + player.getDisplayName().getString()), Util.DUMMY_UUID);
				player.sendMessage(new TranslationTextComponent("You got " + value + " munny from " + player.getDisplayName().getString()), Util.DUMMY_UUID);
			} else {
				user.sendMessage(new TranslationTextComponent("You don't have enough munny (" + value + ") to pay " + player.getDisplayName().getString()), Util.DUMMY_UUID);
			}
		}
		return 1;
	}

}
