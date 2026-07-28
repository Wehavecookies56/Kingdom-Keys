package online.kingdomkeys.kingdomkeys.entity.drops;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCShowOverlayPacket;


public class MunnyEntity extends ItemDropEntity {

	public MunnyEntity(Level worldIn, double x, double y, double z, int expValue) {
		super(ModEntities.TYPE_MUNNY.get(), worldIn, x, y, z, expValue);
	}

	public MunnyEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
	}

	@Override
	void onPickup(Player player) {
		PlayerData playerData = PlayerData.get(player);
		playerData.setMunny(playerData.getMunny() + value, (ServerPlayer) player);
		PacketHandler.sendTo(new SCShowOverlayPacket("munny", value), (ServerPlayer) player);
	}

	@Override
	public SoundEvent getPickupSound() {
		return ModSounds.munny.get();
	}
	
}
