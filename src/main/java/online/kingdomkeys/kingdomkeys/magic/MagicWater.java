package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.magic.WatergaEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WaterEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.WateraEntity;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class MagicWater extends Magic {

	public MagicWater(String registryName, int cost, int maxLevel, boolean hasRC, int order) {
		super(registryName, cost, false, maxLevel, hasRC, order);
	}

	@Override
	protected void magicUse(PlayerEntity player, PlayerEntity caster, int level) {
		IPlayerCapabilities casterData = ModCapabilities.getPlayer(caster);
		casterData.setMagicCooldownTicks(50 + 20);
		PacketHandler.sendTo(new SCSyncCapabilityPacket(casterData), (ServerPlayerEntity)caster);

		switch(level) {
		case 0:
			WaterEntity water = new WaterEntity(player.world, player);
			water.setCaster(player.getDisplayName().getString());
			player.world.addEntity(water);
			player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.PLAYERS, 1F, 1F);
			break;
		case 1:
			WateraEntity watera = new WateraEntity(player.world, player);
			watera.setCaster(player.getDisplayName().getString());
			player.world.addEntity(watera);
			player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.PLAYERS, 1F, 1F);
			break;
		case 2:
			WatergaEntity waterga = new WatergaEntity(player.world, player);
			waterga.setCaster(player.getDisplayName().getString());
			player.world.addEntity(waterga);
			player.world.playSound(null, player.getPosition(), SoundEvents.BLOCK_WATER_AMBIENT, SoundCategory.PLAYERS, 1F, 1F);
			break;
		}
		
		player.swingArm(Hand.MAIN_HAND);
		if(player.isBurning()) {
			player.extinguish();
		}
	}

}
