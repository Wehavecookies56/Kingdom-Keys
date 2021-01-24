package online.kingdomkeys.kingdomkeys.capability;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.PortalData;

public interface IWorldCapabilities {
	int getHeartlessSpawnLevel();
	void setHeartlessSpawnLevel(int l);
	
	void setParties(List<Party> list);
	List<Party> getParties();
	Party getPartyFromMember(UUID memId);
	void addPartyMember(Party party, LivingEntity entity);
	void removeLeaderMember(Party party, LivingEntity entity);
	void addParty(Party party);
	void removeParty(Party party);
	Party getPartyFromName(String name);
	Party getPartyFromLeader(UUID leaderId);
	

	
	void read(CompoundNBT nbt);
	CompoundNBT write(CompoundNBT nbt);
	
	Map<UUID, PortalData> getPortals();
	void setPortals(Map<UUID, PortalData> portals);
	void addPortal(UUID uuid, PortalData data);
	boolean removePortal(UUID id);
	PortalData getPortalFromUUID(UUID uuid);
	UUID getOwnerIDFromUUID(UUID portalID);
	List<UUID> getAllPortalsFromOwnerID(UUID uniqueID);
}
