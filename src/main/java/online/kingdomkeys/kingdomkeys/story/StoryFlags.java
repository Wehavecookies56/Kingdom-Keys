package online.kingdomkeys.kingdomkeys.story;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public final class StoryFlags {

	private StoryFlags() {}

	/**
	 * A visit is owed.
	 *
	 * <p>Set the moment the first keyblade is synthesised, cleared when the master actually turns
	 * up. The two are separate because he comes at dawn, so between the one and the other there is
	 * a night in which the player may be down a mineshaft, and a debt that has to survive it.</p>
	 */
	public static final ResourceLocation FORETELLER_OWED = KingdomKeys.rl("story/foreteller_owed");

	/** The master has come. Guards the whole chain against happening twice. */
	public static final ResourceLocation FORETELLER_VISITED = KingdomKeys.rl("story/foreteller_visited");

	/**
	 * The apprenticeship is over: taught, and home again, with the master's way shut behind them.
	 *
	 * <p>Set when the standing portal closes itself, which only happens once the pupil has earned the
	 * middle lesson and walked back out. From that moment Daybreak Town is a place you travel to, not
	 * a place you were taken, so anything meant for a wielder who returns under their own power hangs
	 * off this rather than off the lesson alone.</p>
	 */
	public static final ResourceLocation INTRODUCTORY_TRAINING_DONE = KingdomKeys.rl("story/training_done");
}
