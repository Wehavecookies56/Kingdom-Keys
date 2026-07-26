package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.block.StruggleBoardBlock;
import online.kingdomkeys.kingdomkeys.entity.PosterEntity;

public class PosterItem extends Item {
	public PosterItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		Player player = context.getPlayer();
		Level level = context.getLevel();
		BlockPos clickedPos = context.getClickedPos();
		ItemStack stack = context.getItemInHand();

		if (player != null && player.isShiftKeyDown()) {
			BlockState clickedState = level.getBlockState(clickedPos);
			if (clickedState.getBlock() instanceof StruggleBoardBlock) {
				if (!level.isClientSide) {
					stack.set(ModComponents.POSTER_TARGET.get(), GlobalPos.of(level.dimension(), clickedPos));
					player.displayClientMessage(Component.translatable("kingdomkeys.item.struggle_poster.saved"), true);
				}
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
			return InteractionResult.PASS;
		}

		Direction face = context.getClickedFace();
		if (face.getAxis().isVertical()) {
			return InteractionResult.FAIL;
		}
		BlockPos posterPos = clickedPos.relative(face);
		BlockState posterState = level.getBlockState(posterPos);
		if (!posterState.canBeReplaced() && posterState.getBlock() != Blocks.AIR) {
			return InteractionResult.FAIL;
		}

		if (!level.isClientSide) {
			PosterEntity poster = new PosterEntity(level, posterPos, face);
			GlobalPos target = stack.get(ModComponents.POSTER_TARGET.get());
			if (target != null) {
				poster.setTarget(target);
			}
			if (poster.survives()) {
				poster.playPlacementSound();
				level.addFreshEntity(poster);
				if (player != null && !player.getAbilities().instabuild) {
					stack.shrink(1);
				}
			} else {
				return InteractionResult.FAIL;
			}
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}
}