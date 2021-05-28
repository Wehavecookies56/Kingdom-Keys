package online.kingdomkeys.kingdomkeys.item.organization;

import java.util.Map.Entry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class EtherealBladeItem extends OrgWeaponItem implements IOrgWeapon {

	@Override
	public Utils.OrgMember getMember() {
		return Utils.OrgMember.XEMNAS;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		//if (!worldIn.isRemote) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(playerIn);

			for (Entry<Integer, String> entry : playerData.getShortcutsMap().entrySet()) {
				String data = entry.getValue();
				System.out.println(entry.getKey()+1+" "+data);
			}
		//}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
}
