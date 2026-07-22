package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.magic.FissionFiragaEntity;

public class MagicFissionFiraga extends Magic {

	public MagicFissionFiraga(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getRealDamageMult(caster) + PlayerData.get(caster).getNumberOfAbilitiesEquipped(ModAbilities.FIRE_BOOST) * 0.2F;
		dmgMult *= fullMPBlastMult;
		lockOnEntity = getMagicLockOn() ? lockOnEntity : null;

		FissionFiragaEntity fissionFiraga = new FissionFiragaEntity(player.level(), player, dmgMult, lockOnEntity);
		player.level().addFreshEntity(fissionFiraga);
		fissionFiraga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 0);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.firaga.get(), SoundSource.PLAYERS, 1F, 0.9F);
	}
}
