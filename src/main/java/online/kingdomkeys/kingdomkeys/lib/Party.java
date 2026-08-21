package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Party {

	private String name;
	private final List<Member> members = new ArrayList<>();
	private boolean priv;
	private byte size;
	private boolean friendlyFire;

	public Party() {}

	public Party(CompoundTag tag) {
		read(tag);
	}

	public Party(String name, UUID leaderId, String username, boolean priv, byte size) {
		this.name = name;
		this.addMember(leaderId, username).setIsLeader(true);
		this.priv = priv;
		this.size = size;
		this.friendlyFire = false;
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

	public void setFriendlyFire(boolean ff) {
		this.friendlyFire = ff;
	}

	public boolean getFriendlyFire() {
		return this.friendlyFire;
	}

	public Member addMember(LivingEntity entity) {
		Member member = new Member(entity);
		this.members.add(member);
		return member;
	}

	public Member addMember(UUID uuid, String username) {
		Member member = new Member(uuid, username, true);
		this.members.add(member);
		return member;
	}

	public Member addMember(UUID uuid, String username, boolean player, int lvl, int hp, int mp) {
		Member member = new Member(uuid, username, player);
		member.setHP(hp);
		member.setMP(mp);
		member.setLevel(lvl);
		this.members.add(member);
		return member;
	}

	public void removeMember(UUID id) {
		Member member = this.getMember(id);

		if (member == null)
			return;

		if (member.isLeader())
			this.members.clear();
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
	public List<Member> getLeaders() {
		return this.members.stream().filter(member -> member.isLeader()).toList();
	}

	public List<Member> getMembers() {
		return this.members;
	}

	/** The members actually standing in this level right now, players or otherwise */
	public List<Member> getMembersOnline(Level level) {
		List<Member> onlineMembers = new ArrayList<>();
		for(Member member : this.members) {
			if(Utils.getPartyEntity(level, member.getUUID()) != null) {
				onlineMembers.add(member);
			}
		}
		return onlineMembers;
	}

	public int getMemberIndex(UUID memberUUID) {
		for (int i = 0; i < members.size(); i++) {
			if (members.get(i).getUUID().equals(memberUUID)) {
				return i;
			}
		}
		return -1;
	}

	public CompoundTag write() {
		CompoundTag partyNBT = new CompoundTag();
		partyNBT.putString("name", this.getName());
		partyNBT.putBoolean("private", this.priv);
		partyNBT.putByte("size", this.size);
		partyNBT.putBoolean("ff", this.friendlyFire);

		ListTag members = new ListTag();
		for (Party.Member member : this.getMembers()) {
			CompoundTag memberNBT = new CompoundTag();
			memberNBT.putUUID("id", member.getUUID());
			memberNBT.putString("username", member.getUsername());
			memberNBT.putBoolean("isMob", !member.isPlayer());
			memberNBT.putBoolean("isLeader", member.isLeader());
			memberNBT.putInt("level", member.getLevel());
			memberNBT.putInt("hp", member.getHP());
			memberNBT.putInt("mp", member.getMP());
			members.add(memberNBT);
		}
		partyNBT.put("members", members);

		return partyNBT;
	}

	public void read(CompoundTag nbt) {
		this.setName(nbt.getString("name"));
		this.setPriv(nbt.getBoolean("private"));
		this.setSize(nbt.getByte("size"));
		this.setFriendlyFire(nbt.getBoolean("ff"));

		ListTag members = nbt.getList("members", Tag.TAG_COMPOUND);
		for (int j = 0; j < members.size(); j++) {
			CompoundTag memberNBT = members.getCompound(j);
			Party.Member member = this.addMember(memberNBT.getUUID("id"), memberNBT.getString("username"), !memberNBT.getBoolean("isMob"), memberNBT.getInt("level"), memberNBT.getInt("hp"), memberNBT.getInt("mp"));
			member.setIsLeader(memberNBT.getBoolean("isLeader"));				
		}

	}

	public static class Member {
		private final UUID uuid;
		private final String username;
		private final boolean player;
		private boolean isLeader;
		private int level,hp,mp;

		public int getLevel() {
			return level;
		}

		public Member setLevel(int level) {
			this.level = level;
			return this;
		}

		public int getHP() {
			return hp;
		}

		public Member setHP(int hp) {
			this.hp = hp;
			return this;
		}

		public int getMP() {
			return mp;
		}

		public Member setMP(int mp) {
			this.mp = mp;
			return this;
		}

		public Member(LivingEntity entity) {
			// A mob carries its level inside its name. Taken off here so the menu can put it in the level field, where
			// it fits, instead of letting the name run off the end of the box
			this(entity.getUUID(), entity instanceof Player player ? player.getGameProfile().getName() : Utils.getBareName(entity), entity instanceof Player);

			setLevel(Utils.getEntityLevel(entity));
			setHP((int) entity.getMaxHealth());
		}

		public Member(Player entity) {
			this((LivingEntity) entity);
		}

		public Member(UUID uuid, String username) {
			this(uuid, username, true);
		}

		public Member(UUID uuid, String username, boolean player) {
			this.uuid = uuid;
			this.username = player ? username : Utils.stripLevel(username);
			this.player = player;
		}

		public boolean isPlayer() {
			return this.player;
		}

		public Member setIsLeader(boolean leader) {
			this.isLeader = leader;
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

	public static final StreamCodec<FriendlyByteBuf, Party> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.COMPOUND_TAG,
			Party::write,
			Party::new
	);
}