package online.kingdomkeys.kingdomkeys.world;

import java.util.function.BiFunction;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class KKDimension extends ModDimension {

	String dimension;
	
	public KKDimension(String bName) {
		this.dimension = bName;
	}

	@Override
	public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
		switch(dimension) {
		case Strings.twilightTown:
			return TraverseTownDimension::new;
		}
		return null;
		
	}

}
