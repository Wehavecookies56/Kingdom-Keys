package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSummonKeyblade;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;
import java.util.List;

public class ProofOfHeartItem extends Item {
    public ProofOfHeartItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
    	IPlayerCapabilities playerData = ModCapabilities.getPlayer(playerIn);
        if (playerData.getAlignment() != Utils.OrgMember.NONE) {
        	if(Utils.isWearingOrgRobes(playerIn)) {
        		playerIn.sendStatusMessage(new TranslationTextComponent("gui.proofofheart.unequip"), true);
        	} else {
				if(Utils.findSummoned(playerIn.inventory, playerData.getEquippedWeapon(), true) > -1)
					PacketHandler.sendToServer(new CSSummonKeyblade(true));
        		playerIn.sendStatusMessage(new TranslationTextComponent("gui.proofofheart.leftorg"), true);
        		
        		if(playerIn.getHeldItemMainhand() != null && playerIn.getHeldItemMainhand().getItem() == this) {
        			playerIn.getHeldItemMainhand().shrink(1);
    	            playerData.setAlignment(Utils.OrgMember.NONE);
    	            return super.onItemRightClick(worldIn, playerIn, handIn);
        		}
        		
        		if(playerIn.getHeldItemOffhand() != null && playerIn.getHeldItemOffhand().getItem() == this) {
        			playerIn.getHeldItemOffhand().shrink(1);
    	            playerData.setAlignment(Utils.OrgMember.NONE);
    	            return super.onItemRightClick(worldIn, playerIn, handIn);
        		}
        		
        	}
        } else {
    		playerIn.sendStatusMessage(new TranslationTextComponent("gui.proofofheart.notinorg"), true);
        }    	
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("gui.proofofheart.desc"));
       	tooltip.add(new TranslationTextComponent("gui.proofofheart.desc2"));
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
