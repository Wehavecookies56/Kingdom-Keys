package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.core.Holder;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.RoomEncounterBuilder;
import online.kingdomkeys.kingdomkeys.datagen.provider.BaseProvider;
import online.kingdomkeys.kingdomkeys.encounter.Encounter;
import online.kingdomkeys.kingdomkeys.encounter.WaveEncounter;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;


public class DuelEncountersGen extends BaseProvider<RoomEncounterBuilder> {

    private static final int INTERVAL = 60;
    private static final int SPAWN_POINTS = 1;

    private static final int ARENA = 0;

    private static final String DUEL_INFO = KingdomKeys.MODID + ".information.duel";
    private static final String SPAR_INFO = KingdomKeys.MODID + ".information.spar";

    /** The mark left by beating a grade, and the key that unlocks the next one. */
    public static ResourceLocation fought(String grade) {
        return KingdomKeys.rl("duel/" + grade);
    }

    public DuelEncountersGen(DataGenerator generator) {
        super(generator, KingdomKeys.MODID, "duel_encounter");
    }

    @Override
    protected void build() {
        // Against your own master, by grade, each gated on the one before it
        duel("easy", master(), ARENA).payout(200, 100).level(5).grants(fought("easy")).info(DUEL_INFO);
        duel("medium", master(), ARENA).payout(550, 300).level(30).requires(fought("easy")).grants(fought("medium")).info(DUEL_INFO);
        duel("hard", master(), ARENA).payout(1200, 700).level(75).requires(fought("medium")).grants(fought("hard")).info(DUEL_INFO);

        duel("dynamic", master(), ARENA).payout(700, 400).requires(fought("hard")).grants(fought("dynamic")).info(DUEL_INFO);

        duel("spar/apprentice", apprentice(), ARENA).payout(120, 60).info(SPAR_INFO);
    }

    /** One opponent, once, in a ring of the given size. */
    private RoomEncounterBuilder duel(String path, Holder<EntityType<?>> opponent, int arena) {
        Encounter encounter = new WaveEncounter(
                new RoomEncountersGen.WaveBuilder().wave(opponent).end().build(),
                INTERVAL,
                false
        );

        return addBuilder(new RoomEncounterBuilder(getLocation(path), encounter)).arena(arena, SPAWN_POINTS);
    }

    private static Holder<EntityType<?>> master() {
        return ModEntities.TYPE_MASTER_DUEL.getDelegate();
    }

    private static Holder<EntityType<?>> apprentice() {
        return ModEntities.TYPE_APPRENTICE_DUEL.getDelegate();
    }

    @Override
    public String getName() {
        return "Kingdom Keys Duel Encounters";
    }
}
