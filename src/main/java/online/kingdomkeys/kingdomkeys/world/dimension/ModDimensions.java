package online.kingdomkeys.kingdomkeys.world.dimension;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.world.dimension.battle_arena.BattleArenaBiomeProvider;
import online.kingdomkeys.kingdomkeys.world.dimension.battle_arena.BattleArenaChunkGenerator;
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartBiomeProvider;
import online.kingdomkeys.kingdomkeys.world.dimension.dive_to_the_heart.DiveToTheHeartChunkGenerator;

@Mod.EventBusSubscriber
public class ModDimensions {
    public static final RegistryKey<World> DIVE_TO_THE_HEART = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(KingdomKeys.MODID, "dive_to_the_heart"));
    public static final RegistryKey<World> BATTLE_ARENA = RegistryKey.getOrCreateKey(Registry.WORLD_KEY, new ResourceLocation(KingdomKeys.MODID, "battle_arena"));

    public static void setupDimension() {
        DiveToTheHeartChunkGenerator.registerChunkGenerator();
        DiveToTheHeartBiomeProvider.registerBiomeProvider();
        
        BattleArenaChunkGenerator.registerChunkGenerator();
        BattleArenaBiomeProvider.registerBiomeProvider();
    }
}
