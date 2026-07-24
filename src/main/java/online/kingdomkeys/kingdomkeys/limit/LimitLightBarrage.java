package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.organization.LightBarrageCoreEntity;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public class LimitLightBarrage extends Limit {

	private static final int LEVITATION_TICKS = 60; // 3s

	public LimitLightBarrage(ResourceLocation registryName, int order, OrgMember owner) {
		super(registryName, order, owner);
	}

	@Override
	public void onUse(Player player, LivingEntity target) {
		super.onUse(player, target);
		PlayerData playerData = PlayerData.get(player);

		ItemStack stack = player.getMainHandItem();
		float damage;
		if (stack.getItem() instanceof IOrgWeapon) {
			damage = DamageCalculation.getOrgStrengthDamage(player, stack) * 0.25F;
		} else {
			damage = playerData.getStrength(true) * 0.25F;
		}
		damage *= getLimitData().getDmgMult();

		player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, LEVITATION_TICKS, 5, false, true, true));
		player.level().playSound(null, player.blockPosition(), ModSounds.lightBeam.get(), SoundSource.PLAYERS, 0.8F, 0.6F);

		LightBarrageCoreEntity barrage = new LightBarrageCoreEntity(player.level(), player, damage);
		player.level().addFreshEntity(barrage);
	}
}
