package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter.EncounterType;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter.WaveEncounter;

public class ModEncounterTypes {
    public static final DeferredRegister<EncounterType<?, ?>> ENCOUNTER_TYPES = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "encounter_type"), KingdomKeys.MODID);
    public static final Registry<EncounterType<?, ?>> REGISTRY = ENCOUNTER_TYPES.makeRegistry(encounterTypeRegistryBuilder -> encounterTypeRegistryBuilder.sync(true));
    public static final DeferredHolder<EncounterType<?, ?>, EncounterType<WaveEncounter, WaveEncounter.State>> WAVE = ENCOUNTER_TYPES.register("wave", () -> new EncounterType<>(WaveEncounter.CODEC, WaveEncounter.State.CODEC, new WaveEncounter.Handler()));
}
