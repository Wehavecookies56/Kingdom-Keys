package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

public class EntityDriveOrb extends EntityItemDrop {

	public EntityDriveOrb(World worldIn, double x, double y, double z, int expValue) {
		super(ModEntities.TYPE_DRIVEORB.get(), worldIn, x, y, z, expValue);
	}

	public EntityDriveOrb(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_DRIVEORB.get(), world);
	}

	public EntityDriveOrb(EntityType<? extends Entity> type, World world) {
		super(type, world);
	}
	

	@Override
	void onPickup(PlayerEntity player) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		props.addDP(value);
	}

	@Override
	SoundEvent getPickupSound() {
		return ModSounds.drive.get();
	}
}
