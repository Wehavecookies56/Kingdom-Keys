package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.dialogue.DialogueAction;
import online.kingdomkeys.kingdomkeys.dialogue.DialogueCondition;

public class DialogueBuilder extends BuilderBase {
    private final JsonObject nodes = new JsonObject();

    public DialogueBuilder(ResourceLocation location) {
        super(location);
        root.add("nodes", nodes);
    }

    public DialogueBuilder start(String node) {
        root.addProperty("start", node);
        return this;
    }

    public NodeBuilder node(String name, String... lines) {
        return new NodeBuilder(this, name, lines);
    }

    public static class NodeBuilder {
        private final DialogueBuilder parent;
        private final String name;
        private final JsonObject node = new JsonObject();
        private final JsonArray answers = new JsonArray();

        private NodeBuilder(DialogueBuilder parent, String name, String... lines) {
            this.parent = parent;
            this.name = name;

            JsonArray said = new JsonArray();
            for (String line : lines) {
                said.add(line);
            }

            node.add("lines", said);
            node.add("answers", answers);
        }

        /** Says one of its lines at random rather than all of them in order. */
        public NodeBuilder pick() {
            node.addProperty("pick", true);
            return this;
        }

        public AnswerBuilder answer(String text) {
            return new AnswerBuilder(this, text);
        }

        public DialogueBuilder end() {
            parent.nodes.add(name, node);
            return parent;
        }
    }

    public static class AnswerBuilder {
        private final NodeBuilder parent;
        private final JsonObject answer = new JsonObject();
        private final JsonArray conditions = new JsonArray();
        private final JsonArray actions = new JsonArray();

        private AnswerBuilder(NodeBuilder parent, String text) {
            this.parent = parent;
            answer.addProperty("text", text);
        }

        public AnswerBuilder goTo(String node) {
            answer.addProperty("goto", node);
            return this;
        }

        public AnswerBuilder onlyIf(DialogueCondition condition) {
            conditions.add(DialogueCondition.CODEC.encodeStart(JsonOps.INSTANCE, condition).resultOrPartial(KingdomKeys.LOGGER::error).orElseThrow());
            return this;
        }

        public AnswerBuilder then(DialogueAction action) {
            actions.add(DialogueAction.CODEC.encodeStart(JsonOps.INSTANCE, action).resultOrPartial(KingdomKeys.LOGGER::error).orElseThrow());
            return this;
        }

        public NodeBuilder end() {
            if (!conditions.isEmpty()) {
                answer.add("if", conditions);
            }
            if (!actions.isEmpty()) {
                answer.add("do", actions);
            }

            parent.answers.add(answer);
            return parent;
        }
    }
}
