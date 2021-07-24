package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class MagicReflect extends Magic {

	public MagicReflect(String registryName, int maxLevel, boolean hasRC, int order) {
		super(registryName, false, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setReflectTicks((int) (40 + (level * 5)), level);
		player.level.playSound(null, player.blockPosition(), ModSounds.reflect1.get(), SoundSource.PLAYERS, 1F, 1F);
		PacketHandler.syncToAllAround(player, playerData);
		player.swing(InteractionHand.MAIN_HAND);
	}

}
