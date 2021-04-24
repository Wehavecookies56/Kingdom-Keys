package online.kingdomkeys.kingdomkeys.command;

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
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class KKDriveLevelCommand extends BaseCommand{ 
//kk_level <give/take/set> <amount> [player]
	private static final SuggestionProvider<CommandSource> SUGGEST_DRIVE_FORMS = (p_198296_0_, p_198296_1_) -> {
		List<String> list = new ArrayList<>();
		for (ResourceLocation location : ModDriveForms.registry.getKeys()) {
			if(!location.getPath().equals("form_anti"))
				list.add(location.toString());
		}
		return ISuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
	};
	
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		
		LiteralArgumentBuilder<CommandSource> builder = Commands.literal("kk_drivelevel").requires(source -> source.hasPermissionLevel(2));
		
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

			DriveForm formInstance = ModDriveForms.registry.getValue(new ResourceLocation(form));
			
			
				context.getSource().sendFeedback(new TranslationTextComponent("Set "+ Utils.translateToLocal(formInstance.getTranslationKey())+" for " +player.getDisplayName().getString()+" to level "+level), true);
			
			player.sendMessage(new TranslationTextComponent("Your "+Utils.translateToLocal(formInstance.getTranslationKey())+" level is now "+level), Util.DUMMY_UUID);
		}
		return 1;
	}
	
}
