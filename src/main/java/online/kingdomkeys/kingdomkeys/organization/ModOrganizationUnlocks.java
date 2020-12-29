package online.kingdomkeys.kingdomkeys.organization;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.organization.AxeSwordItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.item.organization.OrgWeaponItem;
import online.kingdomkeys.kingdomkeys.lib.Lists;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModOrganizationUnlocks {

    public static IForgeRegistry<OrganizationUnlock> registry;

    public static OrganizationUnlock getUnlock(ResourceLocation location) {
        if (registry.containsKey(location)) {
            return registry.getValue(location);
        } else {
            System.out.println("NOT REGISTERED");
        }
        return null;
    }

    @SubscribeEvent
    public static void registerRegistry(RegistryEvent.NewRegistry event) {
         registry = new RegistryBuilder<OrganizationUnlock>().setName(new ResourceLocation(KingdomKeys.MODID, "organization_unlocks")).setType(OrganizationUnlock.class).create();
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<OrganizationUnlock> event) {
        List<OrganizationWeaponUnlock> unlocks = new ArrayList<>();
        Utils.OrgMember[] members = Utils.OrgMember.values();
        for (int i = 0; i < members.length; ++i) {
            List<Item> current = Lists.getListForMember(members[i]);
            current.forEach(item -> {
                if (item instanceof OrgWeaponItem) {
                    unlocks.add(createWeaponUnlock((OrgWeaponItem) item, 1000));
                }
            });

        }
        unlocks.forEach(unlock -> {
            event.getRegistry().register(unlock);
            System.out.println(unlock.getRegistryName());
        });
    }

    public static OrganizationWeaponUnlock createWeaponUnlock(OrgWeaponItem weapon, int cost) {
        return new OrganizationWeaponUnlock(weapon.getRegistryName().toString(), cost, weapon.getMember(), new ItemStack(weapon));
    }

    @SubscribeEvent
    public static void unlockHandler(UnlockEvent event) {
        PlayerEntity playerEntity = event.getPlayer();
        OrganizationUnlock unlock = event.getUnlock();
        IPlayerCapabilities playerData = ModCapabilities.getPlayer(playerEntity);
        if (playerData.getHearts() >= unlock.getUnlockCost()) {
            if (unlock instanceof OrganizationWeaponUnlock) {
                OrganizationWeaponUnlock weaponUnlock = (OrganizationWeaponUnlock) unlock;
                if (!playerData.isWeaponUnlocked(weaponUnlock)) {
                    playerData.unlockWeapon(weaponUnlock);
                    playerData.removeHearts(weaponUnlock.getUnlockCost());
                }
            }
        }
    }
}
