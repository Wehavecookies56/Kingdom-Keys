package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

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
            FEEBLE_DARKNESS = () -> registry.get().getValue(KingdomKeys.rl("feeble_darkness")),
            ALMIGHTY_DARKNESS = () -> registry.get().getValue(KingdomKeys.rl("almighty_darkness")),
            SLEEPING_DARKNESS = () -> registry.get().getValue(KingdomKeys.rl("sleeping_darkness")),
            LOOMING_DARKNESS = () -> registry.get().getValue(KingdomKeys.rl("looming_darkness")),
            WHITE_ROOM = () -> registry.get().getValue(KingdomKeys.rl("white_room")),
            BLACK_ROOM = () -> registry.get().getValue(KingdomKeys.rl("black_room")),
            BOTTOMLESS_DARKNESS = () -> registry.get().getValue(KingdomKeys.rl("bottomless_darkness")),
            ROULETTE_ROOM = () -> registry.get().getValue(KingdomKeys.rl("roulette_room")),

            //Status
            MARTIAL_WAKING = () -> registry.get().getValue(KingdomKeys.rl("martial_waking")),
            SORCEROUS_WAKING = () -> registry.get().getValue(KingdomKeys.rl("sorcerous_waking")),
            ALCHEMIC_WAKING = () -> registry.get().getValue(KingdomKeys.rl("alchemic_waking")),
            STAGNANT_SPACE = () -> registry.get().getValue(KingdomKeys.rl("stagnant_space")),
            WEIGHTLESS_SPACE = () -> registry.get().getValue(KingdomKeys.rl("weightless_space")),

            //Bounty
            CALM_BOUNTY = () -> registry.get().getValue(KingdomKeys.rl("calm_bounty")),
            GUARDED_TROVE = () -> registry.get().getValue(KingdomKeys.rl("guarded_trove")),
            FALSE_BOUNTY = () -> registry.get().getValue(KingdomKeys.rl("false_bounty")),
            MOMENTS_REPRIEVE = () -> registry.get().getValue(KingdomKeys.rl("moments_reprieve")),
            MOOGLE_ROOM = () -> registry.get().getValue(KingdomKeys.rl("moogle_room")),
            PROSPEROUS_REPOSITORY = () -> registry.get().getValue(KingdomKeys.rl("prosperous_repository")),
            TREACHEROUS_RESPOITORY = () -> registry.get().getValue(KingdomKeys.rl("treacherous_repository")),
            REPOSEFUL_GROVE = () -> registry.get().getValue(KingdomKeys.rl("reposeful_grove")),

            //Key
            ROOM_OF_BEGINNINGS = () -> registry.get().getValue(KingdomKeys.rl("room_of_beginnings")),
            ROOM_OF_GUIDANCE = () -> registry.get().getValue(KingdomKeys.rl("room_of_guidance")),
            ROOM_OF_TRUTH = () -> registry.get().getValue(KingdomKeys.rl("room_of_truth")),
            ROOM_OF_REWARDS = () -> registry.get().getValue(KingdomKeys.rl("room_of_rewards"));
}
