package online.kingdomkeys.kingdomkeys.command;

import java.util.Collection;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ExpCommand extends BaseCommand { // kk_exp <give/take/set> <amount> [player]
	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("exp").requires(source -> source.hasPermission(2));

		builder.then(Commands.literal("set").then(Commands.argument("exp", IntegerArgumentType.integer(1, Integer.MAX_VALUE)).then(Commands.argument("targets", EntityArgument.players()).executes(ExpCommand::setValue)).executes(ExpCommand::setValue)));

		builder.then(Commands.literal("give").then(Commands.argument("exp", IntegerArgumentType.integer(1, Integer.MAX_VALUE)).then(Commands.argument("targets", EntityArgument.players()).executes(ExpCommand::addValue)).executes(ExpCommand::addValue)));

		builder.then(Commands.literal("fix").then(Commands.argument("targets", EntityArgument.players()).executes(ExpCommand::fixValue)).executes(ExpCommand::fixValue));

		/*
		 * builder.then(Commands.literal("take") .then(Commands.argument("exp",
		 * IntegerArgumentType.integer(1,Integer.MAX_VALUE))
		 * .then(Commands.argument("targets", EntityArgument.players())
		 * .executes(KKExpCommand::removeValue) ) .executes(KKExpCommand::removeValue) )
		 * );
		 */

		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
		return builder;
	}

	private static int setValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);
		int exp = IntegerArgumentType.getInteger(context, "exp");

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            Utils.restartLevel(playerData, player);

			if(playerData.getSoAState() == SoAState.COMPLETE) {
				playerData.addExperience(player, exp, false, false);
				player.level.playSound((Player) null, player.blockPosition(), ModSounds.levelup.get(), SoundSource.MASTER, 1f, 1.0f);
			} else {
				context.getSource().sendSuccess(new TranslatableComponent(player.getDisplayName().getString() + " has to make a choice first"), true);
			}
            Utils.restartLevel2(playerData, player);			
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);

			context.getSource().sendSuccess(new TranslatableComponent("Set " + player.getDisplayName().getString() + " experience to " + exp), true);

			player.sendMessage(new TranslatableComponent("Your experience is now " + exp), Util.NIL_UUID);
		}
		return 1;

	}

	private static int addValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);
		int value = IntegerArgumentType.getInteger(context, "exp");

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.addExperience(player, value, false, false);
			player.level.playSound((Player) null, player.blockPosition(), ModSounds.levelup.get(), SoundSource.MASTER, 1f, 1.0f);

			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);

			context.getSource().sendSuccess(new TranslatableComponent("Added " + value + " experience to " + player.getDisplayName().getString()), true);

			player.sendMessage(new TranslatableComponent("Your experience has been increased by " + value), Util.NIL_UUID);
		}
		return 1;
	}

	// Sets player to level 1 and gives all his xp back
	private static int fixValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 3);
		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			int exp = playerData.getExperience();
			fix(playerData,player);
			player.level.playSound((Player) null, player.blockPosition(), ModSounds.levelup.get(), SoundSource.MASTER, 1f, 1.0f);
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
			context.getSource().sendSuccess(new TranslatableComponent("Set " + player.getDisplayName().getString() + " experience to " + exp), true);
			player.sendMessage(new TranslatableComponent("Your experience is now " + exp + ", all your missing abilities have been added to you"), Util.NIL_UUID);
		}
		return 1;
	}

	public static void fix(IPlayerCapabilities playerData, Player player) {
		int exp = playerData.getExperience();

		Utils.restartLevel(playerData, player);
		if(playerData.getSoAState() == SoAState.COMPLETE) {
			playerData.addExperience(player, exp, false, false);
		}
        Utils.restartLevel2(playerData, player);
		PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);

	}

}
