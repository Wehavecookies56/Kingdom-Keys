package online.kingdomkeys.kingdomkeys.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

public class Struggle {

	public static final byte PARTICIPANTS_LIMIT = 8;

	private String name;
	private List<Participant> participants = new ArrayList<Participant>();
	//private boolean priv;
	private byte size;
	private float damageMult;
	public BlockPos blockPos;

	public Struggle() {

	}

	public Struggle(BlockPos blockPos, String name, UUID leaderId, String username, boolean priv, byte size) {
		this.name = name;
		this.addParticipant(leaderId, username).setIsOwner();
		//this.priv = priv;
		this.size = size;
		this.damageMult = 1.0F;
		this.blockPos = blockPos;
	}

	public void setPos(BlockPos pos) {
		this.blockPos = pos;
	}

	public BlockPos getPos() {
		return this.blockPos;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	/*public void setPriv(boolean priv) {
		this.priv = priv;
	}

	public boolean getPriv() {
		return this.priv;
	}*/

	public void setSize(byte size) {
		this.size = size;
	}

	public byte getSize() {
		return this.size;
	}

	public void setDamageMult(float val) {
		this.damageMult = val;
	}

	public float getDamageMult() {
		return this.damageMult;
	}

	public Participant addParticipant(LivingEntity entity) {
		return this.addParticipant(entity.getUUID(), entity.getDisplayName().getString());
	}

	public Participant addParticipant(UUID uuid, String username) {
		Participant participant = new Participant(uuid, username);
		this.participants.add(participant);
		return participant;
	}

	public void removeParticipant(UUID id) {
		Participant participant = this.getParticipant(id);
		/*if (participant.isLeader())
			this.participants.removeAll(this.participants);
		else*/
			this.participants.remove(participant);
	}

	public Participant getParticipant(UUID id) {
		return this.participants.stream().filter(participant -> participant.getUUID().equals(id)).findFirst().orElse(null);
	}

	public boolean hasParticipant(UUID id) {
		return this.getParticipant(id) != null;
	}

	@Nullable
	public Participant getOwner() {
		return this.participants.stream().filter(participant -> participant.isOwner()).findFirst().orElse(null);
	}

	public List<Participant> getParticipants() {
		return this.participants;
	}

	public int getParticipantIndex(UUID participantUUID) {
		int i = 0;
		for (i = 0; i < participants.size(); i++) {
			if (participants.get(i).getUUID().equals(participantUUID)) {
				return i;
			}
		}
		return -1;
	}

	public CompoundTag write() {
		CompoundTag struggleNBT = new CompoundTag();
		struggleNBT.putString("name", this.getName());
		//partyNBT.putBoolean("private", this.priv);
		struggleNBT.putByte("size", this.size);
		struggleNBT.putFloat("dmg_mult", this.damageMult);
		struggleNBT.putIntArray("posArray", new int[] {this.blockPos.getX(),this.blockPos.getY(),this.blockPos.getZ()});

		ListTag participants = new ListTag();
		for (Struggle.Participant participant : this.getParticipants()) {
			CompoundTag participantNBT = new CompoundTag();
			participantNBT.putUUID("id", participant.getUUID());
			participantNBT.putString("username", participant.getUsername());
			participantNBT.putBoolean("isOwner", participant.isOwner());
			participants.add(participantNBT);
		}
		struggleNBT.put("participants", participants);

		return struggleNBT;
	}

	public void read(CompoundTag nbt) {
		this.setName(nbt.getString("name"));
		//this.setPriv(nbt.getBoolean("private"));
		this.setSize(nbt.getByte("size"));
		this.setDamageMult(nbt.getFloat("dmg_mult"));
		int[] posArray = nbt.getIntArray("posArray");
		BlockPos pos = new BlockPos(posArray[0],posArray[1],posArray[2]);
		this.setPos(pos);
		ListTag participants = nbt.getList("participants", Tag.TAG_COMPOUND);
		for (int j = 0; j < participants.size(); j++) {
			CompoundTag participantNBT = participants.getCompound(j);
			Struggle.Participant participant = this.addParticipant(participantNBT.getUUID("id"), participantNBT.getString("username"));
			if (participantNBT.getBoolean("isOwner"))
				participant.setIsOwner();
		}

	}

	public static class Participant {
		private UUID uuid;
		private String username;
		private boolean isOwner;

		public Participant(LivingEntity entity) {
			this(entity.getUUID(), entity.getDisplayName().getString());
		}

		public Participant(UUID uuid, String username) {
			this.uuid = uuid;
			this.username = username;
		}

		public Participant setIsOwner() {
			this.isOwner = true;
			return this;
		}

		public boolean isOwner() {
			return this.isOwner;
		}

		public UUID getUUID() {
			return this.uuid;
		}

		public String getUsername() {
			return this.username;
		}
	}
}