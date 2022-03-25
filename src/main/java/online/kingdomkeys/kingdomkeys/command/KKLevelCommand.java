package online.kingdomkeys.kingdomkeys.command;

import java.util.Collection;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.LevelStats;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class KKLevelCommand extends BaseCommand{ //kk_level <give/take/set> <amount> [player]
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("kk_level").requires(source -> source.hasPermission(2));
		
		builder.then(Commands.literal("set")
			.then(Commands.argument("level", IntegerArgumentType.integer(1,100))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(KKLevelCommand::setValue)
				)
				.executes(KKLevelCommand::setValue)
			)
		);
		
		/*builder.then(Commands.literal("give")
			.then(Commands.argument("value", IntegerArgumentType.integer(1,Integer.MAX_VALUE))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(KKLevelCommand::addValue)
				)
				.executes(KKLevelCommand::addValue)
			)
		);
		
		builder.then(Commands.literal("take")
			.then(Commands.argument("value", IntegerArgumentType.integer(1,Integer.MAX_VALUE))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(KKLevelCommand::removeValue)
				)
				.executes(KKLevelCommand::removeValue)
			)
		);*/
		
		dispatcher.register(builder);
		KingdomKeys.LOGGER.warn("Registered command "+builder.getLiteral());
	}

	private static int setValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 3);
		int level = IntegerArgumentType.getInteger(context, "level");
		
		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            Utils.restartLevel(playerData, player);
			
			if(playerData.getSoAState() == SoAState.COMPLETE) {
				while (playerData.getLevel() < level) {
					playerData.addExperience(player, playerData.getExpNeeded(level - 1, playerData.getExperience()), false, false);
				}
				context.getSource().sendSuccess(new TranslatableComponent("Set "+player.getDisplayName().getString()+" level to "+level), true);
				player.sendMessage(new TranslatableComponent("Your level is now "+level), Util.NIL_UUID);
				player.level.playSound((Player) null, player.blockPosition(), ModSounds.levelup.get(), SoundSource.MASTER, 1f, 1.0f);

			} else {
				context.getSource().sendSuccess(new TranslatableComponent(player.getDisplayName().getString() + " has to make a choice first"), true);
			}

            Utils.restartLevel2(playerData, player);			
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}
	

	private static int addValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 3);
		int value = IntegerArgumentType.getInteger(context, "value");
		
		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.setMunny(playerData.getMunny() + value);
			context.getSource().sendSuccess(new TranslatableComponent("Added "+value+" munny to "+player.getDisplayName().getString()), true);
			
			player.sendMessage(new TranslatableComponent("Your munny has been increased by "+value),Util.NIL_UUID);	
		}
		return 1;
	}
	
	
	private static int removeValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 3);
		int value = IntegerArgumentType.getInteger(context, "value");
		
		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.setMunny(playerData.getMunny() - value);
			context.getSource().sendSuccess(new TranslatableComponent("Taken "+value+" munny from "+player.getDisplayName().getString()), true);
			
			player.sendMessage(new TranslatableComponent("Your munny has been decreased by "+value),Util.NIL_UUID);
		}
		return 1;
	}
	
}
