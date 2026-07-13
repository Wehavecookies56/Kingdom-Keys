package online.kingdomkeys.kingdomkeys.entity.drops;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

import java.util.List;

public class DriveOrbEntity extends ItemDropEntity {

	public DriveOrbEntity(Level worldIn, double x, double y, double z, int expValue) {
		super(ModEntities.TYPE_DRIVEORB.get(), worldIn, x, y, z, expValue);
	}

	public DriveOrbEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
	}

	@Override
	void onPickup(Player player) {
		PlayerData playerData = PlayerData.get(player);
		float finalValue = value;
		if (playerData.isAbilityEquipped(ModAbilities.DRIVE_BOOST) && playerData.getRecharge())
			finalValue *=2 ;
		if(playerData.isFormActive(ModDriveForms.NONE))
			playerData.addDP(player,finalValue);
		else {
			playerData.addFP(finalValue);
			if (playerData.isFormActive(ModDriveForms.MASTER)) {
				double mult = Double.parseDouble(ModConfigs.SERVER.driveFormXPMultiplier.get().get(3).split(",")[1]);
				playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), (int) (playerData.getDriveFormExp(playerData.getActiveDriveForm()) + (Math.max(1, (value/10F) * mult)))); //Ensure at least 1 point
				PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer)player);
			}
		}
	}

	@Override
	public SoundEvent getPickupSound() {
		return ModSounds.hp_orb.get();
	}
	
	@Override
	public void tick() {
		super.tick();
		//Merge with surrounding orbs
        if(tickCount % 5 == 0) {
            List<DriveOrbEntity> list = level().getEntitiesOfClass(DriveOrbEntity.class, getBoundingBox().inflate(1.5, 1, 1.5));
            if (!list.isEmpty()) {
                for (DriveOrbEntity e : list) {
                    if (this.tickCount > e.tickCount) {
                        this.value += e.value;
                        e.remove(RemovalReason.KILLED);
                    }
                }
			}
		}
	}
}
