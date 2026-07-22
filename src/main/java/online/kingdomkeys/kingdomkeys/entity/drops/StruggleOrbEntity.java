package online.kingdomkeys.kingdomkeys.entity.drops;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldData;

public class StruggleOrbEntity extends ItemDropEntity {

	private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(StruggleOrbEntity.class, EntityDataSerializers.INT);

	/** Name of the Struggle match this orb belongs to - only its participants can pick it up. */
	private String struggleName = "";

	public StruggleOrbEntity(Level worldIn, double x, double y, double z, int color, String struggleName) {
		super(ModEntities.TYPE_STRUGGLE_ORB.get(), worldIn, x, y, z, 0);
		this.setColor(color);
		this.struggleName = struggleName;
	}

	public StruggleOrbEntity(EntityType<? extends Entity> type, Level world) {
		super(type, world);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(COLOR, 0xFFFFFF);
	}

	public void setColor(int color) {
		this.entityData.set(COLOR, color);
	}

	public int getColor() {
		return this.entityData.get(COLOR);
	}

	public String getStruggleName() {
		return this.struggleName;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putString("struggle_name", struggleName);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		struggleName = compound.getString("struggle_name");
	}

	@Override
	public void playerTouch(Player entityIn) {
		if (!this.level().isClientSide) {
			WorldData worldData = WorldData.get(entityIn.getServer());
			Struggle struggle = worldData.getStruggleFromName(this.struggleName);
			if (struggle == null || !struggle.hasParticipant(entityIn.getUUID())) {
				return; // not a participant of this match (or it no longer exists) - can't pick this up
			}
		}
		super.playerTouch(entityIn);
	}

	@Override
	void onPickup(Player player) {
		WorldData worldData = WorldData.get(player.getServer());
		Struggle struggle = worldData.getStruggleFromName(this.struggleName);
		if (struggle == null) return;

		Struggle.Participant participant = struggle.getParticipant(player.getUUID());
		if (participant == null) return;

		participant.setScore(participant.getScore() + 1);
		worldData.setDirty();
		PacketHandler.sendToAll(new SCSyncWorldData(player.getServer()));
	}

	@Override
	public SoundEvent getPickupSound() {
		return ModSounds.hp_orb.get();
	}
}
