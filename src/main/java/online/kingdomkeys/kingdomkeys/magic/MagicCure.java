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
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MagicCure extends Magic {

	public MagicCure(String registryName, int maxLevel, boolean hasRC, String gmAbility, int order) {
		super(registryName, true, maxLevel, hasRC, gmAbility, order);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult) {
		((ServerLevel) player.level).sendParticles(ParticleTypes.HAPPY_VILLAGER.getType(), player.getX(), player.getY()+2.3D, player.getZ(), 5, 0D, 0D, 0D, 0D);
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		IWorldCapabilities worldData = ModCapabilities.getWorld(player.level);
		player.level.playSound(null, player.blockPosition(), ModSounds.cure.get(), SoundSource.PLAYERS, 1F, 1F);

		float amount;
		switch(level) {
		case 0:
			amount = playerData.getMaxHP()/3 * getDamageMult(level);
			player.heal(amount);
			break;
		case 1:
			amount = playerData.getMaxHP()/2 * getDamageMult(level);
			player.heal(amount);

			if(worldData.getPartyFromMember(player.getUUID()) != null) {
				//heal everyone including user
				Party party = worldData.getPartyFromMember(player.getUUID());
				List<Entity> list = player.level.getEntities(player, player.getBoundingBox().inflate(3,3,3));
		        if (!list.isEmpty()) {
		            for (int i = 0; i < list.size(); i++) {
		                Entity e = (Entity) list.get(i);
		                if (e instanceof LivingEntity && Utils.isEntityInParty(party, e) && e != player) {
		                	((LivingEntity) e).heal(amount / 2);
		            		player.level.playSound(null, e.blockPosition(), ModSounds.cure.get(), SoundSource.PLAYERS, 1F, 1F);

		                }
		            }
		        }
			}
			break;
		case 2:
			amount = playerData.getMaxHP() * getDamageMult(level);
			player.heal(amount);

			if(worldData.getPartyFromMember(player.getUUID()) != null) {
				Party party = worldData.getPartyFromMember(player.getUUID());
				List<Entity> list = player.level.getEntities(player, player.getBoundingBox().inflate(4.0D, 4.0D, 4.0D));
		        if (!list.isEmpty()) {
		            for (int i = 0; i < list.size(); i++) {
		                Entity e = (Entity) list.get(i);
		                if (e instanceof LivingEntity && Utils.isEntityInParty(party, e) && e != player) {
		                	((LivingEntity) e).heal(amount / 2);
		            		player.level.playSound(null, e.blockPosition(), ModSounds.cure.get(), SoundSource.PLAYERS, 1F, 1F);
		                }
		            }
		        }
			}
			break;
		}
		caster.swing(InteractionHand.MAIN_HAND);
	}

}
