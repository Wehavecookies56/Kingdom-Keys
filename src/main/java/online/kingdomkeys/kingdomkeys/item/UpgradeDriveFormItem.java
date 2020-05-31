package online.kingdomkeys.kingdomkeys.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.packet.PacketSyncCapability;

public class UpgradeDriveFormItem extends Item {
	String formName;

	public UpgradeDriveFormItem(Properties properties, String name) {
		super(properties);
		this.formName = name;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		if (!world.isRemote) {
			IPlayerCapabilities props = ModCapabilities.get(player);
			if (props.getDriveFormsMap() != null) {
				int level = props.getDriveFormsMap().containsKey(formName) ? props.getDriveFormsMap().get(formName)[0] + 1 : 1;
				if(level <=7) {
					props.setDriveFormLevel(formName, level);
					PacketHandler.sendTo(new PacketSyncCapability(ModCapabilities.get(player)), (ServerPlayerEntity) player);
				}
			}
		}
		return ActionResult.resultSuccess(player.getHeldItem(hand));
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("Upgrade " + formName));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
