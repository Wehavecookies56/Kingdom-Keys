package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class MagicAero extends Magic {

	public MagicAero(ResourceLocation registryName, int maxLevel, String gmAbility, int order) {
		super(registryName, true, maxLevel, gmAbility, order);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult) {
		player.level.playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.aero1.get(), SoundSource.PLAYERS, 1F, 1F);
		IGlobalCapabilities globalData = ModCapabilities.getGlobal(player);
		int time = (int) (ModCapabilities.getPlayer(caster).getMaxMP() * (4F + level/2F) * getDamageMult(level));
		globalData.setAeroTicks(time, level);
		PacketHandler.syncToAllAround(player, globalData);
		caster.swing(InteractionHand.MAIN_HAND);
	}

}