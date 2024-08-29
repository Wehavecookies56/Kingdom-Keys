package online.kingdomkeys.kingdomkeys.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mojang.brigadier.arguments.IntegerArgumentType;
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
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class DriveLevelCommand extends BaseCommand{
//kk_ <give/take/set> <amount> [player]
	private static final SuggestionProvider<CommandSourceStack> SUGGEST_DRIVE_FORMS = (p_198296_0_, p_198296_1_) -> {
		List<String> list = new ArrayList<>();
		for (ResourceLocation location : ModDriveForms.registry.keySet()) {
			if(!location.toString().equals(Strings.Form_Anti) && !location.toString().equals(DriveForm.NONE.toString()) && !location.toString().equals(DriveForm.SYNCH_BLADE.toString()))
				list.add(location.toString());
		}
		return SharedSuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
	};
	
	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("drivelevel").requires(source -> source.hasPermission(2));
		
		builder.then(Commands.literal("set")
			.then(Commands.argument("form", StringArgumentType.string()).suggests(SUGGEST_DRIVE_FORMS)
				.then(Commands.argument("level", IntegerArgumentType.integer(0,7))
					.then(Commands.argument("targets", EntityArgument.players())
						.executes(DriveLevelCommand::setValue)
					)
					.executes(DriveLevelCommand::setValue)
				)
			)
		);
		
		KingdomKeys.LOGGER.warn("Registered command "+builder.getLiteral());
		return builder;
	}

	private static int setValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 5);
		int level = IntegerArgumentType.getInteger(context, "level");
		String form = StringArgumentType.getString(context, "form");
		
		for (ServerPlayer player : players) {
			IPlayerData playerData = ModData.getPlayer(player);
            
			if(level == 0) {
				playerData.setDriveFormLevel(form, 0);
				playerData.remVisibleDriveForm(form);
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), player);
			} else {
				playerData.setDriveFormLevel(form, 1);
				playerData.addVisibleDriveForm(form);
				playerData.setDriveFormExp(player, form, 0);
				DriveForm drive = ModDriveForms.registry.get(ResourceLocation.parse(form));
				playerData.setNewKeychain(ResourceLocation.parse(form), ItemStack.EMPTY);
				playerData.getAbilityMap().remove(drive.getBaseAbilityForLevel(3));
				
				while (playerData.getDriveFormLevel(form) < level) {
					int cost = drive.getLevelUpCost(playerData.getDriveFormLevel(form)+1);
					playerData.setDriveFormExp(player, form, cost);
				}
			}

			ExpCommand.fix(playerData, player); //Mainly here to remove given abilities in case form is going to be lower
			
			DriveForm formInstance = ModDriveForms.registry.get(ResourceLocation.parse(form));
			context.getSource().sendSuccess(() -> Component.translatable("Set "+ Utils.translateToLocal(formInstance.getTranslationKey())+" for " +player.getDisplayName().getString()+" to level "+level), true);
			player.sendSystemMessage(Component.translatable("Your "+Utils.translateToLocal(formInstance.getTranslationKey())+" level is now "+level));
		}
		return 1;
	}
	
}
