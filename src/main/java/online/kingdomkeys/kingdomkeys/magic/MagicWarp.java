package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.magic.WarpEntity;

public class MagicWarp extends Magic {
	public MagicWarp(ResourceLocation registryName, boolean hasToSelect, int tier, ResourceLocation gmAbility) {
		super(registryName, hasToSelect, gmAbility);
		setTier(tier);
	}

	@Override
	public boolean isProjectile() {
		return true;
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnTarget) {
		float warpChance = getRealDamageMult(caster);

		ThrowableProjectile warp = new WarpEntity(player.level(), player, warpChance);
		warp.setOwner(caster);
		player.level().addFreshEntity(warp);
		warp.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 0.75F, 0);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.playerCast.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
