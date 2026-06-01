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

public class MagicDeepFreeze extends Magic {

	public MagicDeepFreeze(ResourceLocation registryName, int maxLevel, String gmAbility) {
		super(registryName, false, maxLevel, gmAbility);
	}

	@Override
    public void magicUse(LivingEntity player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		PlayerData playerData = PlayerData.get(caster);
		int time = (int) (playerData.getMagic(true) * getRealDamageMult(level, caster));
		int localLevel = Utils.getMagicHighestLocalLevel(playerData.getEquippedMagics(), getRegistryName().toString(), level);
		float radius = 2 + (localLevel * 0.5F); //TODO change back to 2 and scale with magic level

		for (int a = 0; a < 360; a += 5) {
			double angle = Math.toRadians(a);
			double dirX = Math.sin(angle);
			double dirZ = Math.cos(angle);

			double x = player.getX() + radius * dirX;
			double z = player.getZ() + radius * dirZ;

			((ServerLevel) player.level()).sendParticles(ParticleTypes.SNOWFLAKE, x, player.getY() + 1, z, 0, dirX * 0.15, 0, dirZ * 0.15, 1);
		}

		List<LivingEntity> list = new ArrayList<>();

		if(caster instanceof Player p) {
			list = Utils.getLivingEntitiesInRadiusExcludingParty(p, radius);
		}
		list.remove(this);

		for(LivingEntity e : list) {
			e.addEffect(new MobEffectInstance(ModMobEffects.FREEZE, time, level, false, false, false));
		}

		player.swing(InteractionHand.MAIN_HAND);
	}
	
	@Override
	protected void playMagicCastSound(LivingEntity player, Player caster, int level) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.deepFreeze.get(), SoundSource.PLAYERS, 1F, 1F);
	}

}
