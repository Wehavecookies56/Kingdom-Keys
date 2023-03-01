package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

import java.util.List;

import net.minecraft.world.entity.Entity.RemovalReason;

public class DriveOrbEntity extends ItemDropEntity {

	public DriveOrbEntity(Level worldIn, double x, double y, double z, int expValue) {
		super(ModEntities.TYPE_DRIVEORB.get(), worldIn, x, y, z, expValue);
	}

	public DriveOrbEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_DRIVEORB.get(), world);
	}

	public DriveOrbEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
	}
	

	@Override
	void onPickup(Player player) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		float finalValue = value;
		if (playerData.isAbilityEquipped(Strings.driveBoost) && playerData.getRecharge())
			finalValue *=2 ;
		if(playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()))
			playerData.addDP(finalValue);
		else {
			playerData.addFP(finalValue);
			if (playerData.getActiveDriveForm().equals(Strings.Form_Master)) {
				double mult = Double.parseDouble(ModConfigs.driveFormXPMultiplier.get(3).split(",")[1]);
				playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), (int) (playerData.getDriveFormExp(playerData.getActiveDriveForm()) + (Math.max(1, (value/10F) * mult)))); //Ensure at least 1 point
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)player);
			}
		}
	}

	@Override
	SoundEvent getPickupSound() {
		//return ModSounds.drive.get();
		return ModSounds.hp_orb.get();
	}
	
	@Override
	public void tick() {
		super.tick();
		//Merge with surrounding orbs
		List<Entity> list = level.getEntities(this, getBoundingBox().inflate(2.0D, 2.0D, 2.0D));
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i) instanceof ItemDropEntity) {
					ItemDropEntity e = (ItemDropEntity) list.get(i);
					if(e instanceof DriveOrbEntity) {
						if(this.tickCount > e.tickCount) {
							this.value += e.value;
							e.remove(RemovalReason.KILLED);
						}
					}
				}
			}
		}
	}
}
