package online.kingdomkeys.kingdomkeys.entity.drops;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

import java.util.List;

public class MPOrbEntity extends ItemDropEntity {

	public MPOrbEntity(Level worldIn, double x, double y, double z, int expValue) {
		super(ModEntities.TYPE_MPORB.get(), worldIn, x, y, z, expValue);
	}

	public MPOrbEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
	}
	

	@Override
	void onPickup(Player player) {
		PlayerData playerData = PlayerData.get(player);
		playerData.addMP(value);
	}

	@Override
	SoundEvent getPickupSound() {
		return ModSounds.mp_orb.get();
	}
	
	@Override
	public void tick() {
		super.tick();
		//Merge with surrounding orbs
        if(tickCount % 5 == 0) {
            List<MPOrbEntity> list = level().getEntitiesOfClass(MPOrbEntity.class, getBoundingBox().inflate(1.5, 1, 1.5));
            if (!list.isEmpty()) {
                for (MPOrbEntity e : list) {
                    if (this.tickCount > e.tickCount) {
                        this.value += e.value;
                        e.remove(RemovalReason.KILLED);
                    }
                }
            }
		}
	}
}
