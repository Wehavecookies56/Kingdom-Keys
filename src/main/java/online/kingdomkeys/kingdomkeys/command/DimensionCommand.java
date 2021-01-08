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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

//TODO make this work for other dims, when we add them
public class DimensionCommand extends BaseCommand {
	
	private static final SuggestionProvider<CommandSource> SUGGEST_DIMENSIONS = (p_198296_0_, p_198296_1_) -> {
		List<String> list = new ArrayList<>();
		list.add("overworld");
		for (RegistryObject<ModDimension> location : ModDimensions.DIMENSIONS.getEntries()) {
			list.add(location.get().getRegistryName().toString());
		}
		return ISuggestionProvider.suggest(list.stream().map(StringArgumentType::escapeIfRequired), p_198296_1_);
	};


    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("kk_dimension").requires(source -> source.hasPermissionLevel(3));

        builder.then(Commands.argument("dim", StringArgumentType.string()).suggests(SUGGEST_DIMENSIONS)
	        .then(Commands.argument("targets", EntityArgument.players())
	        		.executes(DimensionCommand::changeDim)
	        	)
			.executes(DimensionCommand::changeDim)
        );

        dispatcher.register(builder);
        KingdomKeys.LOGGER.warn("Registered command "+builder.getLiteral());
    }

    private static int changeDim(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = getPlayers(context, 2);
		String dim = StringArgumentType.getString(context, "dim");
		DimensionType dimension = DimensionType.byName(new ResourceLocation(dim));
		
		if(dimension == null) {
			context.getSource().sendFeedback(new TranslationTextComponent("Invalid dimension "+dim), true);
			return 1;
		}
		for (ServerPlayerEntity player : players) {
        	BlockPos coords = getWorldCoords(player,dimension);
            player.changeDimension(dimension, new BaseTeleporter(coords.getX(), coords.getY(), coords.getZ()));
			
			if(player != context.getSource().asPlayer()) {
				context.getSource().sendFeedback(new TranslationTextComponent("Teleported" +player.getDisplayName().getString()+" to dimension "+dimension.getRegistryName().toString()), true);
			}
			player.sendMessage(new TranslationTextComponent("You have been teleported to "+dimension.getRegistryName().toString()), Util.DUMMY_UUID);
        }
        return 1;
    }
    
    private static BlockPos getWorldCoords(PlayerEntity player, DimensionType dimension) {
    	if(dimension == ModDimensions.DIVE_TO_THE_HEART_TYPE) {
    		return new BlockPos(0,26,0);
    	}
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
    	return new BlockPos(playerData.getReturnLocation());
    }
}
