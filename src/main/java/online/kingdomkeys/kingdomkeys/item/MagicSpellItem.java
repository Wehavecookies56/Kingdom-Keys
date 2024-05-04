package online.kingdomkeys.kingdomkeys.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
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
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		Magic magicInstance = ModMagic.registry.get().getValue(new ResourceLocation(magic));

		if (!world.isClientSide) {
			if (playerData != null && playerData.getMagicsMap() != null) {
				if (!playerData.getMagicsMap().containsKey(magic)) {
					playerData.getMagicsMap().put(magic, new int[] {0,0});
					takeItem(player);
					player.displayClientMessage(Component.translatable("message.unlocked", Utils.translateToLocal(magicInstance.getTranslationKey())), true);
				} else {
					int actualLevel = playerData.getMagicLevel(new ResourceLocation(magic));
					if(actualLevel < magicInstance.getMaxLevel()) {
						player.displayClientMessage(Component.translatable("message.magic_upgrade",Utils.translateToLocal(magicInstance.getTranslationKey(actualLevel)),Utils.translateToLocal(magicInstance.getTranslationKey(actualLevel+1))), true);
						playerData.getMagicsMap().put(magic, new int[] {actualLevel+1,0});
						takeItem(player);
					} else {
						player.displayClientMessage(Component.translatable("message.magic_max_level",Utils.translateToLocal(magicInstance.getTranslationKey(actualLevel))), true);
					}
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
			}
		} else { //For the client side
			if (!playerData.getMagicsMap().containsKey(magic)) { // If the magic is not on the list
				if(!ModConfigs.magicDisplayedInCommandMenu.contains(magic)) {
					List<String> list = new ArrayList<>(ModConfigs.magicDisplayedInCommandMenu);
					list.add(magic);
					ModConfigs.setMagicDisplayedInCommandMenu(list);
				}
			}
		}
			
		return InteractionResultHolder.success(player.getItemInHand(hand));
	}

	private void takeItem(Player player) {
		if (!ItemStack.matches(player.getMainHandItem(), ItemStack.EMPTY) && player.getMainHandItem().getItem() == this) {
			player.getMainHandItem().shrink(1);
		} else if (!ItemStack.matches(player.getOffhandItem(), ItemStack.EMPTY) && player.getOffhandItem().getItem() == this) {
			player.getOffhandItem().shrink(1);
		}
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		Magic magicInstance = ModMagic.registry.get().getValue(new ResourceLocation(magic));
		if(Minecraft.getInstance().player != null) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(Minecraft.getInstance().player);
	
			int actualLevel = playerData.getMagicLevel(new ResourceLocation(magic));
			if(!playerData.getMagicsMap().containsKey(magic)) {
				actualLevel--;
			}
			
			if(actualLevel < magicInstance.getMaxLevel()) {
				tooltip.add(Component.translatable(Utils.translateToLocal("gui.magicspell.unlock",Utils.translateToLocal(magicInstance.getTranslationKey(actualLevel+1)))));
			} else {
				tooltip.add(Component.translatable(Utils.translateToLocal("gui.magicspell.maxed",Utils.translateToLocal(magicInstance.getTranslationKey(actualLevel)))));
			}
		}
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.MISC;
	}
}
