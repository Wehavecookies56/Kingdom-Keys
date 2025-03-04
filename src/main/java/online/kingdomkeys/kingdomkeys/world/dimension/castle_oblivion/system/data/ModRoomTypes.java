package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.data;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomType;

import java.util.function.Supplier;

public class ModRoomTypes {

    //TODO create modifiers
    public static final Supplier<RoomType>
            //Special
            LOBBY = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "lobby")),
            CONQUERERS_RESPITE = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "conquerers_respite")),

            //Enemy
            TRANQUIL_DARKNESS = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "tranquil_darkness")),
            TEEMING_DARKNESS = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "teeming_darkness")),
            FEEBLE_DARKNESS = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "feeble_darkness")), //add weakness modifier
            ALMIGHTY_DARKNESS = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "almighty_darkness")), //strength modifier
            SLEEPING_DARKNESS = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "sleeping_darkness")), //stop modifier
            LOOMING_DARKNESS = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "looming_darkness")), //speed modifier
            BOTTOMLESS_DARKNESS = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "bottomless_darkness")), //blindness modifier

            //Status
            MARTIAL_WAKING = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "martial_waking")), //player strength modifier
            SORCEROUS_WAKING = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "sorcerous_waking")), //magic modifier
            ALCHEMIC_WAKING = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "alchemic_waking")), //item modifier
            STAGNANT_SPACE = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "stagnant_space")), //slow modifier
            WEIGHTLESS_SPACE = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "weightless_space")), //jump modifier

            //Bounty
            CALM_BOUNTY = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "calm_bounty")), //fixed room
            GUARDED_TROVE = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "guarded_trove")), //fixed room
            FALSE_BOUNTY = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "false_bounty")), //fixed room
            MOMENTS_REPRIEVE = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "moments_reprieve")), //fixed room
            MOOGLE_ROOM = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "moogle_room")), //fixed room
            PROSPEROUS_REPOSITORY = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "prosperous_repository")),
            TREACHEROUS_RESPOITORY = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "treacherous_repository")),
            REPOSEFUL_GROVE = () -> ModJsonRegistries.ROOM_TYPE.get().getValue(new ResourceLocation(KingdomKeys.MODID, "reposeful_grove")); //fixed room
}
