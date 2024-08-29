package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.Size2i;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomProperties.RoomEnemies;
import online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.RoomProperties.RoomSize;

import java.awt.*;
import java.util.function.Supplier;

public class ModRoomTypes {

    public static DeferredRegister<RoomType> ROOM_TYPES = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "roomtypes"), KingdomKeys.MODID);

    public static final ResourceKey<Registry<RoomType>> ROOM_TYPES_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "roomtypes"));
    public static Registry<RoomType> registry = new RegistryBuilder<>(ROOM_TYPES_KEY).sync(true).defaultKey(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "empty")).create();

    //TODO create modifiers
    public static final Supplier<RoomType>
        //Special
        LOBBY = createRoomType("lobby", new RoomProperties.Builder(RoomSize.SPECIAL, RoomProperties.RoomCategory.SPECIAL, new Size2i(33, 69)).isLobby()),

        //Enemy
        TRANQUIL_DARKNESS = createRoomType("tranquil_darkness", RoomProperties.enemy(RoomSize.M).enemies(RoomEnemies.S)),
        TEEMING_DARKNESS = createRoomType("teeming_darkness", RoomProperties.enemy(RoomSize.L).enemies(RoomEnemies.L)),
        FEEBLE_DARKNESS = createRoomType("feeble_darkness", RoomProperties.enemy(RoomSize.M).enemies(RoomEnemies.S)), //add weakness modifier
        ALMIGHTY_DARKNESS = createRoomType("almighty_darkness", RoomProperties.enemy(RoomSize.M).enemies(RoomEnemies.M)), //strength modifier
        SLEEPING_DARKNESS = createRoomType("sleeping_darkness", RoomProperties.enemy(RoomSize.S).enemies(RoomEnemies.S)), //stop modifier
        LOOMING_DARKNESS = createRoomType("looming_darkness", RoomProperties.enemy(RoomSize.L).enemies(RoomEnemies.M)), //speed modifier
        BOTTOMLESS_DARKNESS = createRoomType("bottomless_darkness", RoomProperties.enemy(RoomSize.L).enemies(RoomEnemies.L).colour(Color.BLACK)), //blindness modifier

        //Status
        MARTIAL_WAKING = createRoomType("martial_waking", RoomProperties.status(RoomSize.M).enemies(RoomEnemies.M)), //player strength modifier
        SORCEROUS_WAKING = createRoomType("sorcerous_waking", RoomProperties.status(RoomSize.M).enemies(RoomEnemies.M)), //magic modifier
        ALCHEMIC_WAKING = createRoomType("alchemic_waking", RoomProperties.status(RoomSize.M).enemies(RoomEnemies.S)), //item modifier
        STAGNANT_SPACE = createRoomType("stagnant_space", RoomProperties.status(RoomSize.M).enemies(RoomEnemies.S)), //slow modifier
        WEIGHTLESS_SPACE = createRoomType("weightless_space", RoomProperties.status(RoomSize.M).enemies(RoomEnemies.S)), //jump modifier

        //Bounty
        CALM_BOUNTY = createRoomType("calm_bounty", RoomProperties.bounty(RoomSize.S)), //fixed room
        GUARDED_TROVE = createRoomType("guarded_trove", RoomProperties.bounty(RoomSize.S).enemies(RoomEnemies.M)), //fixed room
        FALSE_BOUNTY = createRoomType("false_bounty", RoomProperties.bounty(RoomSize.S).enemies(RoomEnemies.S)), //fixed
        MOMENTS_REPRIEVE = createRoomType("moments_reprieve", RoomProperties.bounty(RoomSize.S)), //fixed
        MOOGLE_ROOM = createRoomType("moogle_room", RoomProperties.bounty(RoomSize.S)), //fixed
        PROSPEROUS_REPOSITORY = createRoomType("prosperous_repository", RoomProperties.bounty(RoomSize.S)),
        TREACHEROUS_RESPOITORY = createRoomType("treacherous_repository", RoomProperties.bounty(RoomSize.S).enemies(RoomEnemies.M)),
        REPOSEFUL_GROVE = createRoomType("reposeful_grove", RoomProperties.bounty(RoomSize.M)); //fixed


    public static Supplier<RoomType> createRoomType(String name, RoomProperties.Builder properties) {
        return ROOM_TYPES.register(name, () -> new RoomType(properties).setRegistryName(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, name)));
    }
}
