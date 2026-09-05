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
import online.kingdomkeys.kingdomkeys.lib.Union;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

// /kingdomkeys union <unicornis|anguis|leopardos|vulpes|ursus>
public class UnionCommand extends BaseCommand {

	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		//Any player can run this since it's a patch for the unions change
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("union").requires(source -> source.hasPermission(0));

		for (Union union : Union.choosable()) {
			builder.then(Commands.literal(union.getSerializedName())
					// Admin part with a target player bypasses the no union check
					.then(Commands.argument("targets", EntityArgument.players())
					.requires(source -> source.hasPermission(2))
					.executes(context -> assign(context, union))
				)
				.executes(context -> apply(context, union))
			);
		}

		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
		return builder;
	}

	private static int apply(CommandContext<CommandSourceStack> context, Union union) throws CommandSyntaxException {
		ServerPlayer player = context.getSource().getPlayerOrException();
		PlayerData playerData = PlayerData.get(player);
		if (playerData == null) {
			return 0;
		}

		if (playerData.isOrgMember()) {
			context.getSource().sendFailure(Component.translatable("kingdomkeys.command.union.org"));
			return 0;
		}

		if (playerData.hasUnion()) { //Fail if the player already belongs to a union
			context.getSource().sendFailure(Component.translatable("kingdomkeys.command.union.already", Component.translatable(playerData.getUnion().getTranslationKey())));
			return 0;
		}

		playerData.setUnion(union);
		PacketHandler.syncToAllAround(player, playerData);

		context.getSource().sendSuccess(() -> Component.translatable("kingdomkeys.command.union.set", Component.translatable(union.getTranslationKey())), false);
		return 1;
	}

	// Admin part
	private static int assign(CommandContext<CommandSourceStack> context, Union union) throws CommandSyntaxException {
		int changed = 0;

		for (ServerPlayer player : EntityArgument.getPlayers(context, "targets")) {
			PlayerData playerData = PlayerData.get(player);
			if (playerData == null) {
				continue;
			}

			if (playerData.isOrgMember()) {
				context.getSource().sendFailure(Component.translatable("kingdomkeys.command.union.org_other", player.getDisplayName().getString()));
				continue;
			}

			playerData.setUnion(union);
			PacketHandler.syncToAllAround(player, playerData);

			context.getSource().sendSuccess(() -> Component.translatable("kingdomkeys.command.union.set_other", player.getDisplayName().getString(), Component.translatable(union.getTranslationKey())), true);
			player.sendSystemMessage(Component.translatable("kingdomkeys.command.union.set", Component.translatable(union.getTranslationKey())));
			changed++;
		}

		return changed;
	}
}
