package online.kingdomkeys.kingdomkeys.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommands {

    public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("kingdomkeys")
                .then(AbilityCommand.register())
                .then(DimensionCommand.register())
                .then(DriveLevelCommand.register())
                .then(DrivePointsCommand.register())
                .then(ExpCommand.register())
                .then(FocusPointsCommand.register())
                .then(HeartsCommand.register())
                .then(LevelCommand.register())
                .then(MagicLevelCommand.register())
                .then(MaterialCommand.register())
                .then(MunnyCommand.register())
                .then(PayMunnyCommand.register())
                .then(RecipeCommand.register())
                .then(WhisperInMyEarPinkHairMan.register())
        );
    }

}
