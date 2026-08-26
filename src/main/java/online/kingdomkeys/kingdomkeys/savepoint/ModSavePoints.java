package online.kingdomkeys.kingdomkeys.savepoint;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.HashMap;
import java.util.Map;

public class ModSavePoints {
	public static final Map<ResourceLocation, SavePoint> registry = new HashMap<>();
	public static final SavePoint NORMAL = register("normal");
	public static final SavePoint LINKED = register("linked");
	public static final SavePoint WARP = register("warp");

	private static SavePoint register(String name) {
		ResourceLocation rl = KingdomKeys.rl(name);
		SavePoint point = new SavePoint(rl);
		registry.put(rl, point);
		return point;
	}
}
