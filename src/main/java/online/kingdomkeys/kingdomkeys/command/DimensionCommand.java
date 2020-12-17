package online.kingdomkeys.kingdomkeys.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.ModDimensions;
import online.kingdomkeys.kingdomkeys.world.utils.BaseTeleporter;

import java.util.Collection;

//TODO make this work for other dims, when we add them
public class DimensionCommand extends BaseCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("kk_dimension").requires(source -> source.hasPermissionLevel(3));

        builder.then(Commands.argument("targets", EntityArgument.players()).executes(DimensionCommand::changeDim));

        dispatcher.register(builder);
        KingdomKeys.LOGGER.warn("Registered command "+builder.getLiteral());
    }

    private static int changeDim(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = getPlayers(context, 1);
        players.forEach(player -> {
            player.changeDimension(ModDimensions.DIVE_TO_THE_HEART_TYPE, new BaseTeleporter(0, 26, 0));
        });
        return 1;
    }
}
