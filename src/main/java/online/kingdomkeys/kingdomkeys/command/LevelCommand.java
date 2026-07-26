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
import net.minecraft.sounds.SoundSource;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.SoAState;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.Collection;

public class LevelCommand extends BaseCommand{ //kk_level <give/take/set> <amount> [player]
	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("level").requires(source -> source.hasPermission(2));
		
		builder.then(Commands.literal("set")
			.then(Commands.argument("level", IntegerArgumentType.integer(1,100))
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(LevelCommand::setValue)
				)
				.executes(LevelCommand::setValue)
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
		
		KingdomKeys.LOGGER.warn("Registered command "+builder.getLiteral());
		return builder;
	}

	private static int setValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);
		int level = IntegerArgumentType.getInteger(context, "level");
		
		for (ServerPlayer player : players) {
			PlayerData playerData = PlayerData.get(player);
            Utils.restartLevel(playerData, player);
			
            // Set the level
			if(playerData.getSoAState() == SoAState.COMPLETE) {
				while (playerData.getLevel() < level) {
					playerData.addExperience(player, playerData.getExpNeeded(level - 1, playerData.getExperience()), false, false);
				}
				context.getSource().sendSuccess(() -> Component.translatable("kingdomkeys.command.level.set", player.getDisplayName().getString(), level), true);
				player.sendSystemMessage(Component.translatable("kingdomkeys.command.level.set_self", level));
				player.level().playSound(null, player.blockPosition(), ModSounds.levelup.get(), SoundSource.MASTER, 1f, 1.0f);

			} else {
				context.getSource().sendSuccess(() -> Component.translatable("kingdomkeys.command.no_choice", player.getDisplayName().getString()), true);
			}

            Utils.restartLevel2(playerData, player);			
			PacketHandler.sendTo(new SCSyncPlayerData(player), player);
		}
		return 1;
	}
	
}
