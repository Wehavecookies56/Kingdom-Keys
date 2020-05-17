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

public class EntityMPOrb extends EntityItemDrop {

	public EntityMPOrb(World worldIn, double x, double y, double z, int expValue) {
		super(ModEntities.TYPE_MPORB.get(), worldIn, x, y, z, expValue);
	}

	public EntityMPOrb(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_MPORB.get(), world);
	}

	public EntityMPOrb(EntityType<? extends Entity> type, World world) {
		super(type, world);
	}
	

	@Override
	void onPickup(PlayerEntity player) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		props.addMP(value);
	}

	@Override
	SoundEvent getPickupSound() {
		return ModSounds.antidrive.get();
	}
}
