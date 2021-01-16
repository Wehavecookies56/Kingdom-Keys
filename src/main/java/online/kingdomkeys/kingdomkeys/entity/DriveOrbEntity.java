package online.kingdomkeys.kingdomkeys.entity;

import java.util.List;

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
import online.kingdomkeys.kingdomkeys.config.CommonConfig;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
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
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		float finalValue = value;
		if (playerData.isAbilityEquipped(Strings.driveBoost) && playerData.getRecharge())
			finalValue *=2 ;
		if(playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()))
			playerData.addDP(finalValue);
		else {
			playerData.addFP(finalValue);
			if (playerData.getActiveDriveForm().equals(Strings.Form_Master)) {
				double mult = Double.parseDouble(CommonConfig.driveFormXPMultiplier.get().get(3).split(",")[1]);
				playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), (int) (playerData.getDriveFormExp(playerData.getActiveDriveForm()) + (value/10) * mult));
				PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity)player);
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
		List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, getBoundingBox().grow(2.0D, 2.0D, 2.0D));
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i) instanceof ItemDropEntity) {
					ItemDropEntity e = (ItemDropEntity) list.get(i);
					if(e instanceof DriveOrbEntity) {
						if(this.ticksExisted > e.ticksExisted) {
							this.value += e.value;
							e.remove();
						}
					}
				}
			}
		}
	}
}
