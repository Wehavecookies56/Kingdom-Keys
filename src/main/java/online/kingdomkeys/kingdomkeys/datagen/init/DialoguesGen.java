package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.DialogueBuilder;
import online.kingdomkeys.kingdomkeys.datagen.provider.BaseProvider;
import online.kingdomkeys.kingdomkeys.dialogue.DialogueAction;
import online.kingdomkeys.kingdomkeys.dialogue.DialogueCondition;

import java.util.Optional;

public class DialoguesGen extends BaseProvider<DialogueBuilder> {
    private static final String KEY = KingdomKeys.MODID + ".dialogue.foreteller.";

    private static final String[] GRADES = { "easy", "medium", "hard", "dynamic" };

    /** Set once he has explained why the orbs are about to start biting back. */
    private static final ResourceLocation WARNED_OF_DARKNESS = KingdomKeys.rl("foreteller/warned_of_darkness");

    /** The lesson that first fields dark orbs, and so the one the warning belongs to. */
    private static final String DARK_FROM = "medium";

    /** How many things an apprentice might open with. */
    private static final int SMALL_TALK = 8;

    private static final ResourceLocation SPAR = KingdomKeys.rl("spar/apprentice");

    public DialoguesGen(DataGenerator generator) {
        super(generator, KingdomKeys.MODID, "dialogue");
    }

    @Override
    protected void build() {
        DialogueBuilder foreteller = createDialogue("foreteller");

        foreteller.node("start", KEY + "greeting")
                .answer(KEY + "answer.train").goTo("lessons").onlyIf(ownPupil()).end()
                .answer(KEY + "answer.spar").goTo("duels").onlyIf(ownPupil()).end()
                .answer(KEY + "answer.shop").then(new DialogueAction.OpenShop()).end()
                .answer(KEY + "answer.leave").then(new DialogueAction.Close()).end()
                .end();

        grades(foreteller.node("lessons", KEY + "lessons"), false).end();
        grades(foreteller.node("duels", KEY + "duels"), true).end();

        foreteller.node("darkness", KEY + "darkness.1", KEY + "darkness.2")
                .answer(KEY + "answer.ready")
                    .then(new DialogueAction.SetFlag(WARNED_OF_DARKNESS, true))
                    .then(new DialogueAction.StartEncounter(false, KingdomKeys.rl(DARK_FROM)))
                    .end()
                .answer(KEY + "answer.notyet").goTo("lessons").end()
                .end();

        apprentice();
    }

    private void apprentice() {
        String key = KingdomKeys.MODID + ".dialogue.apprentice.";

        String[] smallTalk = new String[SMALL_TALK];
        for (int i = 0; i < SMALL_TALK; i++) {
            smallTalk[i] = key + "greeting." + (i + 1);
        }

        createDialogue("apprentice")
                .node("start", smallTalk).pick()
                    .answer(key + "answer.spar").then(new DialogueAction.StartEncounter(true, SPAR)).end()
                    .answer(key + "answer.leave").then(new DialogueAction.Close()).end()
                    .end();
    }

    /**
     * One answer per grade, each one only offered once the one before it has been beaten.
     */
    private static DialogueBuilder.NodeBuilder grades(DialogueBuilder.NodeBuilder node, boolean duel) {
        DialogueBuilder.NodeBuilder built = node;

        for (String grade : GRADES) {
            String text = KingdomKeys.MODID + ".encounter." + grade;
            ResourceLocation name = KingdomKeys.rl(grade);
            boolean warns = !duel && DARK_FROM.equals(grade);

            if (warns) {
                built = built.answer(text)
                        .onlyIf(new DialogueCondition.CanStart(false, name))
                        .onlyIf(new DialogueCondition.HasFlag(WARNED_OF_DARKNESS, false))
                        .goTo("darkness")
                        .end();
            }

            DialogueBuilder.AnswerBuilder answer = built.answer(text)
                    .onlyIf(new DialogueCondition.CanStart(duel, name));

            if (warns) {
                answer = answer.onlyIf(new DialogueCondition.HasFlag(WARNED_OF_DARKNESS, true));
            }

            built = answer.then(new DialogueAction.StartEncounter(duel, name)).end();
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
