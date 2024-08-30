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
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class AbilityCommand extends BaseCommand { /// kk_ability <give/take> <ability> [player]
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
						.then(Commands.argument("targets", EntityArgument.players())
								.executes(AbilityCommand::addAbility))
						.executes(AbilityCommand::addAbility)) );
				/*.then(Commands.literal("all")
						.then(Commands.argument("targets", EntityArgument.players())
								.executes(KKAbilityCommand::addAllRecipes))
						.executes(KKAbilityCommand::addAllRecipes)));*/

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
		Collection<ServerPlayer> players = getPlayers(context, 4);
		String abilityName = StringArgumentType.getString(context, "ability");

		for (ServerPlayer player : players) {
			IPlayerData playerData = ModData.getPlayer(player);
			Ability a = ModAbilities.registry.get(ResourceLocation.parse(abilityName));
			playerData.addAbility(abilityName, true);
			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(() -> Component.translatable("Added '" + Utils.translateToLocal(a.getTranslationKey()) + "' ability to " + player.getDisplayName().getString()), true);
			}
			player.sendSystemMessage(Component.translatable("You have been given the ability '" + Utils.translateToLocal(a.getTranslationKey()) + "'"));
			PacketHandler.sendTo(new SCSyncPlayerData(playerData), (ServerPlayer) player);
		}
		return 1;
	}

	private static int removeAbility(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);
		String ability = StringArgumentType.getString(context, "ability");

		for (ServerPlayer player : players) {
			IPlayerData playerData = ModData.getPlayer(player);
			playerData.removeAbility(ability);
			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(() -> Component.translatable("Removed ability '" + Utils.translateToLocal(ability) + "' from " + player.getDisplayName().getString()), true);
			}
			Ability a = ModAbilities.registry.get(ResourceLocation.parse(ability));
			player.sendSystemMessage(Component.translatable("Your ability '" + Utils.translateToLocal(a.getTranslationKey()) + "' has been taken away"));
			PacketHandler.sendTo(new SCSyncPlayerData(playerData), (ServerPlayer) player);
		}
		return 1;
	}

	/*private static int addAllRecipes(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 3);

		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			for (Recipe actual : RecipeRegistry.getInstance().getValues()) {
				playerData.addKnownRecipe(actual.getRegistryName());
			}

			if (player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Added all recipes to " + player.getDisplayName().getString()), true);
			}
			player.sendSystemMessage(new TranslationTextComponent("You have been given all the recipes"),Util.DUMMY_UUID);
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
		}
		return 1;
	}*/

	private static int removeAllAbilities(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);

		for (ServerPlayer player : players) {
			IPlayerData playerData = ModData.getPlayer(player);
			playerData.clearAbilities();

			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(() -> Component.translatable("Removed all abilities from " + player.getDisplayName().getString()), true);
			}
			player.sendSystemMessage(Component.translatable("Your abilities have been taken away"));
			PacketHandler.sendTo(new SCSyncPlayerData(playerData), (ServerPlayer) player);
		}
		return 1;
	}

}
