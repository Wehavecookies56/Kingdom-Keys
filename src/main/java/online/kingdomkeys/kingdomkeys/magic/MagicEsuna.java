package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

import java.util.ArrayList;
import java.util.List;

public class MagicEsuna extends Magic {

	public MagicEsuna(ResourceLocation registryName, boolean hasToSelect, int tier, ResourceLocation gmAbility) {
		super(registryName, hasToSelect, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnTarget) {
		//caster.swing(InteractionHand.MAIN_HAND);
		((ServerLevel) player.level()).sendParticles(ParticleTypes.SONIC_BOOM.getType(), player.getX(), player.getY() + 2.3D, player.getZ(), 5, 0D, 0D, 0D, 0D);

		List<MobEffectInstance> effectsList = new ArrayList<>();
		for (MobEffectInstance e : player.getActiveEffects()) {
			if (e.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
				effectsList.add(e);
			}
		}

		for (MobEffectInstance badEffect : effectsList) {
			player.removeEffect(badEffect.getEffect());
		}

		/*if (level == 1){ // Group Esuna
			if (worldData.getPartyFromMember(player.getUUID()) != null) {
				Party party = worldData.getPartyFromMember(player.getUUID());
				List<Party.Member> list = party.getMembers();
				if (!list.isEmpty()) {
					for (Party.Member member : list) {
						if (player.level().getPlayerByUUID(member.getUUID()) != null && player.distanceTo(player.level().getPlayerByUUID(member.getUUID())) < ModConfigs.SERVER.partyRangeLimit.get()) {
							LivingEntity e = player.level().getPlayerByUUID(member.getUUID());
							if (e != null && Utils.isEntityInParty(party, e) && e != player) {
								for (MobEffectInstance eG : e.getActiveEffects()) {
									if (eG.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
										effectsList.add(eG);
									}
								}

								for (MobEffectInstance badEffect : effectsList) {
									//TODO take a look at EffectCure and removeEffectsCuredBy
									e.removeEffect(badEffect.getEffect());
								}
							}
						}
					}
				}
			}
		}*/


	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.esuna.get(), SoundSource.PLAYERS, 1F, 1F);
	}
}
