package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnetEntity;

public class MagicMagnet extends Magic {

	public MagicMagnet(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, false, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmg = getRealDamageMult(caster);
		dmg *= fullMPBlastMult;

		MagnetEntity.Tier[] tiers = MagnetEntity.Tier.values();
		if (getTier() >= 0 && getTier() < tiers.length) {
			MagnetEntity magnet = new MagnetEntity(player.level(), player, dmg, tiers[getTier()]);
			if (lockOnEntity != null) {
				magnet.setPos(lockOnEntity.getX(), lockOnEntity.getY() + 1, lockOnEntity.getZ());
				magnet.shootFromRotation(lockOnEntity, -90, lockOnEntity.getYRot(), 0, 1, 0);
			} else {
				magnet.shootFromRotation(player, -90, player.getYRot(), 0, 1F, 0);
			}
			player.level().addFreshEntity(magnet);
		}

		player.swing(InteractionHand.MAIN_HAND);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, LivingEntity caster) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.magnet1.get(), SoundSource.PLAYERS, 1F, 1F);
	}

}
