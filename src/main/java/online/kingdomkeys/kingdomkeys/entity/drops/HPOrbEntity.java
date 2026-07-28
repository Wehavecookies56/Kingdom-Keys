package online.kingdomkeys.kingdomkeys.entity.drops;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;


public class HPOrbEntity extends ItemDropEntity {

	public HPOrbEntity(Level worldIn, double x, double y, double z, int expValue) {
		super(ModEntities.TYPE_HPORB.get(), worldIn, x, y, z, expValue);
	}

	public HPOrbEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
	}

	@Override
	void onPickup(Player player) {
		if(!PlayerData.get(player).isFormActive(ModDriveForms.ANTI))
			player.heal(Math.min(this.value, 4));
	}

	@Override
	public SoundEvent getPickupSound() {
		return ModSounds.hp_orb.get();
	}
	
}
