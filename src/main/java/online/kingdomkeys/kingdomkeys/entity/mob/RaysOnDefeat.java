package online.kingdomkeys.kingdomkeys.entity.mob;

/**
 * Anything that goes out in a pillar of light rather than simply falling over.
 *
 * <p>The effect used to belong to {@link BaseKHEntity}, where it was tied to dying. It is an
 * interface so that whoever wants it can have it: a master who is beaten in a bout is not a
 * {@code BaseKHEntity} and is not dying, but the light is his all the same.</p>
 */
public interface RaysOnDefeat {

    /** Ticks the light takes to run its course, from the first spark to the last. */
    int DEATH_SEQUENCE_TICKS = 100;

    /** How far into the sequence it is, counted up so the renderer can grow the glow from it. */
    int getDeathSequence();

    default boolean isDyingWithRays() {
        return getDeathSequence() > 0;
    }

    /**
     * How solid the body still is, one for whole and zero for gone.
     *
     * <p>A boss dissolves as the light takes it. Anything that is merely beaten answers one
     * throughout and keeps its body.</p>
     */
    default float deathAlpha(float partialTick) {
        return 1F;
    }
}
