package online.kingdomkeys.kingdomkeys.world.worldmap;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

// One world on the star map, read from data/kingdomkeys/gummi_worlds/<name>.json.
public record GummiWorld(ResourceKey<Level> dimension, double takeoffAltitude, Vec3 worldmapPosition, Vec3 takeOffSpawn, Vec3 landingSpawn, ResourceLocation texture, float scale, double approachRange) {}