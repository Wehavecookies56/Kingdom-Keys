package online.kingdomkeys.kingdomkeys.command;

import com.mojang.brigadier.arguments.BoolArgumentType;
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
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AbilityCommand extends BaseCommand { // kingdomkeys ability <give/take> <ability> <permanent> [player]
	private static final SuggestionProvider<CommandSourceStack> SUGGEST_ABILITIES = (p_198296_0_, p_198296_1_) -> {
		List<String> list = new ArrayList<>();
		for (ResourceLocation actual : ModAbilities.registry.keySet()) {
			list.add(actual.toString());
		}
		return SharedSuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
	};

	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("ability").requires(source -> source.hasPermission(2));

		builder.then(Commands.literal("give")
			.then(Commands.argument("ability", StringArgumentType.string()).suggests(SUGGEST_ABILITIES)
				.then(Commands.argument("permanent", BoolArgumentType.bool())
					.executes(AbilityCommand::addAbility)
					.then(Commands.argument("targets", EntityArgument.players())
						.executes(AbilityCommand::addAbility))
				)
			)
		);

		builder.then(Commands.literal("take")
			.then(Commands.argument("ability", StringArgumentType.string())
				.suggests(SUGGEST_ABILITIES)
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(AbilityCommand::removeAbility))
				.executes(AbilityCommand::removeAbility))
			.then(Commands.literal("all")
				.then(Commands.argument("targets", EntityArgument.players())
					.executes(AbilityCommand::removeAllAbilities))
				.executes(AbilityCommand::removeAllAbilities))
		);

		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
		return builder;
	}

	private static int addAbility(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 5);
		String abilityName = StringArgumentType.getString(context, "ability");
		boolean permanent = BoolArgumentType.getBool(context, "permanent");

		Ability a = ModAbilities.registry.get(KingdomKeys.rl(abilityName));
		if(a == null) {
			context.getSource().sendFailure(Component.literal("Ability '"+abilityName+ "' does not exist"));
			return 0;
		}

		for (ServerPlayer player : players) {
			PlayerData playerData = PlayerData.get(player);
			if (permanent) {
				playerData.addPAbility(a.getRegistryName());
				player.sendSystemMessage(Component.translatable("You have been given the ability '" + Utils.translateToLocal(a.getTranslationKey()) + "' permanently"));
			} else {
				playerData.addAbility(a.getRegistryName(), true);
				player.sendSystemMessage(Component.translatable("You have been given the ability '" + Utils.translateToLocal(a.getTranslationKey()) + "'"));
			}
			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(() -> Component.translatable("Added '" + Utils.translateToLocal(a.getTranslationKey()) + "' ability to " + player.getDisplayName().getString()), true);
			}
			PacketHandler.sendTo(new SCSyncPlayerData(player), player);
		}
		return 1;
	}

	private static int removeAbility(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);
		String ability = StringArgumentType.getString(context, "ability");

		for (ServerPlayer player : players) {
			PlayerData playerData = PlayerData.get(player);
			playerData.removePAbility(KingdomKeys.rl(ability));
			playerData.removeAbility(KingdomKeys.rl(ability));

			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(() -> Component.translatable("Removed ability '" + Utils.translateToLocal(ability) + "' from " + player.getDisplayName().getString()), true);
			}
			Ability a = ModAbilities.registry.get(KingdomKeys.rl(ability));
			player.sendSystemMessage(Component.translatable("Your ability '" + Utils.translateToLocal(a.getTranslationKey()) + "' has been taken away"));
			PacketHandler.sendTo(new SCSyncPlayerData(player), player);
		}
		return 1;
	}

	private static int removeAllAbilities(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);

		for (ServerPlayer player : players) {
			PlayerData playerData = PlayerData.get(player);
			playerData.clearAbilities();

			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(() -> Component.translatable("Removed all abilities from " + player.getDisplayName().getString()), true);
			}
			player.sendSystemMessage(Component.translatable("Your abilities have been taken away"));
			PacketHandler.sendTo(new SCSyncPlayerData(player), player);
		}
		return 1;
	}

}
