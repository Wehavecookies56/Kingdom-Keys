package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class MagicAero extends Magic {

	public MagicAero(String registryName, int cost, int maxLevel, boolean hasRC, int order) {
		super(registryName, cost, true, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(PlayerEntity player, PlayerEntity caster, int level) {
		IPlayerCapabilities casterData = ModCapabilities.getPlayer(caster);
		casterData.setMagicCooldownTicks(20);
		PacketHandler.sendTo(new SCSyncCapabilityPacket(casterData), (ServerPlayerEntity)caster);

		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		int time = (int) (ModCapabilities.getPlayer(caster).getMaxMP() * (4F + level/2F));
		playerData.setAeroTicks(time, level);
		PacketHandler.syncToAllAround(player, playerData);
		caster.swingArm(Hand.MAIN_HAND);
	}

}