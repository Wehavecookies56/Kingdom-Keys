package online.kingdomkeys.kingdomkeys.command;

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
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MagicLevelCommand extends BaseCommand{
//kk_ <give/take/set> <amount> [player]
	private static final SuggestionProvider<CommandSourceStack> SUGGEST_MAGICS = (p_198296_0_, p_198296_1_) -> {
		List<String> list = new ArrayList<>();
		for (ResourceLocation location : ModMagic.registry.get().getKeys()) {
			//if(!location.toString().equals(Strings.Form_Anti) && !location.toString().equals(DriveForm.NONE.toString()) && !location.toString().equals(DriveForm.SYNCH_BLADE.toString()))
			list.add(location.toString());
		}
		return SharedSuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
	};
	
	public static ArgumentBuilder<CommandSourceStack, ?> register() {
		
		LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("magiclevel").requires(source -> source.hasPermission(2));
		
		builder.then(Commands.literal("set")
			.then(Commands.argument("magic", StringArgumentType.string()).suggests(SUGGEST_MAGICS)
				.then(Commands.argument("level", IntegerArgumentType.integer(-1,10))
					.then(Commands.argument("targets", EntityArgument.players())
						.executes(MagicLevelCommand::setValue)
					)
					.executes(MagicLevelCommand::setValue)
				)
			)
		);
		
		KingdomKeys.LOGGER.warn("Registered command "+builder.getLiteral());
		return builder;
	}

	private static int setValue(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Collection<ServerPlayer> players = getPlayers(context, 5);
		int level = IntegerArgumentType.getInteger(context, "level");
		String magic = StringArgumentType.getString(context, "magic");
		
		Magic magicInstance = ModMagic.registry.get().getValue(new ResourceLocation(magic));
		if(magicInstance == null) {
			context.getSource().sendSuccess(() -> Component.translatable("Unknown magic '"+magic+"'"), true);
			return 1;
		}
		
		for (ServerPlayer player : players) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            
			if(level <= magicInstance.getMaxLevel()) {
				playerData.setMagicLevel(new ResourceLocation(magic), level, false);
			} else {
				context.getSource().sendSuccess(() -> Component.translatable("Level too high, max is '"+magicInstance.getMaxLevel()+"'"), true);
				return 1;
			}
			PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), player);
			
			String magicName = level > -1 ? Utils.translateToLocal(magicInstance.getTranslationKey(level)) : "N/A";
			context.getSource().sendSuccess(() -> Component.translatable("Set "+ Utils.translateToLocal(magicInstance.getTranslationKey())+" magic for " +player.getDisplayName().getString()+" to level "+level+" ("+magicName+")"), true);
			player.sendSystemMessage(Component.translatable("Your "+Utils.translateToLocal(magicInstance.getTranslationKey())+" magic level is now "+level+" ("+magicName+")"));
		}
		return 1;
	}
	
}
