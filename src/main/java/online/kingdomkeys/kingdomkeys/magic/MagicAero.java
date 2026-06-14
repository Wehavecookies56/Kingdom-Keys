package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCAeroSoundPacket;

public class MagicAero extends Magic {

	public MagicAero(ResourceLocation registryName, int tier, String gmAbility) {
		super(registryName, true, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		int time = (int) (PlayerData.get(caster).getMaxMP() * (4F + getRealDamageMult(caster) / 2F));
		player.addEffect(new MobEffectInstance(ModMobEffects.AERO, time, getTier(), false, false, false));
		PacketHandler.sendToAll(new SCAeroSoundPacket(player.getId()));
		caster.swing(InteractionHand.MAIN_HAND);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.aero1.get(), SoundSource.PLAYERS, 1F, 1F);
	}

}