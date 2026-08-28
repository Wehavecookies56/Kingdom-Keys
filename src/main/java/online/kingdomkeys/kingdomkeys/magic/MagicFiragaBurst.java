package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.magic.FiragaBurstControllerEntity;

public class MagicFiragaBurst extends Magic {

	public MagicFiragaBurst(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public boolean isProjectile() {
		return true;
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getRealDamageMult(caster) + PlayerData.get(caster).getNumberOfAbilitiesEquipped(ModAbilities.FIRE_BOOST) * 0.2F;
		dmgMult *= fullMPBlastMult;
		lockOnEntity = getMagicLockOn() ? lockOnEntity : null;

		FiragaBurstControllerEntity firagaBurst = new FiragaBurstControllerEntity(player.level(), player, dmgMult, lockOnEntity);
		firagaBurst.setMagic(this);
		player.level().addFreshEntity(firagaBurst);
		firagaBurst.shootFromRotation(player, -90, player.getYRot(), 0, 0.4F, 0);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.firagaBurst.get(), SoundSource.PLAYERS, 1F, 0.8F);
	}
}
