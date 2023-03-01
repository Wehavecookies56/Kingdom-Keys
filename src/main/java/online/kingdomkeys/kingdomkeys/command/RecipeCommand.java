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
import online.kingdomkeys.kingdomkeys.synthesis.recipe.Recipe;
import online.kingdomkeys.kingdomkeys.synthesis.recipe.RecipeRegistry;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class RecipeCommand extends BaseCommand { /// kk_recipe <give/take> <recipe/all> [player]
	private static final SuggestionProvider<CommandSourceStack> SUGGEST_RECIPES = (p_198296_0_, p_198296_1_) -> {
		List<String> list = new ArrayList<>();
		for (Recipe actual : RecipeRegistry.getInstance().getValues()) {
			list.add(actual.getRegistryName().toString());
		}
		return SharedSuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
	};

	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("recipe").requires(source -> source.hasPermission(2));

		builder.then(Commands.literal("give")
				.then(Commands.argument("recipe", StringArgumentType.string()).suggests(SUGGEST_RECIPES)
						.then(Commands.argument("targets", EntityArgument.players())
								.executes(RecipeCommand::addRecipe))
						.executes(RecipeCommand::addRecipe))
				.then(Commands.literal("all")
						.then(Commands.argument("targets", EntityArgument.players())
								.executes(RecipeCommand::addAllRecipes))
						.executes(RecipeCommand::addAllRecipes))
				.then(Commands.literal("keyblades")
						.then(Commands.argument("targets", EntityArgument.players())
								.executes(RecipeCommand::addAllKeybladeRecipes))
						.executes(RecipeCommand::addAllKeybladeRecipes))
				.then(Commands.literal("items")
						.then(Commands.argument("targets", EntityArgument.players())
								.executes(RecipeCommand::addAllItemRecipes))
						.executes(RecipeCommand::addAllItemRecipes))
				);
			

		builder.then(Commands.literal("take")
				.then(Commands.argument("recipe", StringArgumentType.string())
						.suggests(SUGGEST_RECIPES)
						.then(Commands.argument("targets", EntityArgument.players())
								.executes(RecipeCommand::removeRecipe))
						.executes(RecipeCommand::removeRecipe))
				.then(Commands.literal("all")
						.then(Commands.argument("targets", EntityArgument.players())
								.executes(RecipeCommand::removeAllRecipes))
						.executes(RecipeCommand::removeAllRecipes))
				.then(Commands.literal("keyblades")
						.then(Commands.argument("targets", EntityArgument.players())
								.executes(RecipeCommand::removeAllKeybladeRecipes))
						.executes(RecipeCommand::removeAllKeybladeRecipes))
				.then(Commands.literal("items")
						.then(Commands.argument("targets", EntityArgument.players())
								.executes(RecipeCommand::removeAllItemRecipes))
						.executes(RecipeCommand::removeAllItemRecipes))

		);

		KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
		return builder;
	}

	private static int addRecipe(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);
		String recipe = StringArgumentType.getString(context, "recipe");

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.addKnownRecipe(new ResourceLocation(recipe));
			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(Component.translatable("Added '" + Utils.translateToLocal(recipe) + "' recipe to " + player.getDisplayName().getString()), true);
			}
			player.sendSystemMessage(Component.translatable("You have been given '" + Utils.translateToLocal(recipe) + "' recipe"));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}

	private static int removeRecipe(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);
		String recipe = StringArgumentType.getString(context, "recipe");

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.removeKnownRecipe(new ResourceLocation(recipe));
			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(Component.translatable("Removed recipe '" + Utils.translateToLocal(recipe) + "' from " + player.getDisplayName().getString()), true);
			}
			player.sendSystemMessage(Component.translatable("Your recipe '" + Utils.translateToLocal(recipe) + "' has been taken away"));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}

	private static int addAllRecipes(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			for (Recipe actual : RecipeRegistry.getInstance().getValues()) {
				playerData.addKnownRecipe(actual.getRegistryName());
			}

			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(Component.translatable("Added all recipes to " + player.getDisplayName().getString()), true);
			}
			player.sendSystemMessage(Component.translatable("You have been given all the recipes"));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}
	
	private static int addAllKeybladeRecipes(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			for (Recipe actual : RecipeRegistry.getInstance().getValues()) {
				if(actual.getType().equals("keyblade"))
					playerData.addKnownRecipe(actual.getRegistryName());
			}

			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(Component.translatable("Added all keyblade recipes to " + player.getDisplayName().getString()), true);
			}
			player.sendSystemMessage(Component.translatable("You have been given all the keyblade recipes"));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}

	
	private static int addAllItemRecipes(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			for (Recipe actual : RecipeRegistry.getInstance().getValues()) {
				if(actual.getType().equals("item"))
					playerData.addKnownRecipe(actual.getRegistryName());
			}

			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(Component.translatable("Added all item recipes to " + player.getDisplayName().getString()), true);
			}
			player.sendSystemMessage(Component.translatable("You have been given all the item recipes"));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}


	private static int removeAllRecipes(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.clearRecipes("all");

			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(Component.translatable("Removed all recipes from " + player.getDisplayName().getString()), true);
			}
			player.sendSystemMessage(Component.translatable("Your recipes have been taken away"));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}
	
	private static int removeAllKeybladeRecipes(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			
			playerData.clearRecipes("keyblade");

			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(Component.translatable("Removed all keyblade recipes from " + player.getDisplayName().getString()), true);
			}
			player.sendSystemMessage(Component.translatable("Your keyblade recipes have been taken away"));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}
	
	private static int removeAllItemRecipes(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 4);

		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.clearRecipes("item");

			if (player != context.getSource().getPlayerOrException()) {
				context.getSource().sendSuccess(Component.translatable("Removed all item recipes from " + player.getDisplayName().getString()), true);
			}
			player.sendSystemMessage(Component.translatable("Your item recipes have been taken away"));
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
		}
		return 1;
	}

}
