package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Struggle {

	public static final byte PARTICIPANTS_LIMIT = 8;

	/**
	 * DUEL: classic 1v1 at c1/c2, same match can be re-readied and fought again indefinitely.
	 * TOURNAMENT: single-elimination bracket among every participant (byes if the count is odd),
	 *             fought as a sequence of 1v1s at c1/c2 until only one player ("the champion") remains.
	 * FFA: every ready participant fights at once, scattered across the c1/c2 area.
	 */
	public enum Mode {
		DUEL, TOURNAMENT, FFA
	}

	private String name;
	private final List<Participant> participants = new ArrayList<>();
	//private boolean priv;
	private byte size;
	private int damageMult;
	private boolean inProgress;
	private Mode mode = Mode.DUEL;
	/** Seconds left in the current round, for the HUD countdown. -1 while no round is running. */
	private int roundSecondsLeft = -1;
	/** Tournament-only: remaining UUIDs in fighting order (winners go to the back, losers drop out). */
	private final List<UUID> tournamentQueue = new ArrayList<>();
	/** Who is actually fighting in the currently running match (subset of participants). For the HUD. */
	private final List<UUID> activeCombatantIds = new ArrayList<>();
	public BlockPos blockPos, c1,c2;

	public Struggle() {

	}

	public Struggle(CompoundTag tag) {
		read(tag);
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

	public void setInProgress(boolean inProgress) {
		this.inProgress = inProgress;
	}

	public boolean isInProgress() {
		return this.inProgress;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public Mode getMode() {
		return this.mode;
	}

	public void setRoundSecondsLeft(int seconds) {
		this.roundSecondsLeft = seconds;
	}

	public int getRoundSecondsLeft() {
		return this.roundSecondsLeft;
	}

	public List<UUID> getTournamentQueue() {
		return this.tournamentQueue;
	}

	public List<UUID> getActiveCombatantIds() {
		return this.activeCombatantIds;
	}

	/** True once the owner has set two different corners for the arena. */
	public boolean isConfigured() {
		return this.c1 != null && this.c2 != null && !this.c1.equals(this.c2);
	}

	/** The Struggle weapon matching a player's Station of Awakening choice (sword/wand/hammer). */
	public static Item weaponFor(SoAState chosen) {
		if (chosen == SoAState.MYSTIC) return ModItems.struggleWand.get();
		if (chosen == SoAState.GUARDIAN) return ModItems.struggleHammer.get();
		return ModItems.struggleSword.get(); // WARRIOR, and fallback for anyone without a choice yet
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
		this.tournamentQueue.remove(id);
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

	/**
	 * Clears the roster/scores/queue but keeps the arena (c1/c2), name, size, damage multiplier and
	 * mode intact, so the board can be readied up again without redoing the setup. Used after a
	 * tournament crowns a champion.
	 */
	public void resetForNewMatch() {
		this.participants.clear();
		this.tournamentQueue.clear();
		this.activeCombatantIds.clear();
		this.inProgress = false;
		this.roundSecondsLeft = -1;
	}

	public CompoundTag write() {
		CompoundTag struggleNBT = new CompoundTag();
		struggleNBT.putString("name", this.getName());
		//partyNBT.putBoolean("private", this.priv);
		struggleNBT.putByte("size", this.size);
		struggleNBT.putInt("dmg_mult", this.damageMult);
		struggleNBT.putBoolean("in_progress", this.inProgress);
		struggleNBT.putString("mode", this.mode.name());
		struggleNBT.putInt("round_seconds_left", this.roundSecondsLeft);
		struggleNBT.putIntArray("posArray", new int[] {this.blockPos.getX(),this.blockPos.getY(),this.blockPos.getZ()});
		struggleNBT.putIntArray("c1", new int[] {this.c1.getX(),this.c1.getY(),this.c1.getZ()});
		struggleNBT.putIntArray("c2", new int[] {this.c2.getX(),this.c2.getY(),this.c2.getZ()});

		ListTag participants = new ListTag();
		for (Struggle.Participant participant : this.getParticipants()) {
			CompoundTag participantNBT = new CompoundTag();
			participantNBT.putUUID("id", participant.getUUID());
			participantNBT.putString("username", participant.getUsername());
			participantNBT.putBoolean("isOwner", participant.isOwner());
			participantNBT.putBoolean("ready", participant.isReady());
			participantNBT.putInt("score", participant.getScore());
			participants.add(participantNBT);
		}
		struggleNBT.put("participants", participants);

		ListTag queue = new ListTag();
		for (UUID uuid : this.tournamentQueue) {
			CompoundTag entry = new CompoundTag();
			entry.putUUID("id", uuid);
			queue.add(entry);
		}
		struggleNBT.put("tournament_queue", queue);

		ListTag activeCombatants = new ListTag();
		for (UUID uuid : this.activeCombatantIds) {
			CompoundTag entry = new CompoundTag();
			entry.putUUID("id", uuid);
			activeCombatants.add(entry);
		}
		struggleNBT.put("active_combatants", activeCombatants);

		return struggleNBT;
	}

	public void read(CompoundTag nbt) {
		this.setName(nbt.getString("name"));
		//this.setPriv(nbt.getBoolean("private"));
		this.setSize(nbt.getByte("size"));
		this.setDamageMult(nbt.getInt("dmg_mult"));
		this.setInProgress(nbt.getBoolean("in_progress"));
		try {
			this.setMode(nbt.contains("mode") ? Mode.valueOf(nbt.getString("mode")) : Mode.DUEL);
		} catch (IllegalArgumentException e) {
			this.setMode(Mode.DUEL);
		}
		this.setRoundSecondsLeft(nbt.contains("round_seconds_left") ? nbt.getInt("round_seconds_left") : -1);
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
			participant.setReady(participantNBT.getBoolean("ready"));
			participant.setScore(participantNBT.contains("score") ? participantNBT.getInt("score") : 100);
		}

		this.tournamentQueue.clear();
		ListTag queue = nbt.getList("tournament_queue", Tag.TAG_COMPOUND);
		for (int j = 0; j < queue.size(); j++) {
			this.tournamentQueue.add(queue.getCompound(j).getUUID("id"));
		}

		this.activeCombatantIds.clear();
		ListTag activeCombatants = nbt.getList("active_combatants", Tag.TAG_COMPOUND);
		for (int j = 0; j < activeCombatants.size(); j++) {
			this.activeCombatantIds.add(activeCombatants.getCompound(j).getUUID("id"));
		}
	}

	public static class Participant {
		private final UUID uuid;
		private final String username;
		private boolean isOwner;
		private boolean ready;
		private int score = 100;

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

		public void setReady(boolean ready) {
			this.ready = ready;
		}

		public boolean isReady() {
			return this.ready;
		}

		public void setScore(int score) {
			this.score = Math.max(0, score);
		}

		public int getScore() {
			return this.score;
		}
	}

	public static final StreamCodec<FriendlyByteBuf, Struggle> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.COMPOUND_TAG,
			Struggle::write,
			Struggle::new
	);
}
