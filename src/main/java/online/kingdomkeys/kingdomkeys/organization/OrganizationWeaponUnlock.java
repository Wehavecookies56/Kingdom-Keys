package online.kingdomkeys.kingdomkeys.organization;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class OrganizationWeaponUnlock extends OrganizationUnlock {

    private ItemStack weapon;

    public OrganizationWeaponUnlock(String registryName, int unlockCost, Utils.OrgMember alignment, ItemStack weapon) {
        super(registryName, unlockCost, new ItemUnlockIcon(weapon));
        this.weapon = weapon;
        setAlignment(alignment);
    }

    public ItemStack getWeapon() {
        return weapon;
    }

    public void setWeapon(ItemStack weapon) {
        this.weapon = weapon;
    }
}
