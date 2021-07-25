package online.kingdomkeys.kingdomkeys.capability;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.PortalData;

public class WorldCapabilitiesProvider implements ICapabilitySerializable<CompoundTag> {
	IWorldCapabilities instance = new WorldCapabilities();

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		return ModCapabilities.WORLD_CAPABILITIES.orEmpty(cap, LazyOptional.of(() -> instance));
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag storage = new CompoundTag();
		storage.putInt("heartless", instance.getHeartlessSpawnLevel());

		ListTag parties = new ListTag();
		for (Party party : instance.getParties()) {
			parties.add(party.write());
		}
		storage.put("parties", parties);
		
		ListTag portals = new ListTag();
		for (Entry<UUID, PortalData> entry : instance.getPortals().entrySet()) {
			portals.add(entry.getValue().write());
		}
		storage.put("portals", portals);
		
		return storage;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		CompoundTag storage = (CompoundTag) nbt;
		instance.setHeartlessSpawnLevel(storage.getInt("heartless"));
		
		List<Party> partiesList = instance.getParties();
		ListTag parties = storage.getList("parties", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < parties.size(); i++) {
			CompoundTag partyNBT = parties.getCompound(i);
			Party party = new Party();
			party.read(partyNBT);
			partiesList.add(party);
		}
		instance.setParties(partiesList);
		
		Map<UUID, PortalData> portalList = instance.getPortals();
		ListTag portals = storage.getList("portals", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < portals.size(); i++) {
			CompoundTag portalNBT = portals.getCompound(i);
			PortalData portal = new PortalData(null, null, 0, 0, 0, null, null);
			portal.read(portalNBT);
			portalList.put(portal.getUUID(), portal);
		}
		instance.setPortals(portalList);
	}

}
