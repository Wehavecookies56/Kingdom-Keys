package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class MagicAero extends Magic {

	public MagicAero(String registryName, int maxLevel, boolean hasRC, int order) {
		super(registryName, true, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		int time = (int) (ModCapabilities.getPlayer(caster).getMaxMP() * (4F + level/2F) * getDamageMult(level));
		playerData.setAeroTicks(time, level);
		PacketHandler.syncToAllAround(player, playerData);
		caster.swing(InteractionHand.MAIN_HAND);
	}

}