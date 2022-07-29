package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.sound.AeroSoundInstance;

public class MagicAero extends Magic {

	public MagicAero(String registryName, int maxLevel, String gmAbility, int order) {
		super(registryName, true, maxLevel, gmAbility, order);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult) {
		player.level.playSound(null, player.blockPosition(), ModSounds.aero1.get(), SoundSource.PLAYERS, 1F, 1F);
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		int time = (int) (ModCapabilities.getPlayer(caster).getMaxMP() * (4F + level/2F) * getDamageMult(level));
		playerData.setAeroTicks(time, level);
		PacketHandler.syncToAllAround(player, playerData);
		caster.swing(InteractionHand.MAIN_HAND);
	}

}