package online.kingdomkeys.kingdomkeys.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommands {

    public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> root = dispatcher.register(Commands.literal("kingdomkeys")
        		.then(AbilityCommand.register())
                .then(ChoiceCommand.register())
                .then(DimensionCommand.register())
                .then(DriveLevelCommand.register())
                .then(SynthLevelCommand.register())
                .then(DrivePointsCommand.register())
                .then(ExpCommand.register())
                .then(ExportWorldCommand.register())
                .then(FocusPointsCommand.register())
                .then(HeartsCommand.register())
                .then(LevelCommand.register())
                .then(LuxCommand.register())
                .then(MaterialCommand.register())
                .then(MunnyCommand.register())
                .then(PayMunnyCommand.register())
                .then(RecipeCommand.register())
                .then(UnionCommand.register())
                .then(WhisperInMyEarPinkHairMan.register())
                .then(ConvertOldForgeDataCommand.register())
		        .then(CheckCommand.register())
        );
		dispatcher.register(Commands.literal("kk").redirect(root));
    }

}
