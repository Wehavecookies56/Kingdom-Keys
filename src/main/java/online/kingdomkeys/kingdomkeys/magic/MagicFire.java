package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class MagicFire extends Magic {

	public MagicFire(String registryName, int cost, int maxLevel, int order) {
		super(registryName, cost, false, maxLevel, order);
		this.name = registryName;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUse(PlayerEntity player, PlayerEntity caster, int level) {
		IPlayerCapabilities casterData = ModCapabilities.getPlayer(caster);
		casterData.setMagicCooldownTicks(20);
		PacketHandler.sendTo(new SCSyncCapabilityPacket(casterData), (ServerPlayerEntity)caster);
		
		switch(level) {
		
		}

		ThrowableEntity shot = new FireEntity(player.world, player);
		player.world.addEntity(shot);
		shot.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0, 2F, 0);
		player.world.playSound(null, player.getPosition(), SoundEvents.ENTITY_GHAST_SHOOT, SoundCategory.PLAYERS, 1F, 1F);
		player.swingArm(Hand.MAIN_HAND);
	}

}
