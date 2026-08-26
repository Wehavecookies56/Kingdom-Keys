package online.kingdomkeys.kingdomkeys.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.saveddata.SavedData;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.PartyAllyGoals;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.PortalData;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;
import java.util.*;

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

        ListTag savedCorners = new ListTag();
        for (Map.Entry<BlockPos, BlockPos[]> entry : this.savedStruggleCorners.entrySet()) {
            CompoundTag cornerNBT = new CompoundTag();
            BlockPos pos = entry.getKey();
            BlockPos c1 = entry.getValue()[0];
            BlockPos c2 = entry.getValue()[1];
            cornerNBT.putIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
            cornerNBT.putIntArray("c1", new int[]{c1.getX(), c1.getY(), c1.getZ()});
            cornerNBT.putIntArray("c2", new int[]{c2.getX(), c2.getY(), c2.getZ()});
            savedCorners.add(cornerNBT);
        }
        storage.put("saved_struggle_corners", savedCorners);

        storage.putBoolean("mini_co_generated", miniCOGenerated);
        storage.putInt("mini_co_y", miniCOY);
        return storage;
    }

    public static WorldData load(CompoundTag nbt, HolderLookup.Provider provider) {
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

        ListTag savedCorners = nbt.getList("saved_struggle_corners", Tag.TAG_COMPOUND);
        for (int i = 0; i < savedCorners.size(); i++) {
            CompoundTag cornerNBT = savedCorners.getCompound(i);
            int[] posArr = cornerNBT.getIntArray("pos");
            int[] c1Arr = cornerNBT.getIntArray("c1");
            int[] c2Arr = cornerNBT.getIntArray("c2");
            BlockPos pos = new BlockPos(posArr[0], posArr[1], posArr[2]);
            BlockPos c1 = new BlockPos(c1Arr[0], c1Arr[1], c1Arr[2]);
            BlockPos c2 = new BlockPos(c2Arr[0], c2Arr[1], c2Arr[2]);
            data.savedStruggleCorners.put(pos, new BlockPos[]{c1, c2});
        }

        data.miniCOGenerated = nbt.getBoolean("mini_co_generated");
        data.miniCOY = nbt.getInt("mini_co_y");
        return data;
    }

    public static WorldData get(MinecraftServer server) {
        if (server == null) return getClient();
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
    private final Map<BlockPos, BlockPos[]> savedStruggleCorners = new HashMap<>();

    int heartlessSpawnLevel = 0;
    Map<UUID, PortalData> portals = new HashMap<UUID, PortalData>();


    private boolean miniCOGenerated = false;
    private int miniCOY = Integer.MIN_VALUE;

    public boolean isMiniCOGenerated() {
        return miniCOGenerated;
    }

    public void setMiniCOGenerated(boolean generated) {
        this.miniCOGenerated = generated;
        setDirty();
    }

    public int getMiniCOY() {
        return miniCOY;
    }

    public void setMiniCOY(int y) {
        this.miniCOY = y;
        setDirty();
    }

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

        if (entity instanceof Mob mob) {
            PartyAllyGoals.removeAI(mob);
        }

        setDirty();
    }

    public void addPartyMember(Party party, LivingEntity entity) {
        if (party.hasMember(entity.getUUID())) {
            return;
        }

        party.addMember(entity);

        if (entity instanceof Mob mob) {
            PartyAllyGoals.applyAI(mob);
            mob.setTarget(null);
        }

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

    public Struggle getStruggleFromActiveCombatant(UUID memId) {
        for (Struggle struggle : this.struggles) {
            if (struggle.isInProgress() && struggle.getActiveCombatantIds().contains(memId)) {
                return struggle;
            }
        }
        return null;
    }

    public void addStruggleParticipant(Struggle struggle, LivingEntity entity) {
        struggle.addParticipant(entity);
        setDirty();
    }

    public void removeStruggleParticipant(Struggle struggle, UUID entityId) {
        struggle.removeParticipant(entityId);
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

    /**
     * Remembers a board's arena corners after its Struggle is removed (e.g. once a tournament crowns
     * a champion), so the next match created at that same board doesn't need to be reconfigured.
     */
    public void saveStruggleCorners(BlockPos boardPos, BlockPos c1, BlockPos c2) {
        savedStruggleCorners.put(boardPos, new BlockPos[]{c1, c2});
        setDirty();
    }

    @Nullable
    public BlockPos[] getSavedStruggleCorners(BlockPos boardPos) {
        return savedStruggleCorners.get(boardPos);
    }
}