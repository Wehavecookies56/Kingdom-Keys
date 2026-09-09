package online.kingdomkeys.kingdomkeys.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.RandomSource;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.JsonRegistryObject;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Dialogue extends JsonRegistryObject {
    private final String start;
    private final Map<String, Node> nodes;

    public static final Codec<Dialogue> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.optionalFieldOf("start", "start").forGetter(Dialogue::getStart),
            Codec.unboundedMap(Codec.STRING, Node.CODEC).fieldOf("nodes").forGetter(Dialogue::getNodes)
        ).apply(instance, Dialogue::new)
    );

    private Dialogue(String start, Map<String, Node> nodes) {
        this.start = start;
        this.nodes = nodes;
    }

    public String getStart() {
        return start;
    }

    public Map<String, Node> getNodes() {
        return nodes;
    }

    public Node getNode(String name) {
        return nodes.get(name);
    }

    /**
     * @param pick one of the lines at random instead of all of them in order, so a crowd of the same
     *             kind of person does not greet you with the same sentence every time
     */
    public record Node(List<String> lines, boolean pick, List<Answer> answers) {

        public static final Codec<Node> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.listOf().fieldOf("lines").forGetter(Node::lines),
                Codec.BOOL.optionalFieldOf("pick", false).forGetter(Node::pick),
                Answer.CODEC.listOf().optionalFieldOf("answers", List.of()).forGetter(Node::answers)
            ).apply(instance, Node::new)
        );

        /** What this node actually says this time round. */
        public List<String> spoken(RandomSource random) {
            if (!pick || lines.size() < 2) {
                return lines;
            }

            return List.of(lines.get(random.nextInt(lines.size())));
        }
    }

    public record Answer(String text, Optional<String> next, List<DialogueCondition> conditions, List<DialogueAction> actions) {

        public static final Codec<Answer> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.STRING.fieldOf("text").forGetter(Answer::text),
                        Codec.STRING.optionalFieldOf("goto").forGetter(Answer::next),
                        DialogueCondition.CODEC.listOf().optionalFieldOf("if", List.of()).forGetter(Answer::conditions),
                        DialogueAction.CODEC.listOf().optionalFieldOf("do", List.of()).forGetter(Answer::actions)
                ).apply(instance, Answer::new)
        );

        public boolean ends() {
            return next.isEmpty() || actions.stream().anyMatch(DialogueAction::ends);
        }
    }
}
