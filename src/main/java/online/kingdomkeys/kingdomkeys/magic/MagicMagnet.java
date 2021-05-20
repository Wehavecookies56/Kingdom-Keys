package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnegaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagneraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.MagnetEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class MagicMagnet extends Magic {

	public MagicMagnet(String registryName, int cost, int maxLevel, boolean hasRC, int order) {
		super(registryName, cost, false, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(PlayerEntity player, PlayerEntity caster, int level) {
		IPlayerCapabilities casterData = ModCapabilities.getPlayer(caster);
		casterData.setMagicCooldownTicks(40);
		PacketHandler.sendTo(new SCSyncCapabilityPacket(casterData), (ServerPlayerEntity)caster);

		switch(level) {
		case 0:
			MagnetEntity magent = new MagnetEntity(player.world, player);
			magent.setCaster(player.getUniqueID());
			player.world.addEntity(magent);
			magent.setDirectionAndMovement(player, -90, player.rotationYaw, 0, 1F, 0);
			break;
		case 1:
			MagneraEntity magnera = new MagneraEntity(player.world, player);
			magnera.setCaster(player.getUniqueID());
			player.world.addEntity(magnera);
			magnera.setDirectionAndMovement(player, -90, player.rotationYaw, 0, 1F, 0);
			break;
		case 2:
			MagnegaEntity magnega = new MagnegaEntity(player.world, player);
			magnega.setCaster(player.getUniqueID());
			player.world.addEntity(magnega);
			magnega.setDirectionAndMovement(player, -90, player.rotationYaw, 0, 1F, 0);
			break;
		}
		
		player.swingArm(Hand.MAIN_HAND);
	}

}
