package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.Hand;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.magic.GravigaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.GraviraEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.GravityEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class MagicGravity extends Magic {

	public MagicGravity(String registryName, int cost, int maxLevel, int order) {
		super(registryName, cost, false, maxLevel, order);
		this.name = registryName;
	}

	@Override
	public void onUse(PlayerEntity player, PlayerEntity caster, int level) {
		IPlayerCapabilities casterData = ModCapabilities.getPlayer(caster);
		casterData.setMagicCooldownTicks(40);
		PacketHandler.sendTo(new SCSyncCapabilityPacket(casterData), (ServerPlayerEntity)caster);

		switch(level) {
		case 0:
			ThrowableEntity gravity = new GravityEntity(player.world, player);
			player.world.addEntity(gravity);
			gravity.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 2F, 0);
			break;
		case 1:
			ThrowableEntity gravira = new GraviraEntity(player.world, player);
			player.world.addEntity(gravira);
			gravira.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 2.3F, 0);
			break;
		case 2:
			ThrowableEntity graviga = new GravigaEntity(player.world, player);
			player.world.addEntity(graviga);
			graviga.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 2.6F, 0);
			break;
		}		
		player.swingArm(Hand.MAIN_HAND);
	}

}
