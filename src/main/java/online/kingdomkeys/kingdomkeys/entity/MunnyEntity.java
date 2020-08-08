package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowOverlayPacket;

public class MunnyEntity extends ItemDropEntity {

	public MunnyEntity(World worldIn, double x, double y, double z, int expValue) {
		super(ModEntities.TYPE_MUNNY.get(), worldIn, x, y, z, expValue);
	}

	public MunnyEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_MUNNY.get(), world);
	}

	public MunnyEntity(EntityType<? extends Entity> type, World world) {
		super(type, world);
	}

	@Override
	void onPickup(PlayerEntity player) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setMunny(playerData.getMunny() + value);
		PacketHandler.sendTo(new SCShowOverlayPacket("munny", value), (ServerPlayerEntity) player);
	}

	@Override
	SoundEvent getPickupSound() {
		return ModSounds.munny.get();
	}
}
