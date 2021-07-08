package online.kingdomkeys.kingdomkeys.item;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class StatUpgradeItem extends Item implements IItemCategory {
	String boost;

	public StatUpgradeItem(Properties properties, String name) {
		super(properties);
		this.boost = name;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		if (!world.isRemote) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if (playerData != null ) {
				takeItem(player);
				
				switch (boost) {
				case Strings.powerBoost:
					playerData.setBoostStrength(playerData.getBoostStrength()+1);
					player.sendStatusMessage(new TranslationTextComponent("Increased Strength by 1, it is now "+playerData.getStrength(true)), true);
					break;
				case Strings.magicBoost:
					playerData.setBoostMagic(playerData.getBoostMagic()+1);
					player.sendStatusMessage(new TranslationTextComponent("Increased Magic by 1, it is now "+playerData.getMagic(true)), true);
					break;
				case Strings.defenseBoost:
					playerData.setBoostDefense(playerData.getBoostDefense()+1);
					player.sendStatusMessage(new TranslationTextComponent("Increased Defense by 1, it is now "+playerData.getDefense(true)), true);
					break;
				case Strings.apBoost:
					playerData.setBoostMaxAP(playerData.getBoostMaxAP()+1);
					player.sendStatusMessage(new TranslationTextComponent("Increased Max AP by 1, it is now "+playerData.getMaxAP(true)), true);
					break;
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
			}
		}
		return ActionResult.resultSuccess(player.getHeldItem(hand));
	}

	private void takeItem(PlayerEntity player) {
		if (!ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), ItemStack.EMPTY) && player.getHeldItemMainhand().getItem() == this) {
			player.getHeldItemMainhand().shrink(1);
		} else if (!ItemStack.areItemStacksEqual(player.getHeldItemOffhand(), ItemStack.EMPTY) && player.getHeldItemOffhand().getItem() == this) {
			player.getHeldItemOffhand().shrink(1);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		if(Minecraft.getInstance().player != null) {			
			switch (boost) {
			case Strings.powerBoost:
				tooltip.add(new TranslationTextComponent("Increases Strength by 1"));
				break;
			case Strings.magicBoost:
				tooltip.add(new TranslationTextComponent("Increases Magic by 1"));
				break;
			case Strings.defenseBoost:
				tooltip.add(new TranslationTextComponent("Increases Defense by 1"));
				break;
			case Strings.apBoost:
				tooltip.add(new TranslationTextComponent("Increases Max AP by 1"));
				break;
			}
			
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.BUILDING;
	}
}
