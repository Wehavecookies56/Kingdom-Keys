package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.magic.*;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MagicFire extends Magic {

	public MagicFire(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public boolean isProjectile() {
		return true;
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getRealDamageMult(caster) + PlayerData.get(caster).getNumberOfAbilitiesEquipped(ModAbilities.FIRAZA) * 0.2F;
		dmgMult *= fullMPBlastMult;
		lockOnEntity = getMagicLockOn() ? lockOnEntity : null;

		//If it's dark firaga cast it directly
		if (getRegistryName().toString().equals(Strings.Magic_DarkFiraga)) {
			DarkFiragaEntity darkFiraga = new DarkFiragaEntity(player.level(), player, dmgMult, lockOnEntity);
			darkFiraga.setMagic(this);
			player.level().addFreshEntity(darkFiraga);
			darkFiraga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			return;
		}

		switch (getTier()) {
			case 0:
				BaseMagicProjectile fire = new FireEntity(player.level(), player, dmgMult, lockOnEntity);
				fire.setMagic(this);
				player.level().addFreshEntity(fire);
				fire.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
				break;
			case 1:
				BaseMagicProjectile fira = new FiraEntity(player.level(), player, dmgMult, lockOnEntity);
				fira.setMagic(this);
				player.level().addFreshEntity(fira);
				fira.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
				break;
			case 2:
				BaseMagicProjectile firaga = new FiragaEntity(player.level(), player, dmgMult, lockOnEntity);
				firaga.setMagic(this);
				player.level().addFreshEntity(firaga);
				firaga.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
				break;
			case 3:
				BaseMagicProjectile firaza = new FirazaEntity(player.level(), player, dmgMult, lockOnEntity);
				firaza.setMagic(this);
				player.level().addFreshEntity(firaza);
				firaza.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
				break;
		}

	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		switch (getTier()) {
			case 0 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.fire.get(), SoundSource.PLAYERS, 1F, 1F);
			case 1 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.fira.get(), SoundSource.PLAYERS, 1F, 1F);
			case 2 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.firaga.get(), SoundSource.PLAYERS, 1F, 1F);
			case 3 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.firaga.get(), SoundSource.PLAYERS, 1F, 0.7F);
		}
	}
}
