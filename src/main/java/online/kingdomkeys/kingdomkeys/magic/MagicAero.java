package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class MagicAero extends Magic {

	public MagicAero(String registryName, int maxLevel, boolean hasRC, int order) {
		super(registryName, true, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(PlayerEntity player, PlayerEntity caster, int level) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		int time = (int) (ModCapabilities.getPlayer(caster).getMaxMP() * (4F + level/2F) * getDamageMult());
		playerData.setAeroTicks(time, level);
		PacketHandler.syncToAllAround(player, playerData);
		caster.swingArm(Hand.MAIN_HAND);
	}

}