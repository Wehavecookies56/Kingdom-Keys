package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.PortalData;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IWorldCapabilities extends INBTSerializable<CompoundTag> {
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
	
	void read(CompoundTag nbt);
	CompoundTag write(CompoundTag nbt);
		
	Map<UUID, PortalData> getPortals();
	void setPortals(Map<UUID, PortalData> portals);
	void addPortal(UUID uuid, PortalData data);
	boolean removePortal(UUID id);
	PortalData getPortalFromUUID(UUID uuid);
	UUID getOwnerIDFromUUID(UUID portalID);
	List<UUID> getAllPortalsFromOwnerID(UUID ownerID);
}
