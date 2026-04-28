package online.kingdomkeys.kingdomkeys.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenCheckScreen;

import java.util.Collection;

public class CheckCommand extends BaseCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("check").requires(source -> source.hasPermission(2));
        builder.then(Commands.argument("targets", EntityArgument.players())
                        .executes(CheckCommand::makeChoice))
                .executes(CheckCommand::makeChoice);
        KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
        return builder;
    }

    private static int makeChoice(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> players = getPlayers(context, 2);
        if(context.getSource().getEntity() instanceof ServerPlayer sender){
            Player target = players.stream().findFirst().orElse(null);
            if(target == null) {
                context.getSource().sendSuccess(() -> Component.literal("Player not found "+ players), true);
                return 0;
            }
            PlayerData playerData = PlayerData.get(target);
            if(playerData == null) {
                context.getSource().sendSuccess(() -> Component.literal("PlayerData seems null for player "+ target.getName().getString()), true);
                return 0;
            }
            PacketHandler.sendTo(new SCOpenCheckScreen(playerData, target), sender);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("Command must be run by a player"), true);
        }
        return 1;
    }
}
