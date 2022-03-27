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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class KKExpCommand extends BaseCommand { // kk_exp <give/take/set> <amount> [player]
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> builder = Commands.literal("kk_exp").requires(source -> source.hasPermissionLevel(2));

		builder.then(Commands.literal("set").then(Commands.argument("exp", IntegerArgumentType.integer(1, Integer.MAX_VALUE)).then(Commands.argument("targets", EntityArgument.players()).executes(KKExpCommand::setValue)).executes(KKExpCommand::setValue)));

		builder.then(Commands.literal("give").then(Commands.argument("exp", IntegerArgumentType.integer(1, Integer.MAX_VALUE)).then(Commands.argument("targets", EntityArgument.players()).executes(KKExpCommand::addValue)).executes(KKExpCommand::addValue)));

		builder.then(Commands.literal("fix").then(Commands.argument("targets", EntityArgument.players()).executes(KKExpCommand::fixValue)).executes(KKExpCommand::fixValue));

		/*
		 * builder.then(Commands.literal("take") .then(Commands.argument("exp",
		 * IntegerArgumentType.integer(1,Integer.MAX_VALUE))
		 * .then(Commands.argument("targets", EntityArgument.players())
		 * .executes(KKExpCommand::removeValue) ) .executes(KKExpCommand::removeValue) )
		 * );
		 */

		dispatcher.register(builder);
		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
	}

	private static int setValue(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 3);
		int exp = IntegerArgumentType.getInteger(context, "exp");

		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            Utils.restartLevel(playerData, player);

			if(playerData.getSoAState() == SoAState.COMPLETE) {
				playerData.addExperience(player, exp, false, false);
				player.world.playSound((PlayerEntity) null, player.getPosition(), ModSounds.levelup.get(), SoundCategory.MASTER, 1f, 1.0f);
			} else {
				context.getSource().sendFeedback(new TranslationTextComponent(player.getDisplayName().getString() + " has to make a choice first"), true);
			}
            Utils.restartLevel2(playerData, player);			
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);

			context.getSource().sendFeedback(new TranslationTextComponent("Set " + player.getDisplayName().getString() + " experience to " + exp), true);

			player.sendMessage(new TranslationTextComponent("Your experience is now " + exp), Util.DUMMY_UUID);
		}
		return 1;

	}

	private static int addValue(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 3);
		int value = IntegerArgumentType.getInteger(context, "exp");

		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.addExperience(player, value, false, false);
			player.world.playSound((PlayerEntity) null, player.getPosition(), ModSounds.levelup.get(), SoundCategory.MASTER, 1f, 1.0f);

			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);

			context.getSource().sendFeedback(new TranslationTextComponent("Added " + value + " experience to " + player.getDisplayName().getString()), true);

			player.sendMessage(new TranslationTextComponent("Your experience has been increased by " + value), Util.DUMMY_UUID);
		}
		return 1;
	}

	// Sets player to level 1 and gives all his xp back
	private static int fixValue(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 2);
		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			int exp = playerData.getExperience();
			fix(playerData,player);
			player.world.playSound((PlayerEntity) null, player.getPosition(), ModSounds.levelup.get(), SoundCategory.MASTER, 1f, 1.0f);
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
			context.getSource().sendFeedback(new TranslationTextComponent("Set " + player.getDisplayName().getString() + " experience to " + exp), true);
			player.sendMessage(new TranslationTextComponent("Your experience is now " + exp + ", all your missing abilities have been added to you"), Util.DUMMY_UUID);
		}
		return 1;
	}

	public static void fix(IPlayerCapabilities playerData, PlayerEntity player) {
		int exp = playerData.getExperience();

		Utils.restartLevel(playerData, player);
		if(playerData.getSoAState() == SoAState.COMPLETE) {
			playerData.addExperience(player, exp, false, false);
		}
        Utils.restartLevel2(playerData, player);
		PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);

	}

}
