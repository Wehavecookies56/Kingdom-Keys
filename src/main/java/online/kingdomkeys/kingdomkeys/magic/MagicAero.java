package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCAeroSoundPacket;

public class MagicAero extends Magic {

	public MagicAero(ResourceLocation registryName, int maxLevel, String gmAbility) {
		super(registryName, true, maxLevel, gmAbility);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		player.level().playSound(null, player.blockPosition(), ModSounds.aero1.get(), SoundSource.PLAYERS, 1F, 1F);
		IGlobalCapabilities globalData = ModCapabilities.getGlobal(player);
		int time = (int) (ModCapabilities.getPlayer(caster).getMaxMP() * (4F + level/2F) * getDamageMult(level));
		globalData.setAeroTicks(time, level);
		PacketHandler.syncToAllAround(player, globalData);
		PacketHandler.sendToAllPlayers(new SCAeroSoundPacket(player));
		caster.swing(InteractionHand.MAIN_HAND);
	}

}