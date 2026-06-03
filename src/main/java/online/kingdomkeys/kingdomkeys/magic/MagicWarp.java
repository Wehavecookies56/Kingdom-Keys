package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.magic.WarpEntity;

public class MagicWarp extends Magic {
	public MagicWarp(ResourceLocation registryName, boolean hasToSelect, int maxLevel, String gmAbility) {
		super(registryName, hasToSelect, maxLevel, gmAbility);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnTarget) {
		float warpChance = getRealDamageMult(level,caster);

		ThrowableProjectile warp = new WarpEntity(player.level(), player, warpChance);
		warp.setOwner(caster);
		player.level().addFreshEntity(warp);
		warp.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 0.75F, 0);
	}

	@Override
	protected void playMagicCastSound(LivingEntity player, Player caster, int level) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.playerCast.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
