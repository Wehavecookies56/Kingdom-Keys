package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Rotations;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.entity.organization.ChakramEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ChakramItem extends OrgWeaponItem implements IOrgWeapon {
   
    @Override
    public Utils.OrgMember getMember() {
        return Utils.OrgMember.AXEL;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
    	ChakramEntity entity = new ChakramEntity(worldIn, player, this.getRegistryName().getPath());
    	System.out.println(this.getRegistryName().getPath());
    	switch(this.getRegistryName().getPath()) {
    	case Strings.eternalFlames:
    	case Strings.prometheus:
    	case Strings.volcanics:
    		entity.setRotationPoint(0);
    		break;
    	default:
    		entity.setRotationPoint(2);	
    	}
    	
		player.world.addEntity(entity);
		entity.shoot(player, player.rotationPitch, player.rotationYaw, 0, 2.5F, 0);
		player.swingArm(handIn);
    	return super.onItemRightClick(worldIn, player, handIn);
    }

}
