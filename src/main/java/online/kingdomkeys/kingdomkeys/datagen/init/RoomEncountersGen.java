package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.core.Holder;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.datagen.builder.RoomEncounterBuilder;
import online.kingdomkeys.kingdomkeys.datagen.builder.RoomStructureBuilder;
import online.kingdomkeys.kingdomkeys.datagen.provider.BaseProvider;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter.Encounter;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter.RoomEncounter;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter.WaveEncounter;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.encounter.WaveEncounter.Wave;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomCategory;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomSize;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomStructure;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomEncountersGen extends BaseProvider<RoomEncounterBuilder> {

    public RoomEncountersGen(DataGenerator generator) {
        super(generator, KingdomKeys.MODID, "castle_oblivion/room_encounter");
    }

    @Override
    protected void build() {
        createRoomEncounter("room_of_beginnings", new WaveEncounter(
                new WaveBuilder()
                .wave(ModEntities.TYPE_SHADOW.getDelegate(), ModEntities.TYPE_SHADOW.getDelegate(), ModEntities.TYPE_SHADOW.getDelegate()).end()
                .wave(ModEntities.TYPE_RED_NOCTURNE.getDelegate(), ModEntities.TYPE_BLUE_RHAPSODY.getDelegate(), ModEntities.TYPE_SHADOW.getDelegate()).end()
                .wave(ModEntities.TYPE_SHADOW.getDelegate(), ModEntities.TYPE_SOLDIER.getDelegate(), ModEntities.TYPE_SHADOW.getDelegate()).end()
                .build(),
                100,
                false
        ), new ItemStack(ModItems.keyOfGuidance.get()))
                .music(ModSounds.Music_Forgotten_Challenge.value());
        createRoomEncounter("room_of_guidance", new WaveEncounter(
                new WaveBuilder()
                        .wave(ModEntities.TYPE_SHADOW.getDelegate(), ModEntities.TYPE_LARGE_BODY.getDelegate(), ModEntities.TYPE_SHADOW.getDelegate()).end()
                        .wave(ModEntities.TYPE_SOLDIER.getDelegate(), ModEntities.TYPE_SOLDIER.getDelegate()).end()
                        .wave(ModEntities.TYPE_RED_NOCTURNE.getDelegate(), ModEntities.TYPE_DIRE_PLANT.getDelegate(), ModEntities.TYPE_DIRE_PLANT.getDelegate(), ModEntities.TYPE_DIRE_PLANT).end()
                        .build(),
                100,
                false
        ), new ItemStack(ModItems.keyToTruth.get()))
                .music(ModSounds.Music_Forgotten_Challenge.value());
        createRoomEncounter("room_of_truth", new WaveEncounter(
                new WaveBuilder()
                        .wave(ModEntities.TYPE_SHADOW.getDelegate(), ModEntities.TYPE_LARGE_BODY.getDelegate(), ModEntities.TYPE_SHADOW.getDelegate(), ModEntities.TYPE_SOLDIER.getDelegate()).end()
                        .wave(ModEntities.TYPE_SOLDIER.getDelegate(), ModEntities.TYPE_DARKBALL.getDelegate(), ModEntities.TYPE_GREEN_REQUIEM.getDelegate()).end()
                        .wave(ModEntities.TYPE_RED_NOCTURNE.getDelegate(), ModEntities.TYPE_BLUE_RHAPSODY.getDelegate(), ModEntities.TYPE_YELLOW_OPERA.getDelegate()).end()
                        .wave(ModEntities.TYPE_MINUTE_BOMB.getDelegate(), ModEntities.TYPE_MINUTE_BOMB.getDelegate(), ModEntities.TYPE_EMERALD_BLUES.getDelegate()).end()
                        .wave(ModEntities.TYPE_LARGE_BODY.getDelegate(), ModEntities.TYPE_LARGE_BODY.getDelegate(), ModEntities.TYPE_SHADOW.getDelegate(), ModEntities.TYPE_SHADOW.getDelegate(), ModEntities.TYPE_SHADOW.getDelegate(), ModEntities.TYPE_GREEN_REQUIEM.getDelegate()).end()
                        .build(),
                100,
                false
        )).music(ModSounds.Music_Forgotten_Challenge.value());
    }

    @Override
    public String getName() {
        return "Kingdom Keys Castle Oblivion Room Encounters";
    }

    public RoomEncounterBuilder createRoomEncounter(String path, Encounter encounter, ItemStack... rewards) {
        return addBuilder(new RoomEncounterBuilder(getLocation(path), encounter, rewards));
    }

    public static class WaveBuilder {
        List<Wave> waves = new ArrayList<>();
        List<Holder<EntityType<?>>> spawns = new ArrayList<>();
        List<RoomModifier> modifiers = new ArrayList<>();

        @SafeVarargs
        public final WaveBuilder wave(Holder<EntityType<?>>... spawns) {
            if (this.spawns.isEmpty()) {
                this.spawns = List.of(spawns);
            } else {
                throw new IllegalStateException("Tried to add spawns without calling end() first");
            }
            return this;
        }

        public WaveBuilder modifiers(RoomModifier... modifiers) {
            if (this.modifiers.isEmpty() && !this.spawns.isEmpty()) {
                this.modifiers = List.of(modifiers);
            } else {
                throw new IllegalStateException("Tried to add modifier without adding spawns first");
            }
            return this;
        }

        public WaveBuilder end() {
            if (!spawns.isEmpty()) {
                waves.add(new Wave(spawns, modifiers));
                spawns = new ArrayList<>();
                modifiers = new ArrayList<>();
            } else {
                throw new IllegalStateException("Called end() without adding spawns");
            }
            return this;
        }

        public List<Wave> build() {
            if (waves.isEmpty()) {
                throw new IllegalStateException("Called build() without creating any waves");
            }
            return waves;
        }
    }
}
