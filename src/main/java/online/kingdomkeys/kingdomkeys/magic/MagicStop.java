package online.kingdomkeys.kingdomkeys.magic;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncGlobalCapabilityPacket;

public class MagicStop extends Magic {

	public MagicStop(String registryName, int maxLevel, boolean hasRC, int order) {
		super(registryName, false, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(PlayerEntity player, PlayerEntity caster, int level, float fullMPBlastMult) {
		float dmg = /*ModCapabilities.getPlayer(player).isAbilityEquipped(Strings.waterBoost) ? getDamageMult(level) * 1.2F :*/ getDamageMult(level);
		dmg *= fullMPBlastMult;
		
		float radius = 2 + level;
		List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(radius, radius, radius));
		Party casterParty = ModCapabilities.getWorld(player.world).getPartyFromMember(player.getUniqueID());

		if (casterParty != null && !casterParty.getFriendlyFire()) {
			for (Member m : casterParty.getMembers()) {
				list.remove(player.world.getPlayerByUuid(m.getUUID()));
			}
		}

		player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_BELL_USE, SoundCategory.PLAYERS, 1F, 1F);

		if (!list.isEmpty()) {
			player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_BELL_RESONATE, SoundCategory.PLAYERS, 1F, 1F);
			for (int i = 0; i < list.size(); i++) {
				Entity e = (Entity) list.get(i);
				if (e instanceof LivingEntity) {
					IGlobalCapabilities globalData = ModCapabilities.getGlobal((LivingEntity) e);
					if (e instanceof MobEntity) {
						((MobEntity) e).setNoAI(true);
					}
					globalData.setStoppedTicks((int) (100 + level * 20 * dmg)); // Stop
					globalData.setStopCaster(player.getDisplayName().getString());
					if (e instanceof ServerPlayerEntity)
						PacketHandler.sendTo(new SCSyncGlobalCapabilityPacket(globalData), (ServerPlayerEntity) e);
				}
			}
		}
		player.swingArm(Hand.MAIN_HAND);
	}

}
