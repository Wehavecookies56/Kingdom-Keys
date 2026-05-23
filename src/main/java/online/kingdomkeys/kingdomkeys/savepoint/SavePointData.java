package online.kingdomkeys.kingdomkeys.savepoint;

import net.minecraft.resources.ResourceLocation;

import java.util.EnumSet;
import java.util.Map;

public class SavePointData {

	private final Map<SavePointStat, ResourceLocation> materials;
	private final EnumSet<SavePointStat> restores;

	public SavePointData(Map<SavePointStat, ResourceLocation> materials, EnumSet<SavePointStat> restores) {
		this.materials = materials;
		this.restores = restores;
	}

	public Map<SavePointStat, ResourceLocation> getMaterials() {
		return materials;
	}

	public EnumSet<SavePointStat> getRestores() {
		return restores;
	}

	public boolean restores(SavePointStat stat) {
		return restores.contains(stat);
	}

	public enum SavePointStat {
		HP,
		MP,
		HUNGER,
		FOCUS,
		DRIVE,
		TIER
	}
}

