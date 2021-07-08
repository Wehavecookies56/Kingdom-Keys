package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.organization.LaserDomeCoreEntity;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID)
public class LimitLaserDome extends Limit {

	public LimitLaserDome(String registryName, int order, int cost, int cooldown, OrgMember owner) {
		super(registryName, order, cost, cooldown, owner);
	}

	@Override
	public int getCost() {
		return ModConfigs.limitLaserDomeCost;
	}
	
	@Override
	public void onUse(PlayerEntity player, LivingEntity target) {
		ItemStack stack = player.getHeldItemMainhand();
		player.world.playSound(null, player.getPosition(), ModSounds.portal.get(), SoundCategory.PLAYERS, 1F, 1F);
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		float damage;
		if(stack != null && stack.getItem() instanceof IOrgWeapon) {
			damage = (DamageCalculation.getOrgStrengthDamage(player, stack) + DamageCalculation.getOrgMagicDamage(player, (IOrgWeapon) stack.getItem())) / 2 * 0.15F;
		} else {
			damage = (playerData.getStrength(true) + playerData.getMagic(true)) / 2F;
		}

		LaserDomeCoreEntity dome = new LaserDomeCoreEntity(player.world, player, target, damage);
		dome.setPosition(target.getPosX(), target.getPosY(), target.getPosZ());
		player.world.addEntity(dome);
	}
}