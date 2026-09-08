package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.DialogueBuilder;
import online.kingdomkeys.kingdomkeys.datagen.provider.BaseProvider;
import online.kingdomkeys.kingdomkeys.dialogue.DialogueAction;
import online.kingdomkeys.kingdomkeys.dialogue.DialogueCondition;

import java.util.Optional;

public class DialoguesGen extends BaseProvider<DialogueBuilder> {
    private static final String KEY = KingdomKeys.MODID + ".dialogue.foreteller.";

    private static final String[] GRADES = { "easy", "medium", "hard", "dynamic" };

    public DialoguesGen(DataGenerator generator) {
        super(generator, KingdomKeys.MODID, "dialogue");
    }

    @Override
    protected void build() {
        DialogueBuilder foreteller = createDialogue("foreteller");

        DialogueBuilder.NodeBuilder start = foreteller.node("start", KEY + "greeting")
                .answer(KEY + "answer.train").goTo("lessons").onlyIf(ownPupil()).end()
                .answer(KEY + "answer.spar").goTo("duels").onlyIf(ownPupil()).end()
                .answer(KEY + "answer.shop").then(new DialogueAction.OpenShop()).end()
                .answer(KEY + "answer.leave").then(new DialogueAction.Close()).end();

        start.end();

        grades(foreteller.node("lessons", KEY + "lessons"), false).end();
        grades(foreteller.node("duels", KEY + "duels"), true).end();
    }

    /** The same four answers either side, each one setting the encounter of that name going. */
    private static DialogueBuilder.NodeBuilder grades(DialogueBuilder.NodeBuilder node, boolean duel) {
        DialogueBuilder.NodeBuilder built = node;

        for (String grade : GRADES) {
            built = built.answer(KingdomKeys.MODID + ".encounter." + grade)
                    .then(new DialogueAction.StartEncounter(duel, KingdomKeys.rl(grade)))
                    .end();
        }

        return built.answer(KEY + "answer.back").goTo("start").end();
    }

    /** No union named, so it reads as the speaker's own. */
    private static DialogueCondition ownPupil() {
        return new DialogueCondition.InUnion(Optional.empty());
    }

    @Override
    public String getName() {
        return "Kingdom Keys Dialogues";
    }

    public DialogueBuilder createDialogue(String path) {
        return addBuilder(new DialogueBuilder(getLocation(path)));
    }
}
