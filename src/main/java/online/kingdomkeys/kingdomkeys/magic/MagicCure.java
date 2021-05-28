package online.kingdomkeys.kingdomkeys.magic;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.server.ServerWorld;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class MagicCure extends Magic {

	public MagicCure(String registryName, int maxLevel, boolean hasRC, int order) {
		super(registryName, true, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(PlayerEntity player, PlayerEntity caster, int level) {
		((ServerWorld) player.world).spawnParticle(ParticleTypes.HAPPY_VILLAGER.getType(), player.getPosX(), player.getPosY()+2.3D, player.getPosZ(), 5, 0D, 0D, 0D, 0D);
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		IWorldCapabilities worldData = ModCapabilities.getWorld(player.world);

		float amount;
		switch(level) {
		case 0:
			amount = playerData.getMaxHP()/3 * getDamageMult(level);
			player.heal(amount);
			break;
		case 1:
			amount = playerData.getMaxHP()/2 * getDamageMult(level);
			player.heal(amount);

			if(worldData.getPartyFromMember(player.getUniqueID()) != null) {
				//heal everyone including user
				Party party = worldData.getPartyFromMember(player.getUniqueID());
				List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(3,3,3));
		        if (!list.isEmpty()) {
		            for (int i = 0; i < list.size(); i++) {
		                Entity e = (Entity) list.get(i);
		                if (e instanceof LivingEntity && Utils.isEntityInParty(party, e) && e != player) {
		                	((LivingEntity) e).heal(amount / 2);
		                }
		            }
		        }
			}
			player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1F, 1F);
			break;
		case 2:
			amount = playerData.getMaxHP() * getDamageMult(level);
			player.heal(amount);

			if(worldData.getPartyFromMember(player.getUniqueID()) != null) {
				Party party = worldData.getPartyFromMember(player.getUniqueID());
				List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(4.0D, 4.0D, 4.0D));
		        if (!list.isEmpty()) {
		            for (int i = 0; i < list.size(); i++) {
		                Entity e = (Entity) list.get(i);
		                if (e instanceof LivingEntity && Utils.isEntityInParty(party, e) && e != player) {
		                	((LivingEntity) e).heal(amount / 2);
		                }
		            }
		        }
			}
			break;
		}
		caster.swingArm(Hand.MAIN_HAND);
	}

}
