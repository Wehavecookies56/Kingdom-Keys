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

public class MagicSpellItem extends Item {
	String magic;

	public MagicSpellItem(Properties properties, String name) {
		super(properties);
		this.magic = name;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {		
		if (!world.isRemote) {
			IPlayerCapabilities props = ModCapabilities.get(player);
			System.out.println(props.getMagicsList());
			if (props != null && props.getMagicsList() != null) {
				if (!props.getMagicsList().contains(magic)) {
					props.addMagicToList(magic);
					if(player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == this) {
						player.getHeldItemMainhand().shrink(1);
					} else if(player.getHeldItemOffhand() != null && player.getHeldItemOffhand().getItem() == this) {
						player.getHeldItemOffhand().shrink(1);
					}
					player.sendMessage(new TranslationTextComponent("Unlocked " + magic.substring(magic.indexOf(":") + 1)));
				} else {
					player.sendMessage(new TranslationTextComponent(magic.substring(magic.indexOf(":") + 1)+ " Already unlocked"));
				}
				PacketHandler.sendTo(new SCSyncCapabilityPacket(ModCapabilities.get(player)), (ServerPlayerEntity) player);
			}
		}
		return ActionResult.resultSuccess(player.getHeldItem(hand));
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TranslationTextComponent("Unlock " + magic));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
}
