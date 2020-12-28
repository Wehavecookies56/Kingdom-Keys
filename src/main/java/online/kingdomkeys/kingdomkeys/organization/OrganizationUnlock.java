package online.kingdomkeys.kingdomkeys.organization;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistryEntry;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class OrganizationUnlock extends ForgeRegistryEntry<OrganizationUnlock> {

    private OrganizationUnlock parent;
    private int unlockCost;
    private UnlockIcon icon;
    private Utils.OrgMember alignment = Utils.OrgMember.NONE;

    public OrganizationUnlock(String registryName, int unlockCost, UnlockIcon icon) {
        setRegistryName(KingdomKeys.MODID + ":" + registryName);
        parent = null;
        this.unlockCost = unlockCost;
        this.icon = icon;
    }

    public OrganizationUnlock(String registryName, int unlockCost, UnlockIcon icon, @Nullable OrganizationUnlock parent) {
        this(registryName, unlockCost, icon);
        this.parent = parent;
    }

    public Utils.OrgMember getAlignment() {
        return alignment;
    }

    public void setAlignment(Utils.OrgMember alignment) {
        this.alignment = alignment;
    }

    public OrganizationUnlock getParent() {
        return parent;
    }

    public void setParent(OrganizationUnlock parent) {
        this.parent = parent;
    }

    public UnlockIcon getIcon() {
        return icon;
    }

    public void setIcon(UnlockIcon icon) {
        this.icon = icon;
    }

    public int getUnlockCost() {
        return unlockCost;
    }

    public void setUnlockCost(int unlockCost) {
        this.unlockCost = unlockCost;
    }

    //Not sure if this will be useful
    public Set<OrganizationUnlock> findChildren(Collection<OrganizationUnlock> unlockCollection) {
        Set<OrganizationUnlock> children = new HashSet<>();
        unlockCollection.forEach(organizationUnlock -> {
            if (organizationUnlock.parent == this) {
                children.add(organizationUnlock);
            }
        });
        return children;
    }

    //Do something when unlocked
    public final void unlock(PlayerEntity playerEntity) {
        MinecraftForge.EVENT_BUS.post(new UnlockEvent(playerEntity, this));
    }

}
