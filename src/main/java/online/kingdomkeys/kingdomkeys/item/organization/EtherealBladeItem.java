package online.kingdomkeys.kingdomkeys.item.organization;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.organization.ArrowgunShotEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class EtherealBladeItem extends OrgSwordItem implements IOrgWeapon {

	@Override
	public Utils.OrgMember getMember() {
		return Utils.OrgMember.XEMNAS;
	}	
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		if(playerData != null && !playerData.getRecharge()) {
			int cost = 6;
    		cost -= cost * playerData.getNumberOfAbilitiesEquipped(Strings.mpThrift) * 0.2;
			playerData.remMP(Math.max(1, cost));

			ArrowgunShotEntity shot = new ArrowgunShotEntity(player.level(), player, DamageCalculation.getMagicDamage(player) * 0.08F);
			shot.setShotType(0);
			shot.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 3F, 0);
			world.addFreshEntity(shot);
			player.level().playSound(null, player.blockPosition(), ModSounds.laser.get(), SoundSource.PLAYERS, 1F, 1F);
		}
		return super.use(world, player, hand);
	}
	
}
