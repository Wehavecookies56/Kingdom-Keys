package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.organization.IllusoryMeteorCoreEntity;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public class LimitDarkMeteor extends Limit {

	private static final float CENTER_DISTANCE = 4F;

	public LimitDarkMeteor(ResourceLocation registryName, int order, OrgMember owner) {
		super(registryName, order, owner);
	}

	@Override
	public void onUse(Player player, LivingEntity target) {
		super.onUse(player, target);

		ItemStack stack = player.getMainHandItem();
		float damage;
		if (stack.getItem() instanceof IOrgWeapon) {
			damage = (DamageCalculation.getOrgStrengthDamage(player, stack) + DamageCalculation.getOrgMagicDamage(player, (IOrgWeapon) stack.getItem())) * 0.5F;
		} else {
			damage = (PlayerData.get(player).getStrength(true) + PlayerData.get(player).getMagic(true)) * 0.5F;
		}
		damage *= getLimitData().getDmgMult();

		Vec3 center = target != null ? target.position() : player.position().add(player.getLookAngle().scale(CENTER_DISTANCE));

		player.level().playSound(null, player.blockPosition(), SoundEvents.WARDEN_ROAR, SoundSource.PLAYERS, 0.6F, 1.6F);

		IllusoryMeteorCoreEntity zone = new IllusoryMeteorCoreEntity(player.level(), player, center, damage);
		player.level().addFreshEntity(zone);
	}
}
