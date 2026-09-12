package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.DialogueBuilder;
import online.kingdomkeys.kingdomkeys.datagen.provider.BaseProvider;
import online.kingdomkeys.kingdomkeys.dialogue.DialogueAction;
import online.kingdomkeys.kingdomkeys.dialogue.DialogueCondition;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.story.StoryFlags;

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

    private static final ResourceLocation DAYBREAK_TOWN = KingdomKeys.rl(Strings.daybreakTown);

    public DialoguesGen(DataGenerator generator) {
        super(generator, KingdomKeys.MODID, "dialogue");
    }

    @Override
    protected void build() {
        DialogueBuilder foreteller = createDialogue("foreteller");

        foreteller.node("start", KEY + "greeting")
                .answer(KEY + "answer.train").goTo("lessons").onlyIf(ownPupil()).end()
                // Not while you are his pupil under his roof: crossing keyblades with him is for a wielder who has finished and come back by their own means
                .answer(KEY + "answer.spar").goTo("duels").onlyIf(ownPupil()).onlyIf(new DialogueCondition.HasFlag(StoryFlags.INTRODUCTORY_TRAINING_DONE, true)).end()
                .answer(KEY + "answer.ask").goTo("theory").onlyIf(ownPupil()).end()
                // He keeps no stall for an apprentice he is housing. Lux is worth spending once you are travelling on your own account
                .answer(KEY + "answer.shop").onlyIf(new DialogueCondition.HasFlag(StoryFlags.INTRODUCTORY_TRAINING_DONE, true)).then(new DialogueAction.OpenShop()).end()
                // The apprenticeship is over the moment the middle lesson is passed. Until he has said so, this is the only way out, and saying so is what ends it
                .answer(KEY + "answer.graduate").goTo("graduation")
                    .onlyIf(ownPupil())
                    .onlyIf(new DialogueCondition.HasFlag(TrainingEncountersGen.trained("medium"), true))
                    .onlyIf(new DialogueCondition.HasFlag(StoryFlags.INTRODUCTORY_TRAINING_DONE, false))
                    .end()
                // The errand version of the same trip, for fetching supplies mid-lesson. It gives way to
                // the graduation once that is on offer: two doors home on the same menu, one of which
                // quietly ends the apprenticeship, is a choice nobody asked to be given
                .answer(KEY + "answer.home").goTo("home")
                    .onlyIf(ownPupil())
                    .onlyIf(new DialogueCondition.HasFlag(StoryFlags.FORETELLER_VISITED, true))
                    .onlyIf(new DialogueCondition.HasFlag(TrainingEncountersGen.trained("medium"), false))
                    .onlyIf(new DialogueCondition.HasFlag(StoryFlags.INTRODUCTORY_TRAINING_DONE, false))
                    .end()
                .answer(KEY + "answer.leave").then(new DialogueAction.Close()).end()
                .end();

        theory(foreteller);

        // Taking the way out he opens here is what shuts the one he left in your world
        foreteller.node("graduation", KEY + "graduation.1", KEY + "graduation.2", KEY + "graduation.3", KEY + "graduation.4")
                .answer(KEY + "answer.send")
                    .then(new DialogueAction.SetFlag(StoryFlags.INTRODUCTORY_TRAINING_DONE, true))
                    .then(new DialogueAction.ReturnHome())
                    .end()
                .end();

        foreteller.node("home", KEY + "home")
                .answer(KEY + "answer.send").then(new DialogueAction.ReturnHome()).end()
                .answer(KEY + "answer.back").goTo("start").end()
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

        firstMeeting();
        apprentice();
    }

    private void firstMeeting() {
        String key = KingdomKeys.MODID + ".dialogue.first_meeting.";

        DialogueBuilder meeting = createDialogue("foreteller_first_meeting");

        meeting.node("start", key + "greeting.1", key + "greeting.2")
                .answer(key + "answer.who").goTo("who").end()
                .answer(key + "answer.why").goTo("why").end()
                .answer(key + "answer.come").goTo("offer").end()
                .answer(key + "answer.leave").then(new DialogueAction.Close()).end()
                .end();

        // He opens the way and then waits by it, so the offer can be taken up whenever
        meeting.node("offer", key + "offer.1", key + "offer.2", key + "offer.3")
                .answer(key + "answer.open").then(new DialogueAction.OpenPortal(DAYBREAK_TOWN, Optional.empty(), Optional.of(StoryFlags.INTRODUCTORY_TRAINING_DONE))).end()
                .answer(key + "answer.leave").then(new DialogueAction.Close()).end()
                .end();

        // Both topics stay on offer from either one. Hiding the question you just asked made the
        // menu shuffle about under the cursor, and there is no harm in hearing an answer twice
        meeting.node("who", key + "who.1", key + "who.2")
                .answer(key + "answer.who").goTo("who").end()
                .answer(key + "answer.why").goTo("why").end()
                .answer(key + "answer.come").goTo("offer").end()
                .answer(key + "answer.leave").then(new DialogueAction.Close()).end()
                .end();

        meeting.node("why", key + "why.1", key + "why.2")
                .answer(key + "answer.who").goTo("who").end()
                .answer(key + "answer.why").goTo("why").end()
                .answer(key + "answer.come").goTo("offer").end()
                .answer(key + "answer.leave").then(new DialogueAction.Close()).end()
                .end();
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

    private static void theory(DialogueBuilder foreteller) {
        String[] topics = { "keyblade", "lux" };

        for (String from : new String[] { "theory", "theory_keyblade", "theory_lux" }) {
            String[] lines = "theory".equals(from) ? new String[] { KEY + "theory" } : new String[] { KEY + from + ".1", KEY + from + ".2", KEY + from + ".3" };

            DialogueBuilder.NodeBuilder node = foreteller.node(from, lines);

            for (String topic : topics) {
                node = node.answer(KEY + "answer.theory." + topic).goTo("theory_" + topic).end();
            }

            node.answer(KEY + "answer.back").goTo("start").end().end();
        }
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
