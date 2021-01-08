package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.entity.organization.ChakramEntity;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ChakramItem extends OrgWeaponItem implements IOrgWeapon {
   
    @Override
    public Utils.OrgMember getMember() {
        return Utils.OrgMember.AXEL;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
    	ChakramEntity entity = new ChakramEntity(worldIn, player, this.getRegistryName().getPath());
		player.world.addEntity(entity);
		entity.shoot(player.rotationPitch, player.rotationYaw, 0, 2.5F, 0);
		player.swingArm(Hand.MAIN_HAND);
    	return super.onItemRightClick(worldIn, player, handIn);
    }

}
