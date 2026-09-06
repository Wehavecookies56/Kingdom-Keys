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
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class MagicDeepFreeze extends Magic {

	boolean launch;

	public MagicDeepFreeze(ResourceLocation registryName, int tier, ResourceLocation gmAbility, boolean launch) {
		super(registryName, false, gmAbility);
		setTier(tier);
		this.launch = launch;
	}

	@Override
	public void magicUse(LivingEntity player, LivingEntity caster, float fullMPBlastMult, LivingEntity lockOnEntity) {

		float dmg = getRealDamageMult(caster);
		dmg *= fullMPBlastMult;

		int time = (int) casterMagicStat(caster) * 2;
		float radius = 3 + (getMagicLocalLevel(caster) * 0.2F);

		for (int a = 0; a < 360; a += 5) {
			double angle = Math.toRadians(a);
			double dirX = Math.sin(angle);
			double dirZ = Math.cos(angle);

			double x = player.getX() + radius * dirX;
			double z = player.getZ() + radius * dirZ;

			((ServerLevel) player.level()).sendParticles(ParticleTypes.SNOWFLAKE, x, player.getY() + 1, z, 0, dirX * 0.15, 0, dirZ * 0.15, 1);
		}

		List<LivingEntity> list = Utils.getLivingEntitiesInRadiusExcludingParty(caster, radius);
		list.remove(this);

		for (LivingEntity e : list) {
			if (launch) {
				e.addEffect(new MobEffectInstance(ModMobEffects.ZERO_GRAVITY, 5, 2, false, false, false));
				e.setOnGround(false);
			}
			e.addEffect(new MobEffectInstance(ModMobEffects.FREEZE, time, 50, false, false, false));
			e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.ICE, player, player), dmg);
		}

		player.swing(InteractionHand.MAIN_HAND);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, LivingEntity caster) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.deepFreeze.get(), SoundSource.PLAYERS, 1F, 1F);
	}

}
