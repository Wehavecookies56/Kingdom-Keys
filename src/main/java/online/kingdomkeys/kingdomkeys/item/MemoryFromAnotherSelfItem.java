package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.world.HeartlessForm;

import java.util.List;
import java.util.UUID;

public class MemoryFromAnotherSelfItem extends Item {

	public static final String NOT_YOURS = "kingdomkeys.memory_from_another_self.not_yours";
	public static final String NOTHING_LOST = "kingdomkeys.memory_from_another_self.nothing_lost";
	public static final String RECOVERED = "kingdomkeys.memory_from_another_self.recovered";
	public static final String USE = "kingdomkeys.memory_from_another_self.use";

	public MemoryFromAnotherSelfItem(Properties properties) {
		super(properties);
	}

	public static UUID owner(ItemStack stack) {
		CompoundTag tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
		return tag.hasUUID(HeartlessForm.MEMORY_OWNER) ? tag.getUUID(HeartlessForm.MEMORY_OWNER) : null;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		Player player = Minecraft.getInstance().player;
		if (player != null && player.getUUID().equals(owner(stack))) {
			tooltip.add(Component.translatable(USE).withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (level.isClientSide || !(player instanceof ServerPlayer serverPlayer)) {
			return InteractionResultHolder.success(stack);
		}

		PlayerData data = PlayerData.get(player);
		if (!player.getUUID().equals(owner(stack))) {
			player.displayClientMessage(Component.translatable(NOT_YOURS), true);
			return InteractionResultHolder.fail(stack);
		}
		if (data == null || !HeartlessForm.hasLostStats(data)) {
			player.displayClientMessage(Component.translatable(NOTHING_LOST), true);
			return InteractionResultHolder.fail(stack);
		}

		HeartlessForm.recoverStats(data);
		stack.consume(1, player);
		level.playSound(null, player.blockPosition(), SoundEvents.AMETHYST_BLOCK_RESONATE, SoundSource.PLAYERS, 1F, 1.2F);
		player.displayClientMessage(Component.translatable(RECOVERED), true);
		PacketHandler.syncToAllAround(serverPlayer, data);
		return InteractionResultHolder.consume(stack);
	}
}
