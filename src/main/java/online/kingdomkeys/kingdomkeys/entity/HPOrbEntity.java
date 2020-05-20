package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

public class HPOrbEntity extends ItemDropEntity {

	public HPOrbEntity(World worldIn, double x, double y, double z, int expValue) {
		super(ModEntities.TYPE_HPORB.get(), worldIn, x, y, z, expValue);
	}

	public HPOrbEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_HPORB.get(), world);
	}

	public HPOrbEntity(EntityType<? extends Entity> type, World world) {
		super(type, world);
	}
	

	@Override
	void onPickup(PlayerEntity player) {
		player.heal(this.value);
	}

	@Override
	SoundEvent getPickupSound() {
		return ModSounds.hp_orb.get();
	}
}
