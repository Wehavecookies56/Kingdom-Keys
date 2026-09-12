package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.level.block.LayeredCauldronBlock;

public final class ApprenticeCauldron {
	private ApprenticeCauldron() {}

	private static final CauldronInteraction WASH = (state, level, pos, player, hand, stack) -> {
		if (!(stack.getItem() instanceof UnionApprenticeArmorItem armor) || !armor.isDyed(stack)) {
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		}

		if (!level.isClientSide) {
			armor.clearColors(stack);
			player.awardStat(Stats.CLEAN_ARMOR);
			LayeredCauldronBlock.lowerFillLevel(state, level, pos);
		}

		return ItemInteractionResult.sidedSuccess(level.isClientSide);
	};

	public static void register() {
		for (ArmorItem.Type slot : new ArmorItem.Type[] { ArmorItem.Type.CHESTPLATE, ArmorItem.Type.LEGGINGS, ArmorItem.Type.BOOTS }) {
			CauldronInteraction.WATER.map().put(ModItems.getApprenticeArmor(slot), WASH);
		}
	}
}
