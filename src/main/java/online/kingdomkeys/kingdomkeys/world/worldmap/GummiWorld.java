package online.kingdomkeys.kingdomkeys.world.worldmap;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

// One world on the star map, read from data/kingdomkeys/gummi_worlds/<name>.json.
public record GummiWorld(ResourceKey<Level> dimension, double takeoffAltitude, Vec3 worldmapPosition, Vec3 takeOffSpawn, Vec3 landingSpawn, @Nullable Vec2 takeOffLook, @Nullable Vec2 landingLook, ResourceLocation texture, float scale, double approachRange) {}
