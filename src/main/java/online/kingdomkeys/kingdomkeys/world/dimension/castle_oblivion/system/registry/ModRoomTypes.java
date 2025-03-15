package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.Size2i;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;

import java.awt.*;
import java.util.function.Supplier;

public class ModRoomTypes {

    public static Supplier<JsonRegistry<RoomType>> registry = ModJsonRegistries.ROOM_TYPE;

    //TODO create modifiers
    public static final Supplier<RoomType>
            //Special
            ENTRANCE_HALL = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "entrance_hall")),
            CONQUERERS_RESPITE = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "conquerers_respite")),
            UNKNOWN_ROOM = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "unknown_room")),

            //Enemy
            TRANQUIL_DARKNESS = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "tranquil_darkness")),
            TEEMING_DARKNESS = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "teeming_darkness")),
            FEEBLE_DARKNESS = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "feeble_darkness")), //add weakness modifier
            ALMIGHTY_DARKNESS = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "almighty_darkness")), //strength modifier
            SLEEPING_DARKNESS = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sleeping_darkness")), //stop modifier
            LOOMING_DARKNESS = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "looming_darkness")), //speed modifier
            BOTTOMLESS_DARKNESS = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "bottomless_darkness")), //blindness modifier

            //Status
            MARTIAL_WAKING = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "martial_waking")), //player strength modifier
            SORCEROUS_WAKING = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sorcerous_waking")), //magic modifier
            ALCHEMIC_WAKING = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "alchemic_waking")), //item modifier
            STAGNANT_SPACE = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "stagnant_space")), //slow modifier
            WEIGHTLESS_SPACE = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "weightless_space")), //jump modifier

            //Bounty
            CALM_BOUNTY = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "calm_bounty")), //fixed room
            GUARDED_TROVE = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "guarded_trove")), //fixed room
            FALSE_BOUNTY = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "false_bounty")), //fixed room
            MOMENTS_REPRIEVE = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "moments_reprieve")), //fixed room
            MOOGLE_ROOM = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "moogle_room")), //fixed room
            PROSPEROUS_REPOSITORY = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "prosperous_repository")),
            TREACHEROUS_RESPOITORY = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "treacherous_repository")),
            REPOSEFUL_GROVE = () -> registry.get().getValue(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "reposeful_grove")); //fixed room
}
