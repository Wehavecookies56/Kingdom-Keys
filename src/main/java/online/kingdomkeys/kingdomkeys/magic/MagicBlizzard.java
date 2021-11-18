package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzazaEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MagicBlizzard extends Magic {

	public MagicBlizzard(String registryName, int maxLevel, boolean hasRC, int order) {
		super(registryName, false, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(PlayerEntity player, PlayerEntity caster, int level, float fullMPBlastMult) {
		float dmg = ModCapabilities.getPlayer(player).isAbilityEquipped(Strings.blizzardBoost) ? getDamageMult(level) * 1.2F : getDamageMult(level);
		dmg *= fullMPBlastMult;
		
		switch(level) {
		case 0:
			ThrowableEntity blizzard = new BlizzardEntity(player.world, player, dmg);
			player.world.addEntity(blizzard);
			blizzard.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 2F, 0);
			player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1F, 1F);
			break;
		case 1://-ra and -ga are dmg boosted here
			for(int i = -1; i < 2; i++) {
				ThrowableEntity blizzara = new BlizzardEntity(player.world, player, dmg* 1.3F);
				player.world.addEntity(blizzara);
				blizzara.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw + i*6, 0, 2F, 0);
				player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1F, 1F);
			}
			break;
		case 2:
			for(int i = -1; i < 2; i++) {
				ThrowableEntity blizzara = new BlizzardEntity(player.world, player, dmg*1.6F);
				player.world.addEntity(blizzara);
				blizzara.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw + i*6, 0, 2F, 0);
				player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1F, 1F);
			}
			for(int i = -1; i < 1; i++) {
				ThrowableEntity blizzara = new BlizzardEntity(player.world, player, dmg* 1.6F);
				player.world.addEntity(blizzara);
				blizzara.setDirectionAndMovement(player, player.rotationPitch-6, player.rotationYaw + i*6+3, 0, 2F, 0);
				player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1F, 1F);
			}
			break;
		case 3:
			BlizzazaEntity blizzaza = new BlizzazaEntity(player.world, player, dmg);
			player.world.addEntity(blizzaza);
			blizzaza.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 2F, 0);
			player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 1F, 1F);
			break;
		}
		
		
	}

}
