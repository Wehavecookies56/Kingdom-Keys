package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzazaEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;

public class MagicBlizzard extends Magic {

	public MagicBlizzard(String registryName, int maxLevel, String gmAbility, int order) {
		super(registryName, false, maxLevel, gmAbility, order);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult) {
		float dmgMult = getDamageMult(level) + ModCapabilities.getPlayer(player).getNumberOfAbilitiesEquipped(Strings.blizzardBoost) * 0.2F;
		dmgMult *= fullMPBlastMult;
		
		switch(level) {
		case 0:
			ThrowableProjectile blizzard = new BlizzardEntity(player.level, player, dmgMult);
			player.level.addFreshEntity(blizzard);
			blizzard.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			player.level.playSound(null, player.blockPosition(), ModSounds.blizzard.get(), SoundSource.PLAYERS, 1F, 1F);
			break;
		case 1://-ra and -ga are dmg boosted here
			for(int i = -1; i < 2; i++) {
				ThrowableProjectile blizzara = new BlizzardEntity(player.level, player, dmgMult* 0.9F);
				player.level.addFreshEntity(blizzara);
				blizzara.shootFromRotation(player, player.getXRot(), player.getYRot() + i*6, 0, 2F, 0);
				player.level.playSound(null, player.blockPosition(), ModSounds.blizzard.get(), SoundSource.PLAYERS, 1F, 1F);
			}
			break;
		case 2:
			for(int i = -1; i < 2; i++) {
				ThrowableProjectile blizzara = new BlizzardEntity(player.level, player, dmgMult*0.85F);
				player.level.addFreshEntity(blizzara);
				blizzara.shootFromRotation(player, player.getXRot(), player.getYRot() + i*6, 0, 2F, 0);
				player.level.playSound(null, player.blockPosition(), ModSounds.blizzard.get(), SoundSource.PLAYERS, 1F, 1F);
			}
			for(int i = -1; i < 1; i++) {
				ThrowableProjectile blizzara = new BlizzardEntity(player.level, player, dmgMult*0.85F);
				player.level.addFreshEntity(blizzara);
				blizzara.shootFromRotation(player, player.getXRot()-6, player.getYRot() + i*6+3, 0, 2F, 0);
				player.level.playSound(null, player.blockPosition(), ModSounds.blizzard.get(), SoundSource.PLAYERS, 1F, 1F);
			}
			break;
		case 3:
			BlizzazaEntity blizzaza = new BlizzazaEntity(player.level, player, dmgMult);
			player.level.addFreshEntity(blizzaza);
			blizzaza.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2F, 0);
			player.level.playSound(null, player.blockPosition(), ModSounds.blizzard.get(), SoundSource.PLAYERS, 1F, 1F);
			break;
		}
		
		
	}

}
