package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSUseShortcutPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class EtherealBladeItem extends OrgWeaponItem implements IOrgWeapon {

	@Override
	public Utils.OrgMember getMember() {
		return Utils.OrgMember.XEMNAS;
	}
	
}
