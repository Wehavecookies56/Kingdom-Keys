package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.magic.TripleFiragaControllerEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MagicTripleFiraga extends Magic {

	public MagicTripleFiraga(ResourceLocation registryName, int tier, String gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getRealDamageMult(caster) + PlayerData.get(caster).getNumberOfAbilitiesEquipped(Strings.fireBoost) * 0.2F;
		dmgMult *= fullMPBlastMult;
		lockOnEntity = getMagicLockOn() ? lockOnEntity : null;

		ThrowableProjectile tripleFiragaController = new TripleFiragaControllerEntity(player.level(), player, dmgMult, lockOnEntity);
		player.level().addFreshEntity(tripleFiragaController);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {

	}
}
