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
            ENTRANCE_HALL = () -> registry.get().getValue(KingdomKeys.rl("entrance_hall")),
            CONQUERORS_RESPITE = () -> registry.get().getValue(KingdomKeys.rl("conquerors_respite")),
            UNKNOWN_ROOM = () -> registry.get().getValue(KingdomKeys.rl("unknown_room")),

            //Enemy
            TRANQUIL_DARKNESS = () -> registry.get().getValue(KingdomKeys.rl("tranquil_darkness")),
            TEEMING_DARKNESS = () -> registry.get().getValue(KingdomKeys.rl("teeming_darkness")),
            FEEBLE_DARKNESS = () -> registry.get().getValue(KingdomKeys.rl("feeble_darkness")), //add weakness modifier
            ALMIGHTY_DARKNESS = () -> registry.get().getValue(KingdomKeys.rl("almighty_darkness")), //strength modifier
            SLEEPING_DARKNESS = () -> registry.get().getValue(KingdomKeys.rl("sleeping_darkness")), //stop modifier
            LOOMING_DARKNESS = () -> registry.get().getValue(KingdomKeys.rl("looming_darkness")), //speed modifier
            BOTTOMLESS_DARKNESS = () -> registry.get().getValue(KingdomKeys.rl("bottomless_darkness")), //blindness modifier

            //Status
            MARTIAL_WAKING = () -> registry.get().getValue(KingdomKeys.rl("martial_waking")), //player strength modifier
            SORCEROUS_WAKING = () -> registry.get().getValue(KingdomKeys.rl("sorcerous_waking")), //magic modifier
            ALCHEMIC_WAKING = () -> registry.get().getValue(KingdomKeys.rl("alchemic_waking")), //item modifier
            STAGNANT_SPACE = () -> registry.get().getValue(KingdomKeys.rl("stagnant_space")), //slow modifier
            WEIGHTLESS_SPACE = () -> registry.get().getValue(KingdomKeys.rl("weightless_space")), //jump modifier

            //Bounty
            CALM_BOUNTY = () -> registry.get().getValue(KingdomKeys.rl("calm_bounty")), //fixed room
            GUARDED_TROVE = () -> registry.get().getValue(KingdomKeys.rl("guarded_trove")), //fixed room
            FALSE_BOUNTY = () -> registry.get().getValue(KingdomKeys.rl("false_bounty")), //fixed room
            MOMENTS_REPRIEVE = () -> registry.get().getValue(KingdomKeys.rl("moments_reprieve")), //fixed room
            MOOGLE_ROOM = () -> registry.get().getValue(KingdomKeys.rl("moogle_room")), //fixed room
            PROSPEROUS_REPOSITORY = () -> registry.get().getValue(KingdomKeys.rl("prosperous_repository")),
            TREACHEROUS_RESPOITORY = () -> registry.get().getValue(KingdomKeys.rl("treacherous_repository")),
            REPOSEFUL_GROVE = () -> registry.get().getValue(KingdomKeys.rl("reposeful_grove")), //fixed room

            //Key
            ROOM_OF_BEGINNINGS = () -> registry.get().getValue(KingdomKeys.rl("room_of_beginnings")),
            ROOM_OF_GUIDANCE = () -> registry.get().getValue(KingdomKeys.rl("room_of_guidance")),
            ROOM_OF_TRUTH = () -> registry.get().getValue(KingdomKeys.rl("room_of_truth")),
            ROOM_OF_REWARDS = () -> registry.get().getValue(KingdomKeys.rl("room_of_rewards"));
}
