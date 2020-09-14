package online.kingdomkeys.kingdomkeys.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;

public class KKDriveLevelCommand extends BaseCommand{ 
//kklevel <give/take/set> <amount> [player]
	private static final SuggestionProvider<CommandSource> SUGGEST_DRIVE_FORMS = (p_198296_0_, p_198296_1_) -> {
		List<String> list = new ArrayList<>();
		for (ResourceLocation location : ModDriveForms.registry.getKeys()) {
			list.add(location.toString());
		}
		return ISuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
	};
	
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		
		LiteralArgumentBuilder<CommandSource> builder = Commands.literal("kkdrivelevel").requires(source -> source.hasPermissionLevel(3));
		
		builder.then(Commands.literal("set")
			.then(Commands.argument("form", StringArgumentType.string()).suggests(SUGGEST_DRIVE_FORMS)
				.then(Commands.argument("level", IntegerArgumentType.integer(1,7))
					.then(Commands.argument("targets", EntityArgument.players())
						.executes(KKDriveLevelCommand::setValue)
					)
					.executes(KKDriveLevelCommand::setValue)
				)
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

	private static int setValue(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 4);
		int level = IntegerArgumentType.getInteger(context, "level");
		String form = StringArgumentType.getString(context, "form");
		
		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            
			playerData.setDriveFormLevel(form, 1);
			playerData.setDriveFormExp(player, form, 0);
			DriveForm drive = ModDriveForms.registry.getValue(new ResourceLocation(form));
			playerData.setNewKeychain(new ResourceLocation(form), ItemStack.EMPTY);
			playerData.getAbilityMap().remove(drive.getBaseAbilityForLevel(3));
			
			while (playerData.getDriveFormLevel(form) < level) {
				int cost = drive.getLevelUpCost(playerData.getDriveFormLevel(form)+1);
				playerData.setDriveFormExp(player, form, cost);
			}
			
			if(player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Set "+form+" for " +player.getDisplayName().getFormattedText()+" to level "+level), true);
			}
			player.sendMessage(new TranslationTextComponent("Your "+form+" level is now "+level));
		}
		return 1;
	}
	

	private static int addValue(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 3);
		int value = IntegerArgumentType.getInteger(context, "value");
		
		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.setMunny(playerData.getMunny() + value);
			if(player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Added "+value+" munny to "+player.getDisplayName().getFormattedText()), true);
			}
			player.sendMessage(new TranslationTextComponent("Your munny has been increased by "+value));		}
		return 1;
	}
	
	
	private static int removeValue(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 3);
		int value = IntegerArgumentType.getInteger(context, "value");
		
		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.setMunny(playerData.getMunny() - value);
			if(player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Taken "+value+" munny from "+player.getDisplayName().getFormattedText()), true);
			}
			player.sendMessage(new TranslationTextComponent("Your munny has been decreased by "+value));
		}
		return 1;
	}
	
}
