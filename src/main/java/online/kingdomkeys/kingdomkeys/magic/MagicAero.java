package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCAeroSoundPacket;

public class MagicAero extends Magic {

	public MagicAero(ResourceLocation registryName, int maxLevel, String gmAbility) {
		super(registryName, true, maxLevel, gmAbility);
	}

	@Override
	public void magicUse(Player player, Player caster, int level, float fullMPBlastMult, LivingEntity lockOnEntity) {
		IGlobalCapabilities globalData = ModData.getGlobal(player);
		int time = (int) (ModData.getPlayer(caster).getMaxMP() * (4F + level/2F) * getDamageMult(level));
		globalData.setAeroTicks(time, level);
		PacketHandler.syncToAllAround(player, globalData);
		PacketHandler.sendToAllPlayers(new SCAeroSoundPacket(player));
		caster.swing(InteractionHand.MAIN_HAND);
	}

	@Override
	protected void playMagicCastSound(Player player, Player caster, int level) {
		player.level().playSound(null, player.position().x(), player.position().y(), player.position().z(), ModSounds.aero1.get(), SoundSource.PLAYERS, 1F, 1F);
	}

}