package online.kingdomkeys.kingdomkeys.capability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.Utils;

public class ExtendedWorldData extends WorldSavedData {
	private static final String NAME = "kingdomkeys";

	private HashMap<String, Party> parties = new HashMap<String, Party>();

	private boolean heartlessSpawn = false;

	public static Map<World, ExtendedWorldData> loadedExtWorlds = new HashMap<>();

	public ExtendedWorldData() {
		super(NAME);
	}

	public ExtendedWorldData(String identifier) {
		super(identifier);
	}

	public static ExtendedWorldData get(World world) {
		if (world == null)
			return null;

		ExtendedWorldData worldExt;

		if (loadedExtWorlds.containsKey(world)) {
			worldExt = loadedExtWorlds.get(world);
			return worldExt;
		}

		if (world instanceof ServerWorld) {
			ServerWorld serverWorld = (ServerWorld) world;
			world.increaseMaxEntityRadius(50);
			ExtendedWorldData worldSavedData = serverWorld.getSavedData().get(ExtendedWorldData::new, NAME);
			if (worldSavedData != null) {
				worldExt = worldSavedData;
			} else {
				worldExt = new ExtendedWorldData();
				serverWorld.getSavedData().set(worldExt);
			}
		} else {
			worldExt = new ExtendedWorldData();
		}

		loadedExtWorlds.put(world, worldExt);
		return worldExt;

	}

	@Override
	public void read(CompoundNBT nbt) {
		this.parties.clear();
		ListNBT parties = nbt.getList("parties", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < parties.size(); i++) {
			CompoundNBT partyNBT = parties.getCompound(i);
			Party party = new Party();
			party.read(partyNBT);
			this.parties.put(Utils.getResourceName(party.getName()), party);
		}
		
		this.heartlessSpawn = nbt.getBoolean("heartless");  
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		ListNBT parties = new ListNBT();
		for (Party party : this.parties.values()) {
			parties.add(party.write());
		}
		nbt.put("parties", parties);
		
		nbt.putBoolean("heartless", this.heartlessSpawn);
		return nbt;
	}

	public boolean getHeartlessSpawn() {
		return this.heartlessSpawn;
	}

	public void setHeartlessSpawn(boolean b) {
		this.heartlessSpawn = b;
		this.markDirty();
	}
	
	public List<Party> getParties() {
		return new ArrayList(this.parties.values());
	}

	@Nullable
	public Party getPartyFromMember(UUID memId) {
		for (Party party : this.parties.values()) {
			for (Member member : party.getMembers()) {
				if (member.getUUID().equals(memId))
					return party;
			}
		}

		return null;
	}

	@Nullable
	public Party getPartyFromLeader(UUID leaderId) {
		return this.parties.values().stream().filter(party -> party.getLeader() != null && party.getLeader().getUUID() == leaderId).findFirst().orElse(null);
	}

	public void removeParty(Party party) {
		String key = Utils.getResourceName(party.getName());
		if (this.parties.containsKey(key))
			this.parties.remove(key);
		this.markDirty();
	}

	public void addParty(Party party) {
		String key = Utils.getResourceName(party.getName());
		if (!this.parties.containsKey(key))
			this.parties.put(key, party);
		this.markDirty();
	}

	public void removeLeaderMember(Party party, LivingEntity entity) {
		party.removeMember(entity.getUniqueID());
		this.markDirty();
	}

	public void addPartyMember(Party party, LivingEntity entity) {
		party.addMember(entity);
		this.markDirty();
	}
}