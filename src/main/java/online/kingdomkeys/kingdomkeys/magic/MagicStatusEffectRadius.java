package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class MagicStatusEffectRadius extends Magic {

	Holder<MobEffect> effectType;
	SoundEvent sound;
	ParticleOptions particle;

	public MagicStatusEffectRadius(ResourceLocation registryName, int maxLevel, String gmAbility, Holder<MobEffect> effectType, SoundEvent sound, ParticleOptions particle) {
		super(registryName, true, maxLevel, gmAbility);
		this.effectType = effectType;
		this.sound = sound;
		this.particle = particle;
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		int time = (int) (PlayerData.get(caster).getMaxMP() * (4F + getDamageMult(level)/2F));
		float radius = 2 + (Utils.getMagicHighestLocalLevel(PlayerData.get(caster).getEquippedMagics(), getRegistryName().toString(), level) * 0.2F);
		for(int a = 0; a < 360; a+=5) {
			double x = player.getX() + radius * Math.sin(Math.toRadians(a));
			double z = player.getZ() + radius * Math.cos(Math.toRadians(a));
			((ServerLevel)player.level()).sendParticles(particle, x,player.getY() + 1, z,0,0,1F,0,0);
		}

		List<LivingEntity> list = new ArrayList<>();

		if(caster instanceof Player p) {
			list = Utils.getLivingEntitiesInRadiusExcludingParty(p, radius);
		}
		list.remove(this);

		for(LivingEntity e : list) {
			e.addEffect(new MobEffectInstance(effectType, time, level, false, false, false));
		}

		caster.swing(InteractionHand.MAIN_HAND);
	}

	@Override
	protected void playMagicCastSound(LivingEntity player, Player caster, int level) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), sound, SoundSource.PLAYERS, 1F, 1F);
	}

}