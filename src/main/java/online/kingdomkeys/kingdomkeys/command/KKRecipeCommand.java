package online.kingdomkeys.kingdomkeys.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Lists;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class KKRecipeCommand extends BaseCommand { /// kk_recipe <give/take> <recipe/all> [player]

	// private static final SuggestionProvider<CommandSource> SUGGEST_RECIPES =
	// (p_198296_0_, p_198296_1_) ->
	// ISuggestionProvider.suggestIterable(Lists.allRecipes, p_198296_1_);
	private static final SuggestionProvider<CommandSource> SUGGEST_RECIPES = (p_198296_0_, p_198296_1_) -> {
		List<String> list = new ArrayList<>();
		for (Recipe actual : RecipeRegistry.getInstance().getValues()) {
			list.add(actual.getRegistryName().toString());
		}
		return ISuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
	};

	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		LiteralArgumentBuilder<CommandSource> builder = Commands.literal("kk_recipe").requires(source -> source.hasPermissionLevel(3));

		builder.then(Commands.literal("give").then(Commands.argument("recipe", StringArgumentType.string()).suggests(SUGGEST_RECIPES).then(Commands.argument("targets", EntityArgument.players()).executes(KKRecipeCommand::addRecipe)).executes(KKRecipeCommand::addRecipe)).then(Commands.literal("all").then(Commands.argument("targets", EntityArgument.players()).executes(KKRecipeCommand::addAllRecipes)).executes(KKRecipeCommand::addAllRecipes)));

		builder.then(Commands.literal("take").then(Commands.argument("recipe", StringArgumentType.string()).suggests(SUGGEST_RECIPES).then(Commands.argument("targets", EntityArgument.players()).executes(KKRecipeCommand::removeRecipe)).executes(KKRecipeCommand::removeRecipe)).then(Commands.literal("all").then(Commands.argument("targets", EntityArgument.players()).executes(KKRecipeCommand::removeAllRecipes)).executes(KKRecipeCommand::removeAllRecipes))

		);

		dispatcher.register(builder);
		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
	}

	private static int addRecipe(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 3);
		String recipe = StringArgumentType.getString(context, "recipe");

		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.addKnownRecipe(new ResourceLocation(recipe));
			if (player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Added '" + Utils.translateToLocal(recipe) + "' recipe to " + player.getDisplayName().getString()), true);
			}
			player.sendMessage(new TranslationTextComponent("You have been given '" + Utils.translateToLocal(recipe) + "' recipe"),Util.DUMMY_UUID);
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
		}
		return 1;
	}

	private static int removeRecipe(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 3);
		String recipe = StringArgumentType.getString(context, "recipe");

		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.removeKnownRecipe(new ResourceLocation(recipe));
			if (player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Removed recipe '" + Utils.translateToLocal(recipe) + "' from " + player.getDisplayName().getString()), true);
			}
			player.sendMessage(new TranslationTextComponent("Your recipe '" + Utils.translateToLocal(recipe) + "' has been taken away"),Util.DUMMY_UUID);
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
		}
		return 1;
	}

	private static int addAllRecipes(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 3);

		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			for (Recipe actual : RecipeRegistry.getInstance().getValues()) {
				playerData.addKnownRecipe(actual.getRegistryName());
			}

			if (player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Added all recipes to " + player.getDisplayName().getString()), true);
			}
			player.sendMessage(new TranslationTextComponent("You have been given all the recipes"),Util.DUMMY_UUID);
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
		}
		return 1;
	}

	private static int removeAllRecipes(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 3);

		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.clearRecipes();

			if (player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Removed all recipes from " + player.getDisplayName().getString()), true);
			}
			player.sendMessage(new TranslationTextComponent("Your recipes have been taken away"),Util.DUMMY_UUID);
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
		}
		return 1;
	}

}
