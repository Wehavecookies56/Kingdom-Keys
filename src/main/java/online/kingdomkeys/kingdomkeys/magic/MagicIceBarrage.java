package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.magic.IceBarrageControllerEntity;

public class MagicIceBarrage extends Magic {
	public MagicIceBarrage(ResourceLocation registryName, int maxLevel, String gmAbility) {
		super(registryName, false, maxLevel, gmAbility);
	}

	@Override
    public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		float dmgMult = getRealDamageMult(level, caster);
		dmgMult *= fullMPBlastMult;
		lockOnEntity = getMagicLockOn(level) ? lockOnEntity : null;

		IceBarrageControllerEntity iceBarrage = new IceBarrageControllerEntity(player.level(), player, dmgMult, lockOnEntity);
		player.level().addFreshEntity(iceBarrage);

		player.swing(InteractionHand.MAIN_HAND);
	}
	
	@Override
	protected void playMagicCastSound(LivingEntity player, Player caster, int level) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.iceBarrage.get(), SoundSource.PLAYERS, 1F, 1F);
	}

}
