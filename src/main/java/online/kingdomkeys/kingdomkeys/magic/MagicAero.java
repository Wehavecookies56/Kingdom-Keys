package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public class MagicAero extends Magic {

	public MagicAero(String registryName, int cost, int order) {
		super(registryName, cost, true, order);
		this.name = registryName;
	}

	@Override
	public void onUse(PlayerEntity player, PlayerEntity caster) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setAeroTicks((int) (ModCapabilities.getPlayer(caster).getMaxMP() * 3));
		PacketHandler.syncToAllAround(player, playerData);
		caster.swingArm(Hand.MAIN_HAND);
	}

}
