package online.kingdomkeys.kingdomkeys.capability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.Constants;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class WorldCapabilities implements IWorldCapabilities {

	public static class Storage implements IStorage<IWorldCapabilities> {
		@Override
		public INBT writeNBT(Capability<IWorldCapabilities> capability, IWorldCapabilities instance, Direction side) {
			CompoundNBT storage = new CompoundNBT();
			storage.putInt("heartless", instance.getHeartlessSpawnLevel());

			ListNBT parties = new ListNBT();
			for (Party party : instance.getParties()) {
				parties.add(party.write());
			}
			storage.put("parties", parties);
			
			ListNBT portals = new ListNBT();
			for (Entry<UUID, PortalData> entry : instance.getPortals().entrySet()) {
				portals.add(entry.getValue().write());
			}
			storage.put("portals", portals);
			
			return storage;
		}

		@Override
		public void readNBT(Capability<IWorldCapabilities> capability, IWorldCapabilities instance, Direction side, INBT nbt) {
			CompoundNBT storage = (CompoundNBT) nbt;
			instance.setHeartlessSpawnLevel(storage.getInt("heartless"));
			
			List<Party> partiesList = instance.getParties();
			ListNBT parties = storage.getList("parties", Constants.NBT.TAG_COMPOUND);

			for (int i = 0; i < parties.size(); i++) {
				CompoundNBT partyNBT = parties.getCompound(i);
				Party party = new Party();
				party.read(partyNBT);
				partiesList.add(party);
			}
			instance.setParties(partiesList);
			
			Map<UUID, PortalData> portalList = instance.getPortals();
			ListNBT portals = storage.getList("portals", Constants.NBT.TAG_COMPOUND);

			for (int i = 0; i < portals.size(); i++) {
				CompoundNBT portalNBT = portals.getCompound(i);
				PortalData portal = new PortalData(null, null, 0, 0, 0, 0, null);
				portal.read(portalNBT);
				portalList.put(portal.getUUID(), portal);
			}
			instance.setPortals(portalList);

		}
	}
	
	List<Party> parties = new ArrayList<Party>();
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
			System.out.println(parties.get(i).getName()+":"+key);
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
			if(p.getName().equalsIgnoreCase(key)) {
				found = true;
			}
		}
		if (!found)
			this.parties.add(party);
	}

	@Override
	public void removeLeaderMember(Party party, LivingEntity entity) {
		party.removeMember(entity.getUniqueID());
	}

	@Override
	public void addPartyMember(Party party, LivingEntity entity) {
		party.addMember(entity);
	}
		
	
	@Override
	public void read(CompoundNBT nbt) {
		this.parties.clear();
		ListNBT parties = nbt.getList("parties", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < parties.size(); i++) {
			CompoundNBT partyNBT = parties.getCompound(i);
			Party party = new Party();
			party.read(partyNBT);
			this.parties.add(party);
		}
		
		this.heartlessSpawnLevel = nbt.getInt("heartless");  
		
		this.portals.clear();
		ListNBT portals = nbt.getList("portals", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < portals.size(); i++) {
			CompoundNBT portalNBT = portals.getCompound(i);
			PortalData portal = new PortalData(null, null, 0, 0, 0, 0, null);
			portal.read(portalNBT);
			this.portals.put(portal.getUUID(), portal);
		}

	}
	
	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		ListNBT parties = new ListNBT();
		for (Party party : this.parties) {
			parties.add(party.write());
		}
		nbt.put("parties", parties);
		
		nbt.putInt("heartless", this.heartlessSpawnLevel);
		
		ListNBT portals = new ListNBT();
		for (Entry<UUID, PortalData> entry : getPortals().entrySet()) {
			portals.add(entry.getValue().write());
		}
		nbt.put("portals", portals);
		
		return nbt;
	}

	
}