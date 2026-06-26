package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.EffectRoomModifier;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.LevelModifier;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifier;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.SpawnMobModifier;

import java.util.List;
import java.util.function.Supplier;

public class ModRoomModifiers {

    public static DeferredRegister<RoomModifier> ROOM_MODIFIERS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "room_modifiers"), KingdomKeys.MODID);
    public static Registry<RoomModifier> registry = ROOM_MODIFIERS.makeRegistry(builder -> builder.sync(true));

    public static final Supplier<RoomModifier>
            BLINDNESS = ROOM_MODIFIERS.register("blindness", () -> new EffectRoomModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "blindness"), MobEffects.BLINDNESS, EffectRoomModifier.EffectType.PLAYER)),
            SLOWNESS = ROOM_MODIFIERS.register("slowness", () -> new EffectRoomModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "slowness"), MobEffects.MOVEMENT_SLOWDOWN, EffectRoomModifier.EffectType.MOBS)),
            WATER_BREATHING = ROOM_MODIFIERS.register("water_breathing", () -> new EffectRoomModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "water_breathing"), MobEffects.WATER_BREATHING, EffectRoomModifier.EffectType.BOTH)),
            SPAWN_MOOGLE = ROOM_MODIFIERS.register("spawn_moogle", () -> new SpawnMobModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "spawn_moogle"), ModEntities.TYPE_MOOGLE, SpawnMobModifier.createMoogleInv(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cards")))),
            LEVEL_PLUS_2 = ROOM_MODIFIERS.register("level_plus_2", () -> new LevelModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "level_plus_2"), List.of(new LevelModifier.Operation(2, LevelModifier.Operator.ADD)))),
            LEVEL_MINUS_2 = ROOM_MODIFIERS.register("level_minus_2", () -> new LevelModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "level_minus_2"), List.of(new LevelModifier.Operation(2, LevelModifier.Operator.SUBTRACT))))
    ;

}
