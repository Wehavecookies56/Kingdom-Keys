package online.kingdomkeys.kingdomkeys.item;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.container.ContainerSynthesisBag;
import online.kingdomkeys.kingdomkeys.container.InventorySynthesisBagS;

public class ItemSynthesisBag extends Item{
	
	public ItemSynthesisBag(Properties properties, String name) {
		super(properties);
        setRegistryName(KingdomKeys.MODID, name);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		if (!world.isRemote) {
			ItemStack stack = player.getHeldItem(hand);
			//CompoundNBT nbt = stack.getOrCreateTag();
			//nbt.putInt("level", 2);
			
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
    	tooltip.add(new TranslationTextComponent("Level "+bagLevel));        
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
	public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT oldCapNbt) {
		return new InventorySynthesisBagS();
	}
  
}
