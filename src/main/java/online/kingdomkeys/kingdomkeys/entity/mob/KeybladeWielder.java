package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.world.item.ItemStack;

public interface KeybladeWielder {
	KeybladeSummon keybladeSummon();
	ItemStack keybladeToCall();
	int callingRank();

	default boolean hasKeyblade() {
		return keybladeSummon().hasKeyblade();
	}

	// True while they are calling it and have not got it yet
	default boolean isSummoning() {
		return keybladeSummon().isSummoning();
	}

	// If true will punch, if false will flee
	default boolean holdsGroundUnarmed() {
		return KeybladeSummon.standsItsGround(callingRank());
	}

	default void callKeyblade() {
		keybladeSummon().begin();
	}

	default void dismissKeyblade() {
		keybladeSummon().dismiss();
	}
}
