package online.kingdomkeys.kingdomkeys.item;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.datagen.init.KeybladeStats.Recipe;
import online.kingdomkeys.kingdomkeys.lib.Lists;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeLevel;
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
			if (playerData != null && playerData.getMagicList() != null) {
				if (!playerData.getMagicList().contains(magic)) {
					playerData.addMagicToList(magic);
					if (!ItemStack.areItemStacksEqual(player.getHeldItemMainhand(), ItemStack.EMPTY) && player.getHeldItemMainhand().getItem() == this) {
						player.getHeldItemMainhand().shrink(1);
					} else if (!ItemStack.areItemStacksEqual(player.getHeldItemOffhand(), ItemStack.EMPTY) && player.getHeldItemOffhand().getItem() == this) {
						player.getHeldItemOffhand().shrink(1);
					}
					player.sendMessage(new TranslationTextComponent("Unlocked " + Utils.translateToLocal(magicInstance.getTranslationKey())), Util.DUMMY_UUID);
				} else {
					player.sendMessage(new TranslationTextComponent(Utils.translateToLocal(magicInstance.getTranslationKey()) + " Already unlocked"), Util.DUMMY_UUID);
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
			}
		}
		return ActionResult.resultSuccess(player.getHeldItem(hand));
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		Magic magicInstance = ModMagic.registry.getValue(new ResourceLocation(magic));
		tooltip.add(new TranslationTextComponent("Unlock " + Utils.translateToLocal(magicInstance.getTranslationKey())));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.MISC;
	}
}
