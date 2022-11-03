package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowOverlayPacket;

import java.util.List;

public class MunnyEntity extends ItemDropEntity {

	public MunnyEntity(Level worldIn, double x, double y, double z, int expValue) {
		super(ModEntities.TYPE_MUNNY.get(), worldIn, x, y, z, expValue);
	}

	public MunnyEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_MUNNY.get(), world);
	}

	public MunnyEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
	}

	@Override
	void onPickup(Player player) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setMunny(playerData.getMunny() + value);
		PacketHandler.sendTo(new SCShowOverlayPacket("munny", value), (ServerPlayer) player);
	}

	@Override
	SoundEvent getPickupSound() {
		return ModSounds.munny.get();
	}
	
	@Override
	public void tick() {
		super.tick();
		//Merge with surrounding orbs
		List<Entity> list = level.getEntities(this, getBoundingBox().inflate(2.0D, 2.0D, 2.0D));
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i) instanceof ItemDropEntity) {
					ItemDropEntity e = (ItemDropEntity) list.get(i);
					if(e instanceof MunnyEntity) {
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
