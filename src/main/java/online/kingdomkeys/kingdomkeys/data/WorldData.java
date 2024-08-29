package online.kingdomkeys.kingdomkeys.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.saveddata.SavedData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.*;
import javax.annotation.Nullable;

public class WorldData extends SavedData {

    private WorldData() {}

    private static WorldData create() {
        return new WorldData();
    }

    private static WorldData clientCache = new WorldData();

    @Override
    public CompoundTag save(CompoundTag storage, HolderLookup.Provider pRegistries) {
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
        for (Map.Entry<UUID, PortalData> entry : this.getPortals().entrySet()) {
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

    private static WorldData load(CompoundTag nbt, HolderLookup.Provider provider) {
        WorldData data = WorldData.create();
        data.setHeartlessSpawnLevel(nbt.getInt("heartless"));

        List<Party> partiesList = data.getParties();
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
        data.setParties(partiesList);

        Map<UUID, PortalData> portalList = data.getPortals();
        ListTag portals = nbt.getList("portals", Tag.TAG_COMPOUND);

        for (int i = 0; i < portals.size(); i++) {
            CompoundTag portalNBT = portals.getCompound(i);
            PortalData portal = new PortalData(null, null, 0, 0, 0, null, null);
            portal.read(portalNBT);
            portalList.put(portal.getUUID(), portal);
        }
        data.setPortals(portalList);

        List<Struggle> strugglesList = data.getStruggles();
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
        data.setParties(partiesList);
        return data;
    }

    public static WorldData get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(new Factory<>(WorldData::create, WorldData::load), "kingdomkeys_data");
    }

    public static WorldData getClient() {
        return clientCache;
    }

    public static void setClientCache(WorldData data) {
        clientCache = data;
    }

    private List<Party> parties = new ArrayList<Party>();
    private List<Struggle> struggles = new ArrayList<Struggle>();

    int heartlessSpawnLevel = 0;
    Map<UUID, PortalData> portals = new HashMap<UUID, PortalData>();

    public Map<UUID, PortalData> getPortals() {
        return portals;
    }

    public void setPortals(Map<UUID, PortalData> portals) {
        this.portals = portals;
        setDirty();
    }

    public void addPortal(UUID uuid, PortalData data) {
        this.portals.put(uuid, data);
        setDirty();
    }

    public boolean removePortal(UUID id) {
        if (portals.containsKey(id)) {
            portals.remove(id);
            setDirty();
            return true;
        } else {
            return false;
        }
    }

    public PortalData getPortalFromUUID(UUID uuid) {
        return portals.getOrDefault(uuid, null);
    }

    public UUID getOwnerIDFromUUID(UUID portalUUID) {
        for(Map.Entry<UUID, PortalData> p : portals.entrySet()) {
            if(p.getValue().getUUID().equals(portalUUID)) {
                return p.getValue().getOwnerID();
            }
        }
        return null;
    }

    public List<UUID> getAllPortalsFromOwnerID(UUID ownerID) {
        List<UUID> portals = new ArrayList<UUID>();

        for(Map.Entry<UUID, PortalData> p : getPortals().entrySet()) {
            if(p.getValue().getOwnerID().equals(ownerID)) {
                portals.add(p.getValue().getUUID());
            }
        }
        return portals;
    }


    public int getHeartlessSpawnLevel() {
        return heartlessSpawnLevel;
    }

    public void setHeartlessSpawnLevel(int level) {
        heartlessSpawnLevel = level;
        setDirty();
    }

    public void setParties(List<Party> list) {
        parties	= list;
        setDirty();
    }

    public List<Party> getParties() {
        return parties;
    }

    @Nullable
    public Party getPartyFromMember(UUID memId) {
        for (Party party : this.parties) {
            for (Party.Member member : party.getMembers()) {
                if (member.getUUID().equals(memId))
                    return party;
            }
        }

        return null;
    }

    @Nullable
    public Party getPartyFromName(String name) {
        for (Party party : this.parties) {
            if(party.getName().equalsIgnoreCase(name)) {
                return party;
            }
        }

        return null;
    }

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
        setDirty();
    }

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
        setDirty();
    }

    public void removeLeaderMember(Party party, LivingEntity entity) {
        party.removeMember(entity.getUUID());
        setDirty();
    }

    public void addPartyMember(Party party, LivingEntity entity) {
        party.addMember(entity);
        setDirty();
    }

    public void setStruggles(List<Struggle> list) {
        this.struggles = list;
        setDirty();
    }

    public List<Struggle> getStruggles() {
        return struggles;
    }

    public Struggle getStruggleFromParticipant(UUID memId) {
        for (Struggle struggle : this.struggles) {
            for (Struggle.Participant participant : struggle.getParticipants()) {
                if (participant.getUUID().equals(memId))
                    return struggle;
            }
        }

        return null;
    }

    public void addStruggleParticipant(Struggle struggle, LivingEntity entity) {
        struggle.addParticipant(entity);
        setDirty();
    }

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
        setDirty();
    }

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
        setDirty();
    }

    public Struggle getStruggleFromName(String name) {
        for (Struggle struggle : this.struggles) {
            if(struggle.getName().equalsIgnoreCase(name)) {
                return struggle;
            }
        }

        return null;
    }

    public Struggle getStruggleFromBlockPos(BlockPos boardPos) {
        for (Struggle struggle : this.struggles) {
            if(struggle.getPos().equals(boardPos)) {
                return struggle;
            }
        }
        return null;
    }
}
