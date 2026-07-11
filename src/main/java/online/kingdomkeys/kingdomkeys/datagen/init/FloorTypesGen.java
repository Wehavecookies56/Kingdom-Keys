package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.FloorTypeBuilder;
import online.kingdomkeys.kingdomkeys.datagen.provider.BaseProvider;
import online.kingdomkeys.kingdomkeys.util.KKResourceLocation;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.EffectRoomModifier;

public class FloorTypesGen extends BaseProvider<FloorTypeBuilder> {
    public FloorTypesGen(DataGenerator generator) {
        super(generator, KingdomKeys.MODID, "castle_oblivion/floor_type");
    }

    @Override
    protected void build() {
        createFloorType("none", 0, ResourceKey.create(Registries.BIOME, KKResourceLocation.of("castle_oblivion")));
        createFloorType("plains", 7, Biomes.PLAINS).music(SoundEvents.MUSIC_GAME.value());
        createFloorType("desert", 7, Biomes.DESERT).music(SoundEvents.MUSIC_BIOME_DESERT.value());
        createFloorType("ocean", 7, Biomes.OCEAN).music(SoundEvents.MUSIC_UNDER_WATER.value()).modifiers(new EffectRoomModifier(MobEffects.WATER_BREATHING, EffectRoomModifier.EffectType.BOTH));
        createFloorType("the_nether", 7, Biomes.NETHER_WASTES).music(SoundEvents.MUSIC_BIOME_NETHER_WASTES.value()).useFogColour();
    }

    @Override
    public String getName() {
        return "Kingdom Keys Castle Oblivion Floor Types";
    }

    public FloorTypeBuilder createFloorType(String path, int critPathLength, ResourceKey<Biome> biomeColours) {
        return addBuilder(new FloorTypeBuilder(getLocation(path), critPathLength, biomeColours));
    }
}
