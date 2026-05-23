package online.kingdomkeys.kingdomkeys.savepoint;

import net.minecraft.resources.ResourceLocation;

public class SavePoint {

	private final ResourceLocation id;

	private SavePointData data;

	public SavePoint(ResourceLocation id) {
		this.id = id;
	}

	public ResourceLocation getId() {
		return id;
	}

	public SavePointData getData() {
		return data;
	}

	public void setData(SavePointData data) {
		this.data = data;
	}
}