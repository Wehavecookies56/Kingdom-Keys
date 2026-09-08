package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.core.Holder;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.RoomEncounterBuilder;
import online.kingdomkeys.kingdomkeys.datagen.provider.BaseProvider;
import online.kingdomkeys.kingdomkeys.encounter.Encounter;
import online.kingdomkeys.kingdomkeys.encounter.WaveEncounter;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class DuelEncountersGen extends BaseProvider<RoomEncounterBuilder> {

    private static final int INTERVAL = 60;

    private static final int ARENA_RADIUS = 4;
    private static final int SPAWN_POINTS = 1;

    public DuelEncountersGen(DataGenerator generator) {
        super(generator, KingdomKeys.MODID, "duel_encounter");
    }

    @Override
    protected void build() {
        createDuelEncounter("easy").payout(200, 100).arena(ARENA_RADIUS, SPAWN_POINTS).level(10);
        createDuelEncounter("medium").payout(550, 300).arena(ARENA_RADIUS, SPAWN_POINTS).level(25);
        createDuelEncounter("hard").payout(1200, 700).arena(ARENA_RADIUS, SPAWN_POINTS).level(60);

        createDuelEncounter("dynamic").payout(700, 400).arena(ARENA_RADIUS, SPAWN_POINTS);
    }

    private static Holder<EntityType<?>> master() {
        return ModEntities.TYPE_MASTER_DUEL.getDelegate();
    }

    @Override
    public String getName() {
        return "Kingdom Keys Foreteller Duel Encounters";
    }

    public RoomEncounterBuilder createDuelEncounter(String path) {
        Encounter encounter = new WaveEncounter(
                new RoomEncountersGen.WaveBuilder().wave(master()).end().build(),
                INTERVAL,
                false
        );

        return addBuilder(new RoomEncounterBuilder(getLocation(path), encounter));
    }
}
