package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.RoomType;

import java.util.function.Supplier;

public class ModRoomTypes {

    public static Supplier<JsonRegistry<RoomType>> registry = ModJsonRegistries.ROOM_TYPE;

    //TODO create modifiers
    public static final Supplier<RoomType>
            //Special
            ENTRANCE_HALL = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "entrance_hall")),
            CONQUERERS_RESPITE = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "conquerers_respite")),
            UNKNOWN_ROOM = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "unknown_room")),

            //Enemy
            TRANQUIL_DARKNESS = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "tranquil_darkness")),
            TEEMING_DARKNESS = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "teeming_darkness")),
            FEEBLE_DARKNESS = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "feeble_darkness")), //add weakness modifier
            ALMIGHTY_DARKNESS = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "almighty_darkness")), //strength modifier
            SLEEPING_DARKNESS = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "sleeping_darkness")), //stop modifier
            LOOMING_DARKNESS = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "looming_darkness")), //speed modifier
            BOTTOMLESS_DARKNESS = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "bottomless_darkness")), //blindness modifier

            //Status
            MARTIAL_WAKING = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "martial_waking")), //player strength modifier
            SORCEROUS_WAKING = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "sorcerous_waking")), //magic modifier
            ALCHEMIC_WAKING = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "alchemic_waking")), //item modifier
            STAGNANT_SPACE = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "stagnant_space")), //slow modifier
            WEIGHTLESS_SPACE = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "weightless_space")), //jump modifier

            //Bounty
            CALM_BOUNTY = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "calm_bounty")), //fixed room
            GUARDED_TROVE = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "guarded_trove")), //fixed room
            FALSE_BOUNTY = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "false_bounty")), //fixed room
            MOMENTS_REPRIEVE = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "moments_reprieve")), //fixed room
            MOOGLE_ROOM = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "moogle_room")), //fixed room
            PROSPEROUS_REPOSITORY = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "prosperous_repository")),
            TREACHEROUS_RESPOITORY = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "treacherous_repository")),
            REPOSEFUL_GROVE = () -> registry.get().getValue(new ResourceLocation(KingdomKeys.MODID, "reposeful_grove")); //fixed room
}
