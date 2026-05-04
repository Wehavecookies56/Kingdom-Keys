package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.api.item.IItemCategory;
import online.kingdomkeys.kingdomkeys.api.item.ItemCategory;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class MagicSpellItem extends Item implements IItemCategory {
	String magic;
	int level;

	public MagicSpellItem(Properties properties, String name, int level) {
		super(properties.stacksTo(1));
		this.magic = name;
		this.level = level;
	}

	public int getLevel(){
		return level;
	}

	public String getMagic() {
		return magic;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		Magic magicInstance = ModMagic.registry.get(ResourceLocation.parse(magic));
		player.displayClientMessage(Component.translatable("gui.magicspell.equip", Utils.translateToLocal(magicInstance.getTranslationKey(getLevel()))), true);
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
	public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
		Magic magicInstance = ModMagic.registry.get(ResourceLocation.parse(magic));
		if(Minecraft.getInstance().player != null) {
			tooltip.add(Component.translatable("gui.magicspell.equip", Utils.translateToLocal(magicInstance.getTranslationKey(getLevel()))));

			/*PlayerData playerData = PlayerData.get(Minecraft.getInstance().player);
	
			int actualLevel = playerData.getMagicLevel(ResourceLocation.parse(magic));
			if(!playerData.getMagicsMap().containsKey(magic)) {
				actualLevel--;
			}
			
			if(actualLevel < magicInstance.getLevel()) {
				tooltip.add(Component.translatable("gui.magicspell.unlock",Utils.translateToLocal(magicInstance.getTranslationKey(actualLevel+1))));
			} else {
				tooltip.add(Component.translatable("gui.magicspell.maxed",Utils.translateToLocal(magicInstance.getTranslationKey(actualLevel))));
			}*/
		}
		super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.MISC;
	}
}
