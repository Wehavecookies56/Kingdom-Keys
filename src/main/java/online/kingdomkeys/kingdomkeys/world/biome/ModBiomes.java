package online.kingdomkeys.kingdomkeys.world.biome;

import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModBiomes {

	public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, KingdomKeys.MODID);

	static {
		BIOMES.register(Strings.diveToTheHeart, OverworldBiomes::theVoid);
		BIOMES.register(Strings.stationOfSorrow, OverworldBiomes::theVoid);
	}
}
