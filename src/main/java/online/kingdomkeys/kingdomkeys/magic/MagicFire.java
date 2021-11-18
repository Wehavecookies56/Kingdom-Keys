package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.magic.FiraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FiragaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FirazaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MagicFire extends Magic {

	public MagicFire(String registryName, int maxLevel, boolean hasRC, int order) {
		super(registryName, false, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(PlayerEntity player, PlayerEntity caster, int level, float fullMPBlastMult) {
		float dmg = ModCapabilities.getPlayer(player).isAbilityEquipped(Strings.fireBoost) ? getDamageMult(level) * 1.2F : getDamageMult(level);
		dmg *= fullMPBlastMult;

		switch (level) {
		case 0:
			ThrowableEntity fire = new FireEntity(player.world, player, dmg);
			player.world.addEntity(fire);
			fire.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 2F, 0);
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1F, 1F);
			break;
		case 1:
			ThrowableEntity fira = new FiraEntity(player.world, player, dmg);
			player.world.addEntity(fira);
			fira.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 2F, 0);
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1F, 1F);
			break;
		case 2:
			ThrowableEntity firaga = new FiragaEntity(player.world, player, dmg);
			player.world.addEntity(firaga);
			firaga.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 2F, 0);
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1F, 1F);
			break;
		case 3:
			ThrowableEntity firaza = new FirazaEntity(player.world, player, dmg);
			player.world.addEntity(firaza);
			firaza.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 2F, 0);
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1F, 0.5F);
			break;
		}
		
	}
}
