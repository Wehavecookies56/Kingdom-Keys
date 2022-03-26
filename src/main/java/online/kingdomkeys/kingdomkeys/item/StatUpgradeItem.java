package online.kingdomkeys.kingdomkeys.item;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		if (!world.isClientSide) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if (playerData != null ) {
				takeItem(player);
				
				switch (boost) {
				case Strings.powerBoost:
					playerData.setBoostStrength(playerData.getBoostStrength()+1);
					player.displayClientMessage(new TranslatableComponent("Increased Strength by 1, it is now "+playerData.getStrength(true)), true);
					break;
				case Strings.magicBoost:
					playerData.setBoostMagic(playerData.getBoostMagic()+1);
					player.displayClientMessage(new TranslatableComponent("Increased Magic by 1, it is now "+playerData.getMagic(true)), true);
					break;
				case Strings.defenseBoost:
					playerData.setBoostDefense(playerData.getBoostDefense()+1);
					player.displayClientMessage(new TranslatableComponent("Increased Defense by 1, it is now "+playerData.getDefense(true)), true);
					break;
				case Strings.apBoost:
					playerData.setBoostMaxAP(playerData.getBoostMaxAP()+1);
					player.displayClientMessage(new TranslatableComponent("Increased Max AP by 1, it is now "+playerData.getMaxAP(true)), true);
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
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		if(Minecraft.getInstance().player != null) {			
			switch (boost) {
			case Strings.powerBoost:
				tooltip.add(new TranslatableComponent("Increases Strength by 1"));
				break;
			case Strings.magicBoost:
				tooltip.add(new TranslatableComponent("Increases Magic by 1"));
				break;
			case Strings.defenseBoost:
				tooltip.add(new TranslatableComponent("Increases Defense by 1"));
				break;
			case Strings.apBoost:
				tooltip.add(new TranslatableComponent("Increases Max AP by 1"));
				break;
			}
			
		}
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public ItemCategory getCategory() {
		return ItemCategory.BUILDING;
	}
}
