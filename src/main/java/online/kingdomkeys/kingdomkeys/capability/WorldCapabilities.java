package online.kingdomkeys.kingdomkeys.capability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class WorldCapabilities implements IWorldCapabilities {

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag storage = new CompoundTag();
		storage.putInt("heartless", this.getHeartlessSpawnLevel());

		ListTag parties = new ListTag();
		for (Party party : this.getParties()) {
			parties.add(party.write());
		}
		storage.put("parties", parties);

		ListTag portals = new ListTag();
		for (Entry<UUID, PortalData> entry : this.getPortals().entrySet()) {
			portals.add(entry.getValue().write());
		}
		storage.put("portals", portals);

		return storage;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		CompoundTag storage = (CompoundTag) nbt;
		this.setHeartlessSpawnLevel(storage.getInt("heartless"));

		List<Party> partiesList = this.getParties();
		ListTag parties = storage.getList("parties", Tag.TAG_COMPOUND);

		for (int i = 0; i < parties.size(); i++) {
			CompoundTag partyNBT = parties.getCompound(i);
			Party party = new Party();
			party.read(partyNBT);
			partiesList.add(party);
		}
		this.setParties(partiesList);

		Map<UUID, PortalData> portalList = this.getPortals();
		ListTag portals = storage.getList("portals", Tag.TAG_COMPOUND);

		for (int i = 0; i < portals.size(); i++) {
			CompoundTag portalNBT = portals.getCompound(i);
			PortalData portal = new PortalData(null, null, 0, 0, 0, null, null);
			portal.read(portalNBT);
			portalList.put(portal.getUUID(), portal);
		}
		this.setPortals(portalList);
	}
	
	private List<Party> parties = new ArrayList<Party>();
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
	public Party getPartyFromLeader(UUID leaderId) {
		return this.parties.stream().filter(party -> party.getLeader() != null && party.getLeader().getUUID() == leaderId).findFirst().orElse(null);
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
		if (!found)
			this.parties.add(party);
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
	}
	
	@Override
	public CompoundTag write(CompoundTag nbt) {
		ListTag parties = new ListTag();
		for (Party party : this.parties) {
			parties.add(party.write());
		}
		nbt.put("parties", parties);
		
		nbt.putInt("heartless", this.heartlessSpawnLevel);
		
		ListTag portals = new ListTag();
		for (Entry<UUID, PortalData> entry : getPortals().entrySet()) {
			portals.add(entry.getValue().write());
		}
		nbt.put("portals", portals);
		
		return nbt;
	}

	

}