package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzazaEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MagicBlizzard extends Magic {

	public MagicBlizzard(ResourceLocation registryName, int maxLevel, String gmAbility) {
		super(registryName, false, maxLevel, gmAbility);
	}

	@Override
	public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getDamageMult(level) + ModData.getPlayer(player).getNumberOfAbilitiesEquipped(Strings.blizzardBoost) * 0.2F;
		dmgMult *= fullMPBlastMult;

		switch (level) {
		case 0:
			ThrowableProjectile blizzard = new BlizzardEntity(player.level(), player, dmgMult);
			player.level().addFreshEntity(blizzard);
			blizzard.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			break;
		case 1:// -ra and -ga are dmg boosted here
			for (int i = -1; i < 2; i++) {
				ThrowableProjectile blizzara = new BlizzardEntity(player.level(), player, dmgMult * 0.9F);
				player.level().addFreshEntity(blizzara);
				blizzara.shootFromRotation(player, player.getXRot(), player.getYRot() + i * 6, 0, 2F, 0);
			}
			break;
		case 2:
			for (int i = -1; i < 2; i++) {
				ThrowableProjectile blizzara = new BlizzardEntity(player.level(), player, dmgMult * 0.85F);
				player.level().addFreshEntity(blizzara);
				blizzara.shootFromRotation(player, player.getXRot(), player.getYRot() + i * 6, 0, 2F, 0);
			}
			for (int i = -1; i < 1; i++) {
				ThrowableProjectile blizzara = new BlizzardEntity(player.level(), player, dmgMult * 0.85F);
				player.level().addFreshEntity(blizzara);
				blizzara.shootFromRotation(player, player.getXRot() - 6, player.getYRot() + i * 6 + 3, 0, 2F, 0);
			}

			break;
		case 3:
			BlizzazaEntity blizzaza = new BlizzazaEntity(player.level(), player, dmgMult);
			player.level().addFreshEntity(blizzaza);
			blizzaza.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			break;
		}

	}

	@Override
	protected void playMagicCastSound(Player player, Player caster, int level) {
		switch (level) {
			case 0 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.blizzard.get(), SoundSource.PLAYERS, 1F, 1F);
			case 1 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.blizzara.get(), SoundSource.PLAYERS, 1F, 1F);
			case 2 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.blizzaga.get(), SoundSource.PLAYERS, 1F, 1F);
			case 3 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.blizzaga.get(), SoundSource.PLAYERS, 1F, 0.75F);
		}
	}

}
