package online.kingdomkeys.kingdomkeys.capability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;

public class WorldCapabilities implements IWorldCapabilities {

	public static class Storage implements IStorage<IWorldCapabilities> {
		@Override
		public INBT writeNBT(Capability<IWorldCapabilities> capability, IWorldCapabilities instance, Direction side) {
			CompoundNBT props = new CompoundNBT();
			props.putBoolean("heartless", instance.getHeartlessSpawn());

			ListNBT parties = new ListNBT();
			for (Party party : instance.getParties()) {
				parties.add(party.write());
			}
			props.put("parties", parties);
			
			return props;
		}

		@Override
		public void readNBT(Capability<IWorldCapabilities> capability, IWorldCapabilities instance, Direction side, INBT nbt) {
			CompoundNBT properties = (CompoundNBT) nbt;
			instance.setHeartlessSpawn(properties.getBoolean("heartless"));
			
			List<Party> partiesList = instance.getParties();
			ListNBT parties = properties.getList("parties", Constants.NBT.TAG_COMPOUND);

			for (int i = 0; i < parties.size(); i++) {
				CompoundNBT partyNBT = parties.getCompound(i);
				Party party = new Party();
				party.read(partyNBT);
				partiesList.add(party);
			}
			instance.setParties(partiesList);
		}
	}
	
	List<Party> parties = new ArrayList<Party>();
	boolean heartlessSpawn = false;

	@Override
	public boolean getHeartlessSpawn() {
		return heartlessSpawn;
	}

	@Override
	public void setHeartlessSpawn(boolean b) {
		heartlessSpawn = b;		
	}

	@Override
	public void setParties(List<Party> list) {
		parties	= list;	
	}

	public List<Party> getPartiesMap() {
		return parties;
	}
	
	public List<Party> getParties() {
		return parties;
	}

	@Nullable
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
	public Party getPartyFromLeader(UUID leaderId) {
		return this.parties.stream().filter(party -> party.getLeader() != null && party.getLeader().getUUID() == leaderId).findFirst().orElse(null);
	}
	
	@Nullable
	public Party getPartyFromName(String name) {
		for (Party party : this.parties) {
			if(party.getName().equals(name)) {
				return party;
			}
		}

		return null;
	}

	public void removeParty(Party party) {
		String key = Utils.getResourceName(party.getName());
		for(Party p : parties) {
			if(p.getName().equals(key)) {
				parties.remove(p);
			}
		}
	}

	public void addParty(Party party) {
		String key = Utils.getResourceName(party.getName());
		boolean found = false;
		for(Party p : parties) {
			if(p.getName().equals(key)) {
				found = true;
			}
		}
		if (!found)
			this.parties.add(party);
	}

	public void removeLeaderMember(Party party, LivingEntity entity) {
		party.removeMember(entity.getUniqueID());
	}

	public void addPartyMember(Party party, LivingEntity entity) {
		party.addMember(entity);
	}

}