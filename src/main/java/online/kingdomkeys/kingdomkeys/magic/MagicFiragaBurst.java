package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.magic.FiragaBurstControllerEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.TripleFiragaControllerEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MagicFiragaBurst extends Magic {

	public MagicFiragaBurst(ResourceLocation registryName, int maxLevel, String gmAbility) {
		super(registryName, false, maxLevel, gmAbility);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getDamageMult(level) + PlayerData.get(caster).getNumberOfAbilitiesEquipped(Strings.fireBoost) * 0.2F;
		dmgMult *= fullMPBlastMult;
		lockOnEntity = getMagicLockOn(level) ? lockOnEntity : null;

		FiragaBurstControllerEntity firagaBurst = new FiragaBurstControllerEntity(player.level(), player, dmgMult, lockOnEntity);
		player.level().addFreshEntity(firagaBurst);
		firagaBurst.shootFromRotation(player, -90, player.getYRot(), 0, 0.4F, 0);
	}
	
	@Override
	protected void playMagicCastSound(LivingEntity player, Player caster, int level) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.firagaBurst.get(), SoundSource.PLAYERS, 1F, 0.8F);
	}
}
