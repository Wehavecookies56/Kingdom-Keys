package online.kingdomkeys.kingdomkeys.client.gui.elements.HUD;

import java.util.List;

/**
 * Where a HUDElement that Kingdom Keys doesn't own keeps its numbers.
 */
public interface HUDDataStorage {

	/**
	 * The nine saved numbers, or an empty list when nothing has been saved yet - the element falls back to its defaults in that case.
	 */
	List<? extends Number> load();

	/** X, Y, width, height, scaleX, scaleY, rotation, anchor ordinal, visible (1 or 0). */
	void save(List<Float> data);
}
