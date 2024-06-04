package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.container.SynthesisBagContainer;
import online.kingdomkeys.kingdomkeys.container.SynthesisBagInventory;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class SynthesisBagItem extends Item implements IItemCategory {

	public SynthesisBagItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!world.isClientSide) {
			PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.getPlayer(player)), (ServerPlayer)player);
			MenuProvider container = new SimpleMenuProvider((w, p, pl) -> new SynthesisBagContainer(w, p, stack), stack.getHoverName());
			NetworkHooks.openScreen((ServerPlayer) player, container, buf -> {
				buf.writeBoolean(hand == InteractionHand.MAIN_HAND);
			});
		}
		return InteractionResultHolder.consume(player.getItemInHand(hand));
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		CompoundTag nbt = stack.getOrCreateTag();
		int bagLevel = nbt.getInt("level");
		tooltip.add(Component.translatable(Utils.translateToLocal(Strings.Gui_Menu_Status_Level)+" "+(bagLevel+1)));
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundTag oldCapNbt) {
		return new SynthesisBagInventory();
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.TOOL;
	}
}
