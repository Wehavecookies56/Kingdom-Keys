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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MagicSpellItem extends Item implements IItemCategory {
	String magic;

	public MagicSpellItem(Properties properties, String name) {
		super(properties);
		this.magic = name;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		if (!world.isRemote) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			Magic magicInstance = ModMagic.registry.getValue(new ResourceLocation(magic));
			if (playerData != null && playerData.getMagicsMap() != null) {
				if (!playerData.getMagicsMap().containsKey(magic)) {
					playerData.getMagicsMap().put(magic, new int[] {0,0});
					takeItem(player);
					player.sendStatusMessage(new TranslationTextComponent("Unlocked " + Utils.translateToLocal(magicInstance.getTranslationKey())), true);
				} else {
					int actualLevel = playerData.getMagicLevel(magic);
					if(actualLevel < magicInstance.getMaxLevel()) {
						player.sendStatusMessage(new TranslationTextComponent(Utils.translateToLocal(magicInstance.getTranslationKey(actualLevel)) + " has been upgraded to "+Utils.translateToLocal(magicInstance.getTranslationKey(actualLevel+1))), true);
						playerData.getMagicsMap().put(magic, new int[] {actualLevel+1,0});
						takeItem(player);
					} else {
						player.sendStatusMessage(new TranslationTextComponent(Utils.translateToLocal(magicInstance.getTranslationKey(actualLevel)) + " is already at the max level"), true);
					}
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
		Magic magicInstance = ModMagic.registry.getValue(new ResourceLocation(magic));
		if(Minecraft.getInstance().player != null) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(Minecraft.getInstance().player);
	
			int actualLevel = playerData.getMagicLevel(magic);
			if(!playerData.getMagicsMap().containsKey(magic)) {
				actualLevel--;
			}
			
			if(actualLevel < magicInstance.getMaxLevel()) {
				tooltip.add(new TranslationTextComponent("Unlock " + Utils.translateToLocal(magicInstance.getTranslationKey(actualLevel+1))));
			} else {
				tooltip.add(new TranslationTextComponent(Utils.translateToLocal(magicInstance.getTranslationKey(actualLevel)) + " is the max level"));
			}
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.MISC;
	}
}
