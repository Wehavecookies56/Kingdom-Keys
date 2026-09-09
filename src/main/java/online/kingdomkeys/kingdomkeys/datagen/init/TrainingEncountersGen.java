package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.RoomEncounterBuilder;
import online.kingdomkeys.kingdomkeys.datagen.provider.BaseProvider;
import online.kingdomkeys.kingdomkeys.encounter.Encounter;
import online.kingdomkeys.kingdomkeys.encounter.WaveEncounter;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class TrainingEncountersGen extends BaseProvider<RoomEncounterBuilder> {
    private static final int INTERVAL = 60;

    public TrainingEncountersGen(DataGenerator generator) {
        super(generator, KingdomKeys.MODID, "training_encounter");
    }

    public static ResourceLocation trained(String grade) {
        return KingdomKeys.rl("training/" + grade);
    }

    private static final int EASY_LEVEL = 10;
    private static final int MEDIUM_LEVEL = 25;
    private static final int HARD_LEVEL = 60;

    @Override
    protected void build() {
        createTrainingEncounter("easy", new WaveEncounter(
                new RoomEncountersGen.WaveBuilder()
                        .wave(light(), light(), light(), light()).end()
                        .wave(light(), light(), light(), light(), light()).end()
                        .wave(light(), light(), light(), light(), light(), light()).end()
                        .build(), INTERVAL, false)).payout(120, 60).arena(7, 8).level(EASY_LEVEL).grants(trained("easy"));

        createTrainingEncounter("medium", new WaveEncounter(
                new RoomEncountersGen.WaveBuilder()
                        .wave(light(), light(), dark(), light()).end()
                        .wave(light(), light(), dark(), dark(), dark()).end()
                        .wave(light(), light(), dark(), light(), dark(), dark()).end()
                        .wave(dark(), dark(), dark()).end()
                        .build(), INTERVAL, false)).payout(320, 180).arena(8, 8).level(MEDIUM_LEVEL).requires(trained("easy")).grants(trained("medium"));

        createTrainingEncounter("hard", new WaveEncounter(
                new RoomEncountersGen.WaveBuilder()
                        .wave(dark(), dark(), dark(), dark()).end()
                        .wave(dark(), dark(), dark(), dark(), dark()).end()
                        .wave(dark(), dark(), dark(), dark(), dark(), dark()).end()
                        .wave(dark(), dark(), dark(), dark(), dark(), dark(), dark()).end()
                        .build(), INTERVAL, false)).payout(700, 400).arena(9, 10).level(HARD_LEVEL).requires(trained("medium")).grants(trained("hard"));

        createTrainingEncounter("dynamic", new WaveEncounter(
                new RoomEncountersGen.WaveBuilder()
                        .wave(light(), light(), light(), light()).end()
                        .wave(light(), light(), dark(), dark()).end()
                        .wave(light(), dark(), dark(), dark(), dark()).end()
                        .wave(dark(), dark(), dark(), dark(), dark(), dark()).end()
                        .build(), INTERVAL, false)).payout(400, 220).arena(8, 10).requires(trained("hard")).grants(trained("dynamic"));
    }

    private static Holder<EntityType<?>> light() {
        return ModEntities.TYPE_LIGHT_TRAINING_ORB.getDelegate();
    }

    private static Holder<EntityType<?>> dark() {
        return ModEntities.TYPE_DARK_TRAINING_ORB.getDelegate();
    }

    @Override
    public String getName() {
        return "Kingdom Keys Foreteller Training Encounters";
    }

    public RoomEncounterBuilder createTrainingEncounter(String path, Encounter encounter) {
        return addBuilder(new RoomEncounterBuilder(getLocation(path), encounter));
    }
}
