package online.kingdomkeys.kingdomkeys.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;

public class Party {
	
	public static final byte PARTY_LIMIT = 4;

	private String name;
	private List<Member> members = new ArrayList<Member>();
	private boolean priv;
	private byte size;

	public Party() {
		
	}

	/*public Party(String name, LivingEntity entity) {
		this(name, entity.getUniqueID(), entity.getDisplayName().getFormattedText());
	}*/

	public Party(String name, UUID leaderId, String username, boolean priv, byte size) {
		this.name = name;
		this.addMember(leaderId, username).setIsLeader();
		this.priv = priv;
		this.size = size;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
	public void setPriv(boolean priv) {
		this.priv = priv;
	}

	public boolean getPriv() {
		return this.priv;
	}
	
	public void setSize(byte size) {
		this.size = size;
	}
	
	public byte getSize() {
		return this.size;
	}	
	
	public Member addMember(LivingEntity entity) {
		return this.addMember(entity.getUniqueID(), entity.getDisplayName().getFormattedText());
	}

	public Member addMember(UUID uuid, String username) {
		Member member = new Member(uuid, username);
		this.members.add(member);
		return member;
	}

	public void removeMember(UUID id) {
		Member member = this.getMember(id);
		if (member.isLeader())
			this.members.removeAll(this.members);
		else
			this.members.remove(member);
	}

	public Member getMember(UUID id) {
		return this.members.stream().filter(member -> member.getUUID().equals(id)).findFirst().orElse(null);
	}

	public boolean hasMember(UUID id) {
		return this.getMember(id) != null;
	}

	@Nullable
	public Member getLeader() {
		return this.members.stream().filter(member -> member.isLeader()).findFirst().orElse(null);
	}

	public List<Member> getMembers() {
		return this.members;
	}

	public CompoundNBT write() {
		CompoundNBT partyNBT = new CompoundNBT();
		partyNBT.putString("name", this.getName());
		partyNBT.putBoolean("private", this.priv);
		partyNBT.putByte("size", this.size);
		
		ListNBT members = new ListNBT();
		for (Party.Member member : this.getMembers()) {
			CompoundNBT memberNBT = new CompoundNBT();
			memberNBT.putUniqueId("id", member.getUUID());
			memberNBT.putString("username", member.getUsername());
			memberNBT.putBoolean("isLeader", member.isLeader());
			members.add(memberNBT);
		}
		partyNBT.put("members", members);

		return partyNBT;
	}

	public void read(CompoundNBT nbt) {
		this.setName(nbt.getString("name"));
		this.setPriv(nbt.getBoolean("private"));
		this.setSize(nbt.getByte("size"));

		ListNBT members = nbt.getList("members", Constants.NBT.TAG_COMPOUND);
		for (int j = 0; j < members.size(); j++) {
			CompoundNBT memberNBT = members.getCompound(j);
			Party.Member member = this.addMember(memberNBT.getUniqueId("id"), memberNBT.getString("username"));
			if (memberNBT.getBoolean("isLeader"))
				member.setIsLeader();
		}

	}

	public static class Member {
		private UUID uuid;
		private String username;
		private boolean isLeader;

		public Member(LivingEntity entity) {
			this(entity.getUniqueID(), entity.getDisplayName().getFormattedText());
		}

		public Member(UUID uuid, String username) {
			this.uuid = uuid;
			this.username = username;
		}

		public Member setIsLeader() {
			this.isLeader = true;
			return this;
		}

		public boolean isLeader() {
			return this.isLeader;
		}

		public UUID getUUID() {
			return this.uuid;
		}

		public String getUsername() {
			return this.username;
		}
	}
}