package online.kingdomkeys.kingdomkeys.world;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import online.kingdomkeys.kingdomkeys.dialogue.Dialogue;
import online.kingdomkeys.kingdomkeys.dialogue.DialogueAction;
import online.kingdomkeys.kingdomkeys.dialogue.DialogueCondition;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCOpenDialogue;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry.ModJsonRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DialogueHandler {

    private static final Map<UUID, Talk> TALKS = new HashMap<>();

    /** Everyone who is speaking */
    private static final Set<UUID> SPEAKING = new HashSet<>();

    private static final double REACH = 12.0D;
    private static final int CHAT_CHECK_DELAY = 20;

    private record Talk(Dialogue dialogue, LivingEntity speaker, String node) { }

    public static boolean isTalking(Player player) {
        return TALKS.containsKey(player.getUUID());
    }

    public static boolean start(ServerPlayer player, LivingEntity speaker, ResourceLocation dialogue) {
        Dialogue found = ModJsonRegistries.DIALOGUE.get().getValue(dialogue);
        return found != null && goTo(player, speaker, found, found.getStart());
    }

    private static boolean goTo(ServerPlayer player, LivingEntity speaker, Dialogue dialogue, String nodeName) {
        Dialogue.Node node = dialogue.getNode(nodeName);

        if (node == null) {
            forget(player);
            return false;
        }

        TALKS.put(player.getUUID(), new Talk(dialogue, speaker, nodeName));

        // Mark them as speaking therefore they stand still
        SPEAKING.add(speaker.getUUID());
        speaker.lookAt(EntityAnchorArgument.Anchor.EYES, player.getEyePosition());

        List<Dialogue.Answer> offered = offered(node, player, speaker);
        PacketHandler.sendTo(new SCOpenDialogue(speaker.getId(), node.spoken(player.getRandom()), offered.stream().map(Dialogue.Answer::text).toList()), player);
        return true;
    }

    /** The answers this player is allowed to see, in the order the file wrote them. */
    private static List<Dialogue.Answer> offered(Dialogue.Node node, ServerPlayer player, LivingEntity speaker) {
        List<Dialogue.Answer> offered = new ArrayList<>();

        for (Dialogue.Answer answer : node.answers()) {
            if (DialogueCondition.all(answer.conditions(), player, speaker)) {
                offered.add(answer);
            }
        }

        return offered;
    }

    public static void answer(ServerPlayer player, int index) {
        Talk talk = TALKS.get(player.getUUID());

        if (talk == null) {
            return;
        }

        // Gone, dead, or walked off mid-sentence
        if (!talk.speaker().isAlive() || talk.speaker().level() != player.level() || player.distanceToSqr(talk.speaker()) > REACH * REACH) {
            forget(player);
            return;
        }

        Dialogue.Node node = talk.dialogue().getNode(talk.node());

        if (node == null) {
            forget(player);
            return;
        }

        List<Dialogue.Answer> offered = offered(node, player, talk.speaker());

        if (index < 0 || index >= offered.size()) {
            forget(player);
            return;
        }

        Dialogue.Answer answer = offered.get(index);

        // Cleared first: an action that opens a screen or starts a fight wants the talk already over
        if (answer.ends()) {
            forget(player);
        }

        for (DialogueAction action : answer.actions()) {
            action.run(player, talk.speaker());
        }

        if (!answer.ends()) {
            answer.next().ifPresent(next -> goTo(player, talk.speaker(), talk.dialogue(), next));
        }
    }

    /** Dropped without a word, for when there is nobody left to say it to. */
    public static void forget(Player player) {
        forget(player.getUUID());
    }

    private static void forget(UUID listener) {
        Talk gone = TALKS.remove(listener);

        if (gone == null) {
            return;
        }

        // Free to walk off again, unless somebody else is still talking to them
        for (Talk talk : TALKS.values()) {
            if (talk.speaker() == gone.speaker()) {
                return;
            }
        }

        SPEAKING.remove(gone.speaker().getUUID());
    }

    /**
     * Holds a speaker still while they are talking.
     *
     * <p>Cancelling the tick outright rather than stopping their navigation, because a goal that
     * re-paths every tick will out-argue anything gentler. Nothing is written to the entity, so a
     * server that goes down mid-sentence leaves nobody frozen.</p>
     */
    @SubscribeEvent
    public void onEntityTick(EntityTickEvent.Pre event) {
        if (!event.getEntity().level().isClientSide && SPEAKING.contains(event.getEntity().getUUID())) {
            event.setCanceled(true);
        }
    }

    /**
     * Lets go of anyone whose listener is no longer there to listen.
     *
     * <p>The screen says goodbye on its way out, but a death or a dropped connection says nothing,
     * and a speaker held still by a conversation that ended without a word would stay that way.</p>
     */
    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        if (TALKS.isEmpty() || event.getServer().getTickCount() % CHAT_CHECK_DELAY != 0) {
            return;
        }

        for (UUID listener : List.copyOf(TALKS.keySet())) {
            ServerPlayer player = event.getServer().getPlayerList().getPlayer(listener);
            Talk talk = TALKS.get(listener);

            if (player == null || !player.isAlive() || !talk.speaker().isAlive()
                    || talk.speaker().level() != player.level()
                    || player.distanceToSqr(talk.speaker()) > REACH * REACH) {
                forget(listener);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        forget(event.getEntity());
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        forget(event.getEntity());
    }

    public static Entity speakerFor(Player player) {
        Talk talk = TALKS.get(player.getUUID());
        return talk == null ? null : talk.speaker();
    }
}
