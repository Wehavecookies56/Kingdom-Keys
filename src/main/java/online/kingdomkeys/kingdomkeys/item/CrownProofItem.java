package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.CrownTier;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

import java.util.List;

public class CrownProofItem extends Item implements ICreativeTab {
	public CrownProofItem(Properties properties) {
		super(properties);
	}

	@Override
	public Tab getTab() {
		return Tab.MISC;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		PlayerData playerData = PlayerData.get(player);

		if (playerData == null) {
			return InteractionResultHolder.pass(stack);
		}

		CrownTier tier = CrownTier.next(playerData);
		if (tier == null) {
			if (!level.isClientSide) {
				player.displayClientMessage(Component.translatable("gui.crownproof.allunlocked"), true);
			}
			return InteractionResultHolder.pass(stack); // nothing granted, so nothing is spent either
		}

		if (!level.isClientSide) {
			playerData.unlockCrown(tier.getName());

			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}

			level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.6F, 1.4F);
			player.displayClientMessage(Component.translatable("gui.crownproof.unlocked", Component.translatable(tier.getTranslationKey())), true);
			PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
		}

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("gui.crownproof.desc"));
		tooltip.add(Component.translatable("gui.crownproof.desc2"));
		super.appendHoverText(stack, tooltipContext, tooltip, flag);
	}
}
