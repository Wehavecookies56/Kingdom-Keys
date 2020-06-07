package online.kingdomkeys.kingdomkeys.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

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
			if (props != null && props.getDriveFormsMap() != null) {
				if (props.getDriveFormsMap().containsKey(formName)) { // If you have the form add some exp
					int level = props.getDriveFormsMap().containsKey(formName) ? props.getDriveFormsMap().get(formName)[0] + 1 : 1;
					if (level <= 7) {
						int exp = ModDriveForms.registry.getValue(new ResourceLocation(formName)).getLevelUpCost(level);
						int oldExp = 0;
						if (level > 1) {
							oldExp = ModDriveForms.registry.getValue(new ResourceLocation(formName)).getLevelUpCost(level - 1);
						}
						int newExp = exp - oldExp;
						props.setDriveFormExp(player, formName, props.getDriveFormExp(formName) + Math.max(newExp / 10, 1));
						player.sendMessage(new TranslationTextComponent(formName.substring(formName.indexOf(":") + 1) + " has got +" + Math.max(newExp / 10, 1) + " exp"));
					}

				} else {// If you don't have the form unlock it
					props.setDriveFormLevel(formName, 1);
					player.sendMessage(new TranslationTextComponent("Unlocked " + formName.substring(formName.indexOf(":") + 1)));
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.get(player)), (ServerPlayerEntity) player);
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
