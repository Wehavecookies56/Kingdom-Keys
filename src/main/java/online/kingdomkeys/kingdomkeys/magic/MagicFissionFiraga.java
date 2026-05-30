package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.magic.DarkFiragaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FissionFiragaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.TripleFiragaControllerEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MagicFissionFiraga extends Magic {

	public MagicFissionFiraga(ResourceLocation registryName, int maxLevel, String gmAbility) {
		super(registryName, false, maxLevel, gmAbility);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getDamageMult(level) + PlayerData.get(caster).getNumberOfAbilitiesEquipped(Strings.fireBoost) * 0.2F;
		dmgMult *= fullMPBlastMult;
		lockOnEntity = getMagicLockOn(level) ? lockOnEntity : null;

		FissionFiragaEntity fissionFiraga = new FissionFiragaEntity(player.level(), player, dmgMult, lockOnEntity);
		player.level().addFreshEntity(fissionFiraga);
		fissionFiraga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 0);
	}
	
	@Override
	protected void playMagicCastSound(LivingEntity player, Player caster, int level) {

	}
}
