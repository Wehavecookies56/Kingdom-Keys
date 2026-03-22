package online.kingdomkeys.kingdomkeys.entity.drops;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.util.List;

public class HPOrbEntity extends ItemDropEntity {

	public HPOrbEntity(Level worldIn, double x, double y, double z, int expValue) {
		super(ModEntities.TYPE_HPORB.get(), worldIn, x, y, z, expValue);
	}

	public HPOrbEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
	}

	@Override
	void onPickup(Player player) {
		if(!PlayerData.get(player).getActiveDriveForm().equals(Strings.Form_Anti))
			player.heal(Math.min(this.value, 4));
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
            List<HPOrbEntity> list = level().getEntitiesOfClass(HPOrbEntity.class, getBoundingBox().inflate(1.5, 1, 1.5));
            if (!list.isEmpty()) {
                for (HPOrbEntity e : list) {
                    if (this.tickCount > e.tickCount) {
                        this.value += e.value;
                        e.remove(RemovalReason.KILLED);
                    }
                }
            }
		}
	}
}
