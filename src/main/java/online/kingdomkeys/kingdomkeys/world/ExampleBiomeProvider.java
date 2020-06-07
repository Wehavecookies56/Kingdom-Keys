package online.kingdomkeys.kingdomkeys.world;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;

public class ExampleBiomeProvider extends BiomeProvider {

	private Random rand;

	public ExampleBiomeProvider() {
		super(biomeList);
		rand = new Random();
	}

	private static final Set<Biome> biomeList = ImmutableSet.of(ModBiomes.traverseTownBiome.get());

	@Override
	public Biome getNoiseBiome(int x, int y, int z) {
		return ModBiomes.traverseTownBiome.get();
	}

}

