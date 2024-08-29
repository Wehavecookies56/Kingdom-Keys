package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class StatUpgradeItem extends Item implements IItemCategory {
	String boost;

	public StatUpgradeItem(Properties properties, String name) {
		super(properties);
		this.boost = name;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (!world.isClientSide) {
			PlayerData playerData = PlayerData.get(player);
			if (playerData != null ) {
				takeItem(player);
				
				switch (boost) {
				case Strings.powerBoost:
					playerData.getStrengthStat().addModifier("boost", 1, true, false);
					player.displayClientMessage(Component.translatable(Utils.translateToLocal("gui.statboost.increased"),Utils.translateToLocal(Strings.Gui_Menu_Status_Strength),playerData.getStrength(true)), true);
					break;
				case Strings.magicBoost:
					playerData.getMagicStat().addModifier("boost", 1, true, false);
					player.displayClientMessage(Component.translatable(Utils.translateToLocal("gui.statboost.increased"),Utils.translateToLocal(Strings.Gui_Menu_Status_Magic),playerData.getMagic(true)), true);
					break;
				case Strings.defenseBoost:
					playerData.getDefenseStat().addModifier("boost", 1, true, false);
					player.displayClientMessage(Component.translatable(Utils.translateToLocal("gui.statboost.increased"),Utils.translateToLocal(Strings.Gui_Menu_Status_Defense),playerData.getDefense(true)), true);
					break;
				case Strings.apBoost:
					playerData.getMaxAPStat().addModifier("boost", 1, true, false);
					player.displayClientMessage(Component.translatable(Utils.translateToLocal("gui.statboost.increased"),Utils.translateToLocal(Strings.Gui_Menu_Status_AP),playerData.getMaxAP(true)), true);
					break;
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
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
	public void appendHoverText(ItemStack stack, TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag flagIn) {
		if(Minecraft.getInstance().player != null) {			
			switch (boost) {
			case Strings.powerBoost:
				tooltip.add(Component.translatable(Utils.translateToLocal("gui.statboost.tooltip"),Utils.translateToLocal((Strings.Gui_Menu_Status_Strength))));
				break;
			case Strings.magicBoost:
				tooltip.add(Component.translatable(Utils.translateToLocal("gui.statboost.tooltip"),Utils.translateToLocal((Strings.Gui_Menu_Status_Magic))));
				break;
			case Strings.defenseBoost:
				tooltip.add(Component.translatable(Utils.translateToLocal("gui.statboost.tooltip"),Utils.translateToLocal((Strings.Gui_Menu_Status_Defense))));
				break;
			case Strings.apBoost:
				tooltip.add(Component.translatable(Utils.translateToLocal("gui.statboost.tooltip"),Utils.translateToLocal((Strings.Gui_Menu_Status_AP))));
				break;
			}
			
		}
		super.appendHoverText(stack, tooltipContext, tooltip, flagIn);
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.BUILDING;
	}
}
