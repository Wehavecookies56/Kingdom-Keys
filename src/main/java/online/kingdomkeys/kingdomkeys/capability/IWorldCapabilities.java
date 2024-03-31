package online.kingdomkeys.kingdomkeys.capability;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.lib.Struggle;

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
	
	void read(CompoundTag nbt);
	CompoundTag write(CompoundTag nbt);
		
	Map<UUID, PortalData> getPortals();
	void setPortals(Map<UUID, PortalData> portals);
	void addPortal(UUID uuid, PortalData data);
	boolean removePortal(UUID id);
	PortalData getPortalFromUUID(UUID uuid);
	UUID getOwnerIDFromUUID(UUID portalID);
	List<UUID> getAllPortalsFromOwnerID(UUID ownerID);
	
	void setStruggles(List<Struggle> list);
	List<Struggle> getStruggles();
	Struggle getStruggleFromParticipant(UUID memId);
	void addStruggleParticipant(Struggle party, LivingEntity entity);
	//void removeLeaderMember(Struggle party, LivingEntity entity);
	void addStruggle(Struggle party);
	void removeStruggle(Struggle party);
	Struggle getStruggleFromName(String name);
	//Struggle getStruggleFromLeader(UUID leaderId);
	Struggle getStruggleFromBlockPos(BlockPos boardPos);
}
