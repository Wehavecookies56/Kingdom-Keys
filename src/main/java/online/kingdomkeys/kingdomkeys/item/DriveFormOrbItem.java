package online.kingdomkeys.kingdomkeys.item;

import java.util.List;

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
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class DriveFormOrbItem extends Item {
	String formName;

	public DriveFormOrbItem(Properties properties, String name) {
		super(properties);
		this.formName = name;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {		
		if (!world.isClientSide) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if (playerData != null && playerData.getDriveFormMap() != null) {
				DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(formName));
				playerData.addVisibleDriveForm(formName);

				if (playerData.getDriveFormMap().containsKey(formName)) { // If you have the form add some exp
					int level = playerData.getDriveFormMap().containsKey(formName) ? playerData.getDriveFormMap().get(formName)[0] + 1 : 1;
					if (level <= 7) {
						int exp = form.getLevelUpCost(level);
						int oldExp = 0;
						if (level > 1) {
							oldExp = form.getLevelUpCost(level - 1);
						}
						int newExp = exp - oldExp;
						int finalExp = Math.max(newExp / 10, 1);
						playerData.setDriveFormExp(player, formName, playerData.getDriveFormExp(formName) + finalExp);
						String message = Utils.translateToLocal("gui.driveformorb.upgrade",Utils.translateToLocal(form.getTranslationKey()),finalExp);
						player.displayClientMessage(Component.translatable(message), true);
						
						if(!ItemStack.matches(player.getMainHandItem(), ItemStack.EMPTY) && player.getMainHandItem().getItem() == this) {
							player.getMainHandItem().shrink(1);
						} else if(!ItemStack.matches(player.getOffhandItem(), ItemStack.EMPTY) && player.getOffhandItem().getItem() == this) {
							player.getOffhandItem().shrink(1);
						}
						
					}

				} else {// If you don't have the form unlock it
					playerData.setDriveFormLevel(formName, 1);
					playerData.setNewKeychain(new ResourceLocation(formName), ItemStack.EMPTY);
					player.displayClientMessage(Component.translatable("message.form_unlocked", Utils.translateToLocal(form.getTranslationKey())), true);
					if(!ItemStack.matches(player.getMainHandItem(), ItemStack.EMPTY) && player.getMainHandItem().getItem() == this) {
						player.getMainHandItem().shrink(1);
					} else if(!ItemStack.matches(player.getOffhandItem(), ItemStack.EMPTY) && player.getOffhandItem().getItem() == this) {
						player.getOffhandItem().shrink(1);
					}
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.getPlayer(player)), (ServerPlayer) player);
			}
		}
		return InteractionResultHolder.success(player.getItemInHand(hand));
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		DriveForm form = ModDriveForms.registry.get().getValue(new ResourceLocation(formName));
		if (form != null) {
			tooltip.add(Component.translatable(Utils.translateToLocal("gui.driveformorb.tooltip",Utils.translateToLocal(form.getTranslationKey()))));
		}
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}
}
