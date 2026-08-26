package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.organization.LanceStormCoreEntity;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public class LimitLanceStorm extends Limit {

	public LimitLanceStorm(ResourceLocation registryName, int order, OrgMember owner) {
		super(registryName, order, owner);
	}

	@Override
	public void onUse(Player player, LivingEntity target) {
		super.onUse(player, target);
		PlayerData playerData = PlayerData.get(player);

		ItemStack stack = player.getMainHandItem();
		float damage;
		if (stack.getItem() instanceof IOrgWeapon) {
			damage = DamageCalculation.getOrgStrengthDamage(player, stack);
		} else {
			damage = playerData.getStrength(true);
		}
		damage *= getLimitData().getDmgMult();

		LanceStormCoreEntity storm = new LanceStormCoreEntity(player.level(), player, damage);
		player.level().addFreshEntity(storm);
	}
}
