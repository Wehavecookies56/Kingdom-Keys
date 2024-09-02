package online.kingdomkeys.kingdomkeys.entity;

import java.util.List;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class HPOrbEntity extends ItemDropEntity {

	public HPOrbEntity(Level worldIn, double x, double y, double z, int expValue) {
		super(ModEntities.TYPE_HPORB.get(), worldIn, x, y, z, expValue);
	}

	public HPOrbEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {

	}


	@Override
	void onPickup(Player player) {
		if(!PlayerData.get(player).getActiveDriveForm().equals(Strings.Form_Anti))
			player.heal(Math.min(this.value, 8));
	}

	@Override
	SoundEvent getPickupSound() {
		return ModSounds.hp_orb.get();
	}
	
	@Override
	public void tick() {
		super.tick();
		//Merge with surrounding orbs
		List<Entity> list = level().getEntities(this, getBoundingBox().inflate(2.0D, 2.0D, 2.0D));
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i) instanceof ItemDropEntity) {
					ItemDropEntity e = (ItemDropEntity) list.get(i);
					if(e instanceof HPOrbEntity) {
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
