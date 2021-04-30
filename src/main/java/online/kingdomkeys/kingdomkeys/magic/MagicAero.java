package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class MagicAero extends Magic {

	public MagicAero(String registryName, int cost, int order) {
		super(registryName, cost, true, order);
		this.name = registryName;
	}

	@Override
	public void onUse(PlayerEntity player, PlayerEntity caster) {
		IPlayerCapabilities casterData = ModCapabilities.getPlayer(caster);
		casterData.setMagicCooldownTicks(20);
		PacketHandler.sendTo(new SCSyncCapabilityPacket(casterData), (ServerPlayerEntity)caster);

		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setAeroTicks((int) (ModCapabilities.getPlayer(caster).getMaxMP() * 4));
		PacketHandler.syncToAllAround(player, playerData);
		caster.swingArm(Hand.MAIN_HAND);
	}

}