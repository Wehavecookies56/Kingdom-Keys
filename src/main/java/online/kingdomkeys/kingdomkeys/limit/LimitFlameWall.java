package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.organization.FlameWallCoreEntity;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

/**
 * Axel's Limit - "Ring of Fire: Cage": traps everything around the caster
 * (except party members) inside a fixed cylinder of flame for a few seconds.
 * Anything that tries to reach the edge gets shoved back in and takes a
 * little fire damage each pulse. See FlameBarrierCoreEntity for the logic.
 */
public class LimitFlameWall extends Limit {

	public LimitFlameWall(ResourceLocation registryName, int order, OrgMember owner) {
		super(registryName, order, owner);
	}

	@Override
	public void onUse(Player player, LivingEntity target) {
		ItemStack stack = player.getMainHandItem();
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.fire.get(), SoundSource.PLAYERS, 1F, 0.7F);

		super.onUse(player, target);
		PlayerData playerData = PlayerData.get(player);

		float damage;
		if (stack != null && stack.getItem() instanceof IOrgWeapon) {
			damage = (DamageCalculation.getOrgStrengthDamage(player, stack) + DamageCalculation.getOrgMagicDamage(player, (IOrgWeapon) stack.getItem())) * 0.15F;
		} else {
			damage = (playerData.getStrength(true) + playerData.getMagic(true)) * 0.15F;
		}

		damage *= getLimitData().getDmgMult();

		FlameWallCoreEntity barrier = new FlameWallCoreEntity(player.level(), player, damage);
		barrier.setPos(player.getX(), player.getY(), player.getZ());
		player.level().addFreshEntity(barrier);
	}
}
