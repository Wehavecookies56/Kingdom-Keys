package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class DriveOrbEntity extends ItemDropEntity {

	public DriveOrbEntity(World worldIn, double x, double y, double z, int expValue) {
		super(ModEntities.TYPE_DRIVEORB.get(), worldIn, x, y, z, expValue);
	}

	public DriveOrbEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_DRIVEORB.get(), world);
	}

	public DriveOrbEntity(EntityType<? extends Entity> type, World world) {
		super(type, world);
	}
	

	@Override
	void onPickup(PlayerEntity player) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		if(props.getActiveDriveForm().equals(""))
			props.addDP(value);
		else {
			props.addFP(value);

			if (props.getActiveDriveForm().equals(Strings.Form_Master)) {
				props.setDriveFormExp(player, props.getActiveDriveForm(), props.getDriveFormExp(props.getActiveDriveForm()) + value/10);
				PacketHandler.sendTo(new SCSyncCapabilityPacket(props), (ServerPlayerEntity)player);
			}
		}
	}

	@Override
	SoundEvent getPickupSound() {
		//return ModSounds.drive.get();
		return ModSounds.hp_orb.get();
	}
}
