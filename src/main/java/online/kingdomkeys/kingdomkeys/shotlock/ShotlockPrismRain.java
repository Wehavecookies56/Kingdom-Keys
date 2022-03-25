package online.kingdomkeys.kingdomkeys.shotlock;

import java.util.List;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.shotlock.PrismRainCoreEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID)
public class ShotlockPrismRain extends Shotlock {

	public ShotlockPrismRain(String registryName, int order, int cooldown, int max) {
		super(registryName, order, cooldown, max);
	}

	@Override
	public void onUse(Player player, List<Entity> targetList) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setLimitCooldownTicks(cooldown);
		PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)player);
		float damage = (float) (DamageCalculation.getMagicDamage(player) * ModConfigs.shotlockMult);
		PrismRainCoreEntity core = new PrismRainCoreEntity(player.level, player, targetList, damage);
		core.setPos(player.getX(), player.getY(), player.getZ());
		player.level.addFreshEntity(core);
	}
}