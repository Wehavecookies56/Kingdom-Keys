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
	private int damageMult;
	public BlockPos blockPos, c1,c2;

	public Struggle() {

	}

	public Struggle(BlockPos blockPos, String name, UUID leaderId, String username, boolean priv, byte size) {
		this.name = name;
		this.addParticipant(leaderId, username).setIsOwner();
		//this.priv = priv;
		this.size = size;
		this.damageMult = 100;
		this.blockPos = blockPos;
		this.c1 = new BlockPos(0,0,0);
		this.c2 = new BlockPos(0,0,0);
	}

	public void setPos(BlockPos pos) {
		this.blockPos = pos;
	}

	public BlockPos getPos() {
		return this.blockPos;
	}
	
	public void setC1(BlockPos pos) {
		this.c1 = pos;
	}

	public BlockPos getC1() {
		return this.c1;
	}
	
	public void setC2(BlockPos pos) {
		this.c2 = pos;
	}

	public BlockPos getC2() {
		return this.c2;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}	

	public void setSize(byte size) {
		this.size = size;
	}

	public byte getSize() {
		return this.size;
	}

	public void setDamageMult(int val) {
		this.damageMult = val;
	}

	public int getDamageMult() {
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
		struggleNBT.putInt("dmg_mult", this.damageMult);
		struggleNBT.putIntArray("posArray", new int[] {this.blockPos.getX(),this.blockPos.getY(),this.blockPos.getZ()});
		struggleNBT.putIntArray("c1", new int[] {this.c1.getX(),this.c1.getY(),this.c1.getZ()});
		struggleNBT.putIntArray("c2", new int[] {this.c2.getX(),this.c2.getY(),this.c2.getZ()});

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
		this.setDamageMult(nbt.getInt("dmg_mult"));
		int[] posArray = nbt.getIntArray("posArray");
		this.setPos(new BlockPos(posArray[0],posArray[1],posArray[2]));
		
		int[] c1Array = nbt.getIntArray("c1");
		this.setC1(new BlockPos(c1Array[0],c1Array[1],c1Array[2]));
		
		int[] c2Array = nbt.getIntArray("c2");
		this.setC2(new BlockPos(c2Array[0],c2Array[1],c2Array[2]));
		
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