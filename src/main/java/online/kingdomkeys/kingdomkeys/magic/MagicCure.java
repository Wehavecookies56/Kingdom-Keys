package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class MagicCure extends Magic {

	public MagicCure(ResourceLocation registryName, int tier, ResourceLocation gmAbility) {
		super(registryName, true, gmAbility);
		setTier(tier);
	}

	@Override
	public void magicUse(LivingEntity player, Player caster, float fullMPBlastMult, LivingEntity lockOnEntity) {
		((ServerLevel) player.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), player.getX(), player.getY() + 2.3D, player.getZ(), 5, 0D, 0D, 0D, 0D);
		PlayerData playerData = PlayerData.get(caster);
		WorldData worldData = WorldData.get(player.getServer());

		float amount = playerData.getMaxHP() * getRealDamageMult(caster);
		if (playerData.getNumberOfAbilitiesEquipped(ModAbilities.LEAF_BRACER) > 0)
			player.invulnerableTime = 40;

		Utils.reviveFromKO(player);
		switch (getTier()) {
			case 0:
				player.heal(amount);
				break;
			case 1:
				player.heal(amount);

				if (worldData.getPartyFromMember(player.getUUID()) != null) {
					// heal everyone including user
					Party party = worldData.getPartyFromMember(player.getUUID());
					List<LivingEntity> list = Utils.getLivingEntitiesInRadius(player, 3);
					if (!list.isEmpty()) {
						for (LivingEntity e : list) {
							if (Utils.isEntityInParty(party, e) && e != player) {
								e.heal(amount / 2);
								Utils.reviveFromKO(e);
								player.level().playSound(null, e.position().x(), e.position().y(), e.position().z(), ModSounds.cura.get(), SoundSource.PLAYERS, 1F, 1F);
							}
						}
					}
				}
				break;
			case 2:
				player.heal(amount);

				if (worldData.getPartyFromMember(player.getUUID()) != null) {
					Party party = worldData.getPartyFromMember(player.getUUID());
					List<LivingEntity> list = Utils.getLivingEntitiesInRadius(player, 5);
					if (!list.isEmpty()) {
						for (LivingEntity e : list) {
							if (Utils.isEntityInParty(party, e) && e != player) {
								e.heal(amount / 2);
								Utils.reviveFromKO(e);
								player.level().playSound(null, e.position().x(), e.position().y(), e.position().z(), ModSounds.curaga.get(), SoundSource.PLAYERS, 1F, 1F);
							}
						}
					}

				}
				break;
			case 3:
				player.heal(amount);
				if (player instanceof Player p)
					p.getFoodData().eat(20, 10);

				if (worldData.getPartyFromMember(player.getUUID()) != null) {
					Party party = worldData.getPartyFromMember(player.getUUID());
					List<Member> list = party.getMembers();
					if (!list.isEmpty()) { // Heal everyone in the party within reach
						for (Member member : list) {
							if (player.level().getPlayerByUUID(member.getUUID()) != null && player.distanceTo(player.level().getPlayerByUUID(member.getUUID())) < ModConfigs.SERVER.partyRangeLimit.get()) {
								LivingEntity e = player.level().getPlayerByUUID(member.getUUID());
								if (e != null && Utils.isEntityInParty(party, e) && e != player) {
									e.heal(amount);
									if (e instanceof Player targetPlayer)
										targetPlayer.getFoodData().eat(20, 10);
								}
							}
						}
					}

				}
				break;
		}
		caster.swing(InteractionHand.MAIN_HAND);
	}

	@Override
	public void playMagicCastSound(LivingEntity player, Player caster) {
		switch (getTier()) {
			case 0 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.cure.get(), SoundSource.PLAYERS, 1F, 1F);
			case 1 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.cura.get(), SoundSource.PLAYERS, 1F, 1F);
			case 2 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.curaga.get(), SoundSource.PLAYERS, 1F, 1F);
			case 3 -> player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.curaga.get(), SoundSource.PLAYERS, 1F, 0.8F);
		}
	}

}
