package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.EffectRoomModifier;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers.RoomModifier;

import java.util.function.Supplier;

public class ModRoomModifiers {

    public static DeferredRegister<RoomModifier> ROOM_MODIFIERS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "room_modifiers"), KingdomKeys.MODID);
    public static Registry<RoomModifier> registry = ROOM_MODIFIERS.makeRegistry(builder -> builder.sync(true));

    public static final Supplier<RoomModifier> BLINDNESS = ROOM_MODIFIERS.register("blindness", () -> new EffectRoomModifier(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "blindness"), MobEffects.BLINDNESS));

}
