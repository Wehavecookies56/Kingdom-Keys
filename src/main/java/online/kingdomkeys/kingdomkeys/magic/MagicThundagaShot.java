package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.magic.CrawlingFiragaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThundagaShotEntity;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class MagicThundagaShot extends Magic {

	public MagicThundagaShot(ResourceLocation registryName, int maxLevel, String gmAbility) {
		super(registryName, false, maxLevel, gmAbility);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getDamageMult(level) + PlayerData.get(caster).getNumberOfAbilitiesEquipped(Strings.fireBoost) * 0.2F;
		dmgMult *= fullMPBlastMult;
		lockOnEntity = getMagicLockOn(level) ? lockOnEntity : null;

		ThundagaShotEntity thundagaShot = new ThundagaShotEntity(player.level(), player, dmgMult, lockOnEntity);
		player.level().addFreshEntity(thundagaShot);
		thundagaShot.setPos(player.getX(), player.getY()+1, player.getZ());
		thundagaShot.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.3F, 0);
	}
	
	@Override
	protected void playMagicCastSound(LivingEntity player, Player caster, int level) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.thundagaShot.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
