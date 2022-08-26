package online.kingdomkeys.kingdomkeys.magic;

import java.util.List;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MagicCure extends Magic {

	public MagicCure(String registryName, int maxLevel, String gmAbility, int order) {
		super(registryName, true, maxLevel, gmAbility, order);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult) {
		((ServerLevel) player.level).sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), player.getX(), player.getY()+2.3D, player.getZ(), 5, 0D, 0D, 0D, 0D);
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		IWorldCapabilities worldData = ModCapabilities.getWorld(player.level);

		float amount = playerData.getMaxHP() * getDamageMult(level);
		switch(level) {
		case 0:
			player.level.playSound(null, player.blockPosition(), ModSounds.cure.get(), SoundSource.PLAYERS, 1F, 1F);
			player.heal(amount);
			break;
		case 1:
			player.level.playSound(null, player.blockPosition(), ModSounds.cure.get(), SoundSource.PLAYERS, 1F, 1F);
			player.heal(amount);

			if(worldData.getPartyFromMember(player.getUUID()) != null) {
				//heal everyone including user
				Party party = worldData.getPartyFromMember(player.getUUID());
				List<LivingEntity> list = Utils.getLivingEntitiesInRadius(player, 3); 
		        if (!list.isEmpty()) {
		            for (int i = 0; i < list.size(); i++) {
		                LivingEntity e = list.get(i);
		                if (Utils.isEntityInParty(party, e) && e != player) {
		                	e.heal(amount / 2);
		            		player.level.playSound(null, e.blockPosition(), ModSounds.cure.get(), SoundSource.PLAYERS, 1F, 1F);
		                }
		            }
		        }
			}
			break;
		case 2:
			player.level.playSound(null, player.blockPosition(), ModSounds.cure.get(), SoundSource.PLAYERS, 1F, 1F);
			player.heal(amount);

			if(worldData.getPartyFromMember(player.getUUID()) != null) {
				Party party = worldData.getPartyFromMember(player.getUUID());
				List<LivingEntity> list = Utils.getLivingEntitiesInRadius(player, 5); 
		        if (!list.isEmpty()) {
		            for (int i = 0; i < list.size(); i++) {
		                LivingEntity e = list.get(i);
		                if (Utils.isEntityInParty(party, e) && e != player) {
		                	e.heal(amount / 2);
		            		player.level.playSound(null, e.blockPosition(), ModSounds.cure.get(), SoundSource.PLAYERS, 1F, 1F);
		                }
		            }
		        }

			}
			break;
		case 3:
			player.level.playSound(null, player.blockPosition(), ModSounds.cure.get(), SoundSource.PLAYERS, 1F, 0.9F);
			player.heal(amount);
			player.getFoodData().eat(20, 10);

			if(worldData.getPartyFromMember(player.getUUID()) != null) {
				Party party = worldData.getPartyFromMember(player.getUUID());
				List<Member> list = party.getMembers();
		        if (!list.isEmpty()) { //Heal everyone in the party within reach
		            for (int i = 0; i < list.size(); i++) {
		            	if(player.level.getPlayerByUUID(list.get(i).getUUID()) != null && player.distanceTo(player.level.getPlayerByUUID(list.get(i).getUUID())) < ModConfigs.partyRangeLimit) {
			                LivingEntity e = player.level.getPlayerByUUID(list.get(i).getUUID());
			                if (e != null && Utils.isEntityInParty(party, e) && e != player) {
			                	e.heal(amount);
			        			if(e instanceof Player targetPlayer)
			        				targetPlayer.getFoodData().eat(20, 10);
			            		player.level.playSound(null, e.blockPosition(), ModSounds.cure.get(), SoundSource.PLAYERS, 1F, 0.9F);
			                }
		            	}
		            }
		        }

			}
			break;
		}
		caster.swing(InteractionHand.MAIN_HAND);
	}

}
