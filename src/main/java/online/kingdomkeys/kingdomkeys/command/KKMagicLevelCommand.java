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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class KKMagicLevelCommand extends BaseCommand{ 
//kk_ <give/take/set> <amount> [player]
	private static final SuggestionProvider<CommandSource> SUGGEST_MAGICS = (p_198296_0_, p_198296_1_) -> {
		List<String> list = new ArrayList<>();
		for (ResourceLocation location : ModMagic.registry.getKeys()) {
			//if(!location.toString().equals(Strings.Form_Anti) && !location.toString().equals(DriveForm.NONE.toString()) && !location.toString().equals(DriveForm.SYNCH_BLADE.toString()))
			list.add(location.toString());
		}
		return ISuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
	};
	
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		
		LiteralArgumentBuilder<CommandSource> builder = Commands.literal("kk_magiclevel").requires(source -> source.hasPermissionLevel(2));
		
		builder.then(Commands.literal("set")
			.then(Commands.argument("magic", StringArgumentType.string()).suggests(SUGGEST_MAGICS)
				.then(Commands.argument("level", IntegerArgumentType.integer(-1,10))
					.then(Commands.argument("targets", EntityArgument.players())
						.executes(KKMagicLevelCommand::setValue)
					)
					.executes(KKMagicLevelCommand::setValue)
				)
			)
		);
		
		dispatcher.register(builder);
		KingdomKeys.LOGGER.warn("Registered command "+builder.getLiteral());
	}

	private static int setValue(CommandContext<CommandSource> context) throws CommandSyntaxException {
		Collection<ServerPlayerEntity> players = getPlayers(context, 4);
		int level = IntegerArgumentType.getInteger(context, "level");
		String magic = StringArgumentType.getString(context, "magic");
		
		Magic magicInstance = ModMagic.registry.getValue(new ResourceLocation(magic));
		if(magicInstance == null) {
			context.getSource().sendFeedback(new TranslationTextComponent("Unknown magic '"+magic+"'"), true);
			return 1;
		}
		
		for (ServerPlayerEntity player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            
			if(level <= magicInstance.getMaxLevel()) {
				playerData.setMagicLevel(magic, level);
			} else {
				context.getSource().sendFeedback(new TranslationTextComponent("Level too high, max is '"+magicInstance.getMaxLevel()+"'"), true);
				return 1;
			}
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), player);
			
			String magicName = level > -1 ? Utils.translateToLocal(magicInstance.getTranslationKey(level)) : "N/A";
			context.getSource().sendFeedback(new TranslationTextComponent("Set "+ Utils.translateToLocal(magicInstance.getTranslationKey())+" magic for " +player.getDisplayName().getString()+" to level "+level+" ("+magicName+")"), true);
			player.sendMessage(new TranslationTextComponent("Your "+Utils.translateToLocal(magicInstance.getTranslationKey())+" magic level is now "+level+" ("+magicName+")"), Util.DUMMY_UUID);
		}
		return 1;
	}
	
}
