package online.kingdomkeys.kingdomkeys.capability;

import java.util.*;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.lib.Struggle.Participant;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class WorldCapabilities implements IWorldCapabilities {

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag storage = new CompoundTag();
		storage.putInt("heartless", this.getHeartlessSpawnLevel());

		ListTag parties = new ListTag();
		List<String> partyNames = new ArrayList<>();
		int dupeCount = 0;
		for (Party party : this.getParties()) {
			if (partyNames.contains(party.getName())) {
				dupeCount++;
			} else {
				partyNames.add(party.getName());
				parties.add(party.write());
			}
		}
		if (dupeCount > 0) {
			KingdomKeys.LOGGER.warn("Discarded {} duplicate parties while writing", dupeCount);
		}
		storage.put("parties", parties);

		ListTag portals = new ListTag();
		for (Entry<UUID, PortalData> entry : this.getPortals().entrySet()) {
			portals.add(entry.getValue().write());
		}
		storage.put("portals", portals);
		
		ListTag struggles = new ListTag();
		List<String> struggleNames = new ArrayList<>();
		int struggleDupeCount = 0;
		for (Struggle struggle : this.getStruggles()) {
			if (struggleNames.contains(struggle.getName())) {
				struggleDupeCount++;
			} else {
				struggleNames.add(struggle.getName());
				struggles.add(struggle.write());
			}
		}
		if (struggleDupeCount > 0) {
			KingdomKeys.LOGGER.warn("Discarded {} duplicate struggles while writing", struggleDupeCount);
		}
		storage.put("struggles", struggles);

		return storage;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.setHeartlessSpawnLevel(nbt.getInt("heartless"));

		List<Party> partiesList = this.getParties();
		List<String> partyNames = new ArrayList<>();
		int dupeCount = 0;
		ListTag parties = nbt.getList("parties", Tag.TAG_COMPOUND);

		for (int i = 0; i < parties.size(); i++) {
			CompoundTag partyNBT = parties.getCompound(i);
			Party party = new Party();
			party.read(partyNBT);
			if (partyNames.contains(party.getName())) {
				dupeCount++;
			} else {
				partyNames.add(party.getName());
				partiesList.add(party);
			}
		}
		if (dupeCount > 0) {
			KingdomKeys.LOGGER.warn("Discarded {} duplicate parties while reading", dupeCount);
		}
		this.setParties(partiesList);

		Map<UUID, PortalData> portalList = this.getPortals();
		ListTag portals = nbt.getList("portals", Tag.TAG_COMPOUND);

		for (int i = 0; i < portals.size(); i++) {
			CompoundTag portalNBT = portals.getCompound(i);
			PortalData portal = new PortalData(null, null, 0, 0, 0, null, null);
			portal.read(portalNBT);
			portalList.put(portal.getUUID(), portal);
		}
		this.setPortals(portalList);
		
		List<Struggle> strugglesList = this.getStruggles();
		List<String> struggleNames = new ArrayList<>();
		int struggleDupeCount = 0;
		ListTag struggles = nbt.getList("struggles", Tag.TAG_COMPOUND);

		for (int i = 0; i < struggles.size(); i++) {
			CompoundTag struggleNBT = struggles.getCompound(i);
			Struggle struggle = new Struggle();
			struggle.read(struggleNBT);
			if (struggleNames.contains(struggle.getName())) {
				struggleDupeCount++;
			} else {
				struggleNames.add(struggle.getName());
				strugglesList.add(struggle);
			}
		}
		if (struggleDupeCount > 0) {
			KingdomKeys.LOGGER.warn("Discarded {} duplicate struggles while reading", struggleDupeCount);
		}
		this.setParties(partiesList);
	}
	
	private List<Party> parties = new ArrayList<Party>();
	private List<Struggle> struggles = new ArrayList<Struggle>();

	int heartlessSpawnLevel = 0;
	Map<UUID, PortalData> portals = new HashMap<UUID, PortalData>();

	@Override
	public Map<UUID, PortalData> getPortals() {
		return portals;
	}
	
	@Override
	public void setPortals(Map<UUID, PortalData> portals) {
		this.portals = portals;
	}

	@Override
	public void addPortal(UUID uuid, PortalData data) {
		this.portals.put(uuid, data);
	}
	
	@Override
	public boolean removePortal(UUID id) {
        if (portals.containsKey(id)) {
        	portals.remove(id);
            return true;
        } else {
            return false;
        }
    }
	
	@Override
	public PortalData getPortalFromUUID(UUID uuid) {
		return portals.getOrDefault(uuid, null);
	}
	
	@Override
	public UUID getOwnerIDFromUUID(UUID portalUUID) {
		for(Entry<UUID, PortalData> p : portals.entrySet()) {
			if(p.getValue().getUUID().equals(portalUUID)) {
				return p.getValue().getOwnerID();
			}
		}
		return null;
	}
	
	@Override
	public List<UUID> getAllPortalsFromOwnerID(UUID ownerID) {
		List<UUID> portals = new ArrayList<UUID>();

		for(Entry<UUID, PortalData> p : getPortals().entrySet()) {
        	if(p.getValue().getOwnerID().equals(ownerID)) {
        		portals.add(p.getValue().getUUID());
        	}
        }
		return portals;
	}

	
	@Override
	public int getHeartlessSpawnLevel() {
		return heartlessSpawnLevel;
	}

	@Override
	public void setHeartlessSpawnLevel(int level) {
		heartlessSpawnLevel = level;		
	}

	@Override
	public void setParties(List<Party> list) {
		parties	= list;	
	}

	@Override
	public List<Party> getParties() {
		return parties;
	}

	@Nullable
	@Override
	public Party getPartyFromMember(UUID memId) {
		for (Party party : this.parties) {
			for (Member member : party.getMembers()) {
				if (member.getUUID().equals(memId))
					return party;
			}
		}

		return null;
	}
	
	@Nullable
	@Override
	public Party getPartyFromName(String name) {
		for (Party party : this.parties) {
			if(party.getName().equalsIgnoreCase(name)) {
				return party;
			}
		}

		return null;
	}

	@Override
	public void removeParty(Party party) {
		String key = Utils.getResourceName(party.getName());
		int pos = -1;
		for(int i = 0; i < parties.size();i++) {
			//System.out.println(parties.get(i).getName()+":"+key);
			if(Utils.getResourceName(parties.get(i).getName()).equalsIgnoreCase(key)) {
				pos = i;
				break;
			}
		}
		
		if(pos>-1)
			parties.remove(pos);
	}

	@Override
	public void addParty(Party party) {
		String key = Utils.getResourceName(party.getName());
		boolean found = false;
		for(Party p : parties) {
			if(Utils.getResourceName(p.getName()).equalsIgnoreCase(key)) {
				found = true;
			}
		}
		if (!found) {
			this.parties.add(party);
		}
	}

	@Override
	public void removeLeaderMember(Party party, LivingEntity entity) {
		party.removeMember(entity.getUUID());
	}

	@Override
	public void addPartyMember(Party party, LivingEntity entity) {
		party.addMember(entity);
	}

	@Override
	public void setStruggles(List<Struggle> list) {
		this.struggles = list;
	}

	@Override
	public List<Struggle> getStruggles() {
		return struggles;
	}

	@Override
	public Struggle getStruggleFromParticipant(UUID memId) {
		for (Struggle struggle : this.struggles) {
			for (Participant participant : struggle.getParticipants()) {
				if (participant.getUUID().equals(memId))
					return struggle;
			}
		}

		return null;
	}

	@Override
	public void addStruggleParticipant(Struggle struggle, LivingEntity entity) {
		struggle.addParticipant(entity);
		
	}

	@Override
	public void addStruggle(Struggle struggle) {
		String key = Utils.getResourceName(struggle.getName());
		boolean found = false;
		for(Struggle p : struggles) {
			if(Utils.getResourceName(p.getName()).equalsIgnoreCase(key)) {
				found = true;
			}
		}
		if (!found) {
			this.struggles.add(struggle);
		}		
	}

	@Override
	public void removeStruggle(Struggle struggle) {
		String key = Utils.getResourceName(struggle.getName());
		int pos = -1;
		for(int i = 0; i < struggles.size();i++) {
			//System.out.println(parties.get(i).getName()+":"+key);
			if(Utils.getResourceName(struggles.get(i).getName()).equalsIgnoreCase(key)) {
				pos = i;
				break;
			}
		}
		
		if(pos>-1)
			struggles.remove(pos);		
	}

	@Override
	public Struggle getStruggleFromName(String name) {
		for (Struggle struggle : this.struggles) {
			if(struggle.getName().equalsIgnoreCase(name)) {
				return struggle;
			}
		}

		return null;
	}
	
	@Override
	public Struggle getStruggleFromBlockPos(BlockPos boardPos) {
		for (Struggle struggle : this.struggles) {
			if(struggle.getPos().equals(boardPos)) {
				return struggle;
			}
		}
		return null;
	}
	
	@Override
	public void read(CompoundTag nbt) {
		this.parties.clear();
		ListTag parties = nbt.getList("parties", Tag.TAG_COMPOUND);
		for (int i = 0; i < parties.size(); i++) {
			CompoundTag partyNBT = parties.getCompound(i);
			Party party = new Party();
			party.read(partyNBT);
			addParty(party);
		}

		this.heartlessSpawnLevel = nbt.getInt("heartless");

		this.portals.clear();
		ListTag portals = nbt.getList("portals", Tag.TAG_COMPOUND);

		for (int i = 0; i < portals.size(); i++) {
			CompoundTag portalNBT = portals.getCompound(i);
			PortalData portal = new PortalData(null, null, 0, 0, 0, null, null);
			portal.read(portalNBT);
			this.portals.put(portal.getUUID(), portal);
		}
		
		this.struggles.clear();
		ListTag struggles = nbt.getList("struggles", Tag.TAG_COMPOUND);
		for (int i = 0; i < struggles.size(); i++) {
			CompoundTag struggleNBT = struggles.getCompound(i);
			Struggle struggle = new Struggle();
			struggle.read(struggleNBT);
			addStruggle(struggle);
		}
	}
	
	@Override
	public CompoundTag write(CompoundTag nbt) {
		ListTag parties = new ListTag();
		Set<String> names = new HashSet<String>();
		for (Party party : this.parties) {
			if(!names.contains(party.getName())) {
				parties.add(party.write());
				names.add(party.getName());
			}
		}

		nbt.put("parties", parties);

		nbt.putInt("heartless", this.heartlessSpawnLevel);

		ListTag portals = new ListTag();
		for (Entry<UUID, PortalData> entry : getPortals().entrySet()) {
			portals.add(entry.getValue().write());
		}
		nbt.put("portals", portals);
		
		
		ListTag struggles = new ListTag();
		Set<String> struggleNames = new HashSet<String>();
		for (Struggle struggle : this.struggles) {
			if(!struggleNames.contains(struggle.getName())) {
				struggles.add(struggle.write());
				struggleNames.add(struggle.getName());
			}
		}

		nbt.put("struggles", struggles);

		return nbt;
	}

	
	
}