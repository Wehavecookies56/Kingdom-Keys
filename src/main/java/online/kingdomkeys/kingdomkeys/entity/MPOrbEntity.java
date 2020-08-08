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

public class MPOrbEntity extends ItemDropEntity {

	public MPOrbEntity(World worldIn, double x, double y, double z, int expValue) {
		super(ModEntities.TYPE_MPORB.get(), worldIn, x, y, z, expValue);
	}

	public MPOrbEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_MPORB.get(), world);
	}

	public MPOrbEntity(EntityType<? extends Entity> type, World world) {
		super(type, world);
	}
	

	@Override
	void onPickup(PlayerEntity player) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.addMP(value);
	}

	@Override
	SoundEvent getPickupSound() {
		return ModSounds.mp_orb.get();
	}
}
