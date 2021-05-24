package online.kingdomkeys.kingdomkeys.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

public class FocusOrbEntity extends ItemDropEntity {

	public FocusOrbEntity(World worldIn, double x, double y, double z, int expValue) {
		super(ModEntities.TYPE_FOCUSORB.get(), worldIn, x, y, z, expValue);
	}

	public FocusOrbEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_FOCUSORB.get(), world);
	}

	public FocusOrbEntity(EntityType<? extends Entity> type, World world) {
		super(type, world);
	}
	

	@Override
	void onPickup(PlayerEntity player) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.addFocus(value);
	}

	@Override
	SoundEvent getPickupSound() {
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
					if(e instanceof FocusOrbEntity) {
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
