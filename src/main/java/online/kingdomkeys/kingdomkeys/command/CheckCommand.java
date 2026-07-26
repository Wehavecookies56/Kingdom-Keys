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
                        .executes(CheckCommand::checkPlayer))
                .executes(CheckCommand::checkPlayer);
        KingdomKeys.LOGGER.warn("Registered command " + builder.getLiteral());
        return builder;
    }

    private static int checkPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<ServerPlayer> players = getPlayers(context, 2);
        if(context.getSource().getEntity() instanceof ServerPlayer sender){
            Player target = players.stream().findFirst().orElse(null);
            if(target == null) {
                context.getSource().sendFailure(Component.translatable("kingdomkeys.command.check.player_not_found", players));
                return 0;
            }
            PlayerData playerData = PlayerData.get(target);
            if(playerData == null) {
                context.getSource().sendFailure(Component.translatable("kingdomkeys.command.check.data_null", target.getName().getString()));
                return 0;
            }

            context.getSource().sendSuccess(() -> Component.translatable("kingdomkeys.command.check.checking", target.getName().getString()), true);
            SCOpenCheckScreen packet = new SCOpenCheckScreen(playerData, target);
            KingdomKeys.LOGGER.warn("[DEBUG check] serialized NBT has {} keys: {}", packet.playerData().size(), packet.playerData().getAllKeys());
            PacketHandler.sendTo(packet, sender);
        } else {
            context.getSource().sendFailure(Component.translatable("kingdomkeys.command.player_only"));
        }
        return 1;
    }
}