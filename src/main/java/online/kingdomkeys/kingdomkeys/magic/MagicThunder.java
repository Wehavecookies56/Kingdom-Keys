package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.magic.ThundagaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThundaraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.ThunderEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class MagicThunder extends Magic {

	public MagicThunder(String registryName, int cost, int maxLevel, int order) {
		super(registryName, cost, false, maxLevel, order);
		this.name = registryName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUse(PlayerEntity player, PlayerEntity caster, int level) {
		IPlayerCapabilities casterData = ModCapabilities.getPlayer(caster);
		casterData.setMagicCooldownTicks(50);
		PacketHandler.sendTo(new SCSyncCapabilityPacket(casterData), (ServerPlayerEntity)caster);

		switch(level) {
		case 0:
			ThunderEntity thunderController = new ThunderEntity(player.world, player);
			thunderController.setCaster(player.getUniqueID());
			player.world.addEntity(thunderController);
			player.swingArm(Hand.MAIN_HAND);
			break;
		case 1:
			ThundaraEntity thundaraController = new ThundaraEntity(player.world, player);
			thundaraController.setCaster(player.getUniqueID());
			player.world.addEntity(thundaraController);
			player.swingArm(Hand.MAIN_HAND);
			break;
		case 2:
			ThundagaEntity thundagaController = new ThundagaEntity(player.world, player);
			thundagaController.setCaster(player.getUniqueID());
			player.world.addEntity(thundagaController);
			player.swingArm(Hand.MAIN_HAND);
			break;
		}
	}

}
