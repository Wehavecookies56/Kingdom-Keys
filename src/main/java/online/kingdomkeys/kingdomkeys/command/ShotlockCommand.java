package online.kingdomkeys.kingdomkeys.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;
import online.kingdomkeys.kingdomkeys.shotlock.Shotlock;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ShotlockCommand extends BaseCommand { /// kingdomkeys shotlock <give/take> <shotlock> [player]
	private static final SuggestionProvider<CommandSourceStack> SUGGEST_SHOTLOCKS = (p_198296_0_, p_198296_1_) -> {
		List<String> list = new ArrayList<>();
		for (ResourceLocation actual : ModShotlocks.registry.get().getKeys()) {
			list.add(actual.toString());
		}
		return SharedSuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
	};

	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("shotlock").requires(source -> source.hasPermission(2));

		builder.then(Commands.literal("give")
				.then(Commands.argument("shotlock", StringArgumentType.string()).suggests(SUGGEST_SHOTLOCKS)
						.then(Commands.argument("targets", EntityArgument.players())
								.executes(ShotlockCommand::addShotlock))
						.executes(ShotlockCommand::addShotlock)) );

		builder.then(Commands.literal("take")
				.then(Commands.argument("shotlock", StringArgumentType.string())
						.suggests(SUGGEST_SHOTLOCKS)
						.then(Commands.argument("targets", EntityArgument.players())
								.executes(ShotlockCommand::removeShotlock))
						.executes(ShotlockCommand::removeShotlock))
				.then(Commands.literal("all")
						.then(Commands.argument("targets", EntityArgument.players())
								.executes(ShotlockCommand::removeAllShotlocks))
						.executes(ShotlockCommand::removeAllShotlocks))

		);

		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
		return builder;
	}

	private static int addShotlock(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);
		String shotlockName = StringArgumentType.getString(context, "shotlock");

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			Shotlock a = ModShotlocks.registry.get().getValue(new ResourceLocation(shotlockName));
			playerData.addShotlockToList(shotlockName, true);
			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(() -> Component.translatable("Added '" + Utils.translateToLocal(a.getTranslationKey()) + "' shotlock to " + player.getDisplayName().getString()), true);
			}
			player.sendSystemMessage(Component.translatable("You have been given the shotlock '" + Utils.translateToLocal(a.getTranslationKey()) + "'"));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}

	private static int removeShotlock(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);
		String shotlock = StringArgumentType.getString(context, "shotlock");

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.removeShotlockFromList(shotlock);
			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(() -> Component.translatable("Removed shotlock '" + Utils.translateToLocal(shotlock) + "' from " + player.getDisplayName().getString()), true);
			}
			Shotlock a = ModShotlocks.registry.get().getValue(new ResourceLocation(shotlock));
			player.sendSystemMessage(Component.translatable("Your shotlock '" + Utils.translateToLocal(a.getTranslationKey()) + "' has been taken away"));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}

	private static int removeAllShotlocks(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.getShotlockList().clear();

			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(() -> Component.translatable("Removed all shotlocks from " + player.getDisplayName().getString()), true);
			}
			player.sendSystemMessage(Component.translatable("Your shotlocks have been taken away"));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}

}
