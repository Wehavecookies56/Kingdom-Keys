package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class MagicZeroGravity extends Magic {

	public MagicZeroGravity(ResourceLocation registryName, int maxLevel, String gmAbility) {
		super(registryName, false, maxLevel, gmAbility);
	}

	@Override
    public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		int time = (int) (PlayerData.get(caster).getMagic(true) * getRealDamageMult(level,caster));
		float radius = level + 1 + (getMagicLocalLevel(caster, level) * 0.2F);

		for(int a = 0; a < 360; a+=5) {
			double x = player.getX() + radius * Math.sin(Math.toRadians(a));
			double z = player.getZ() + radius * Math.cos(Math.toRadians(a));
			((ServerLevel)player.level()).sendParticles(ParticleTypes.DRAGON_BREATH, x,player.getY(), z,0,0,1F,0,0.25);
			((ServerLevel)player.level()).sendParticles(ParticleTypes.DRAGON_BREATH, x, player.getY() - radius, z,0,0,1F,0,0.25);
			((ServerLevel)player.level()).sendParticles(ParticleTypes.DRAGON_BREATH, x,player.getY() - radius * 2, z,0,0,1F,0,0.25);
		}

		List<LivingEntity> list = new ArrayList<>();

		if(caster instanceof Player p) {
			list = Utils.getLivingEntitiesInRadiusExcludingParty(p, radius);
		}
		list.remove(this);

		for(LivingEntity e : list) {
			e.addEffect(new MobEffectInstance(ModMobEffects.ZERO_GRAVITY, time, 1, false, false, false));
		}

		player.swing(InteractionHand.MAIN_HAND);
	}
	
	@Override
	protected void playMagicCastSound(LivingEntity player, Player caster, int level) {
		switch (level) {
			case 0 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.zeroGravity.get(), SoundSource.PLAYERS, 1F, 1F);
			case 1 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.zeroGravity.get(), SoundSource.PLAYERS, 1F, 1F);
			case 2 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.zeroGravity.get(), SoundSource.PLAYERS, 1F, 1F);
			case 3 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.zeroGravity.get(), SoundSource.PLAYERS, 1F, 1F);
		}
	}

}
