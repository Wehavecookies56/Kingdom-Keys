package online.kingdomkeys.kingdomkeys.world.dimension;

import java.util.function.BiFunction;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class KingdomKeysModDimension extends ModDimension {

	String dimension;
	
	public KingdomKeysModDimension(String bName) {
		this.dimension = bName;
	}

	@Override
	public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
		switch(dimension) {
			case Strings.diveToTheHeart:
				return DiveToTheHeartDimension::new;
		}
		return null;
		
	}

}
