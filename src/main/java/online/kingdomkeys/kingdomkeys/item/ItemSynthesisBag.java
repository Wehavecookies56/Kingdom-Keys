package online.kingdomkeys.kingdomkeys.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.container.ContainerSynthesisBag;
import online.kingdomkeys.kingdomkeys.container.InventorySynthesisBag;
import online.kingdomkeys.kingdomkeys.handler.KeyboardHelper;

public class ItemSynthesisBag extends Item {

	public ItemSynthesisBag(Properties properties, String name) {
		super(properties);
		setRegistryName(KingdomKeys.MODID, name);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (!world.isRemote) {
			INamedContainerProvider container = new SimpleNamedContainerProvider((w, p, pl) -> new ContainerSynthesisBag(w, p, stack), stack.getDisplayName());
			NetworkHooks.openGui((ServerPlayerEntity) player, container, buf -> {
				buf.writeBoolean(hand == Hand.MAIN_HAND);
			});
		}
		return ActionResult.resultSuccess(player.getHeldItem(hand));
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		CompoundNBT nbt = stack.getOrCreateTag();
		int bagLevel = nbt.getInt("level");
		tooltip.add(new TranslationTextComponent("Level " + bagLevel));

		if (worldIn != null) { // So it does not crash D:
			if (!KeyboardHelper.isShiftDown()) {
				tooltip.add(new TranslationTextComponent(TextFormatting.ITALIC + "Hold down <SHIFT> for info"));
			} else {
				IItemHandlerModifiable bagInv = (IItemHandlerModifiable) stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
				if (bagInv != null) {
					for (int i = 0; i < bagInv.getSlots(); i++) {
						ItemStack itemStack = bagInv.getStackInSlot(i);
						if (!ItemStack.areItemsEqual(ItemStack.EMPTY, itemStack)) {
							tooltip.add(new TranslationTextComponent("- " + itemStack.getDisplayName().getFormattedText() + " x" + itemStack.getCount()));
						}
					}
				}
			}
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {
		return new InventorySynthesisBag();
	}

}
