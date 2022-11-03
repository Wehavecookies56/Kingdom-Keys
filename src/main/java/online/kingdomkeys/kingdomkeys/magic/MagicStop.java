package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.mob.MarluxiaEntity;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncGlobalCapabilityPacket;

import java.util.List;

public class MagicStop extends Magic {

	public MagicStop(String registryName, int maxLevel, String gmAbility, int order) {
		super(registryName, false, maxLevel,  gmAbility, order);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult) {
		float dmg = /*ModCapabilities.getPlayer(player).isAbilityEquipped(Strings.waterBoost) ? getDamageMult(level) * 1.2F :*/ getDamageMult(level);
		dmg *= fullMPBlastMult;
		
		float radius = 2 + level;
		List<Entity> list = player.level.getEntities(player, player.getBoundingBox().inflate(radius, radius, radius));
		Party casterParty = ModCapabilities.getWorld(player.level).getPartyFromMember(player.getUUID());

		if (casterParty != null && !casterParty.getFriendlyFire()) {
			for (Member m : casterParty.getMembers()) {
				list.remove(player.level.getPlayerByUUID(m.getUUID()));
			}
		}
		
		for(Entity e : list) {
			if(e instanceof MarluxiaEntity) {
				list.remove(e);
			}
		}

		player.level.playSound(null, player.blockPosition(), ModSounds.stop.get(), SoundSource.PLAYERS, 1F, 1F);
		if (!list.isEmpty()) {
			//player.level.playSound(null, player.blockPosition(), SoundEvents.BELL_RESONATE, SoundSource.PLAYERS, 1F, 1F);
			for (int i = 0; i < list.size(); i++) {
				Entity e = (Entity) list.get(i);
				if (e instanceof LivingEntity) {
					IGlobalCapabilities globalData = ModCapabilities.getGlobal((LivingEntity) e);
					if (e instanceof Mob) {
						((Mob) e).setNoAi(true);
					}
					globalData.setStoppedTicks((int) (100 + level * 20 * dmg)); // Stop
					globalData.setStopCaster(player.getDisplayName().getString());
					if (e instanceof ServerPlayer)
						PacketHandler.sendTo(new SCSyncGlobalCapabilityPacket(globalData), (ServerPlayer) e);
				}
			}
		}
		player.swing(InteractionHand.MAIN_HAND);
	}

}
