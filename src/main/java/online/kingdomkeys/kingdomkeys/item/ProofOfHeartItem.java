package online.kingdomkeys.kingdomkeys.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSummonKeyblade;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ProofOfHeartItem extends Item {
    public ProofOfHeartItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
    	IPlayerCapabilities playerData = ModCapabilities.getPlayer(playerIn);
        if (playerData.getAlignment() != Utils.OrgMember.NONE) {
        	if(Utils.isWearingOrgRobes(playerIn)) {
        		playerIn.displayClientMessage(new TranslatableComponent("gui.proofofheart.unequip"), true);
        	} else {
        		if(worldIn.isClientSide) {
					if(Utils.findSummoned(playerIn.inventory, playerData.getEquippedWeapon(), true) > -1)
						PacketHandler.sendToServer(new CSSummonKeyblade(true, playerData.getAlignment()));
        		}
        		playerIn.displayClientMessage(new TranslatableComponent("gui.proofofheart.leftorg"), true);

        		if(playerIn.getMainHandItem() != null && playerIn.getMainHandItem().getItem() == this) {
        			playerIn.getMainHandItem().shrink(1);
    	            playerData.setAlignment(Utils.OrgMember.NONE);
    	            return super.use(worldIn, playerIn, handIn);
        		}
        		
        		if(playerIn.getOffhandItem() != null && playerIn.getOffhandItem().getItem() == this) {
        			playerIn.getOffhandItem().shrink(1);
    	            playerData.setAlignment(Utils.OrgMember.NONE);
    	            return super.use(worldIn, playerIn, handIn);
        		}
        		
        	}
        } else {
    		playerIn.displayClientMessage(new TranslatableComponent("gui.proofofheart.notinorg"), true);
        }    	
        return super.use(worldIn, playerIn, handIn);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("gui.proofofheart.desc"));
       	tooltip.add(new TranslatableComponent("gui.proofofheart.desc2"));
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
