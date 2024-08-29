package online.kingdomkeys.kingdomkeys.api.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.ICancellableEvent;
import online.kingdomkeys.kingdomkeys.shotlock.ModShotlocks;

import javax.annotation.Nullable;

/**
 * Events for when equipment is equipped/unequipped
 */
public abstract class EquipmentEvent extends Event {
    
    private final Player player;
    private final ItemStack newStack, previousStack;
    private final int slotFrom;

    private EquipmentEvent(Player player, ItemStack previousStack, ItemStack newStack, int slotFrom) {
        this.player = player;
        this.previousStack = previousStack;
        this.newStack = newStack;
        this.slotFrom = slotFrom;
    }
    
    public Player getPlayer() {
        return player;
    }

    /**
     * @return The stack being equipped, null if a shotlock
     */
    @Nullable
    public ItemStack getNewStack() {
        return newStack;
    }

    /**
     * @return The stack being unequipped, null if a shotlock
     */
    @Nullable
    public ItemStack getPreviousStack() {
        return previousStack;
    }

    /**
     * @return The inventory slot index of the newStack, -1 if a shotlock or an orgweapon
     */
    public int getSlotFrom() {
        return slotFrom;
    }

    public static class Keychain extends EquipmentEvent implements ICancellableEvent {
        private final ResourceLocation keychainSlot;
        public Keychain(Player player, ItemStack previousStack, ItemStack newStack, int slotFrom, ResourceLocation keychainSlot) {
            super(player, previousStack, newStack, slotFrom);
            this.keychainSlot = keychainSlot;
        }

        public ResourceLocation getKeychainSlot() {
            return keychainSlot;
        }
    }

    public static class OrgWeapon extends EquipmentEvent implements ICancellableEvent {
        public OrgWeapon(Player player, ItemStack previousStack, ItemStack newStack) {
            super(player, previousStack, newStack, -1);
        }
    }

    public static class Shotlock extends EquipmentEvent implements ICancellableEvent {
        private final online.kingdomkeys.kingdomkeys.shotlock.Shotlock previousShotlock, newShotlock;
        public Shotlock(Player player, ResourceLocation previousShotlock, ResourceLocation newShotlock) {
            super(player, null, null, -1);
            this.newShotlock = ModShotlocks.registry.get(newShotlock);
            this.previousShotlock = ModShotlocks.registry.get(previousShotlock);
        }

        public online.kingdomkeys.kingdomkeys.shotlock.Shotlock getPreviousShotlock() {
            return previousShotlock;
        }

        public online.kingdomkeys.kingdomkeys.shotlock.Shotlock getNewShotlock() {
            return newShotlock;
        }
    }

    private static class BaseEquipment extends EquipmentEvent implements ICancellableEvent {
        private final int slotTo;
        private BaseEquipment(Player player, ItemStack previousStack, ItemStack newStack, int slotFrom, int slotTo) {
            super(player, previousStack, newStack, slotFrom);
            this.slotTo = slotTo;
        }

        public int getSlotTo() {
            return slotTo;
        }
    }

    public static class Item extends BaseEquipment {
        public Item(Player player, ItemStack previousStack, ItemStack newStack, int slotFrom, int slotTo) {
            super(player, previousStack, newStack, slotFrom, slotTo);
        }
    }

    public static class Accessory extends BaseEquipment {
        public Accessory(Player player, ItemStack previousStack, ItemStack newStack, int slotFrom, int slotTo) {
            super(player, previousStack, newStack, slotFrom, slotTo);
        }
    }

    public static class Armour extends BaseEquipment {
        public Armour(Player player, ItemStack previousStack, ItemStack newStack, int slotFrom, int slotTo) {
            super(player, previousStack, newStack, slotFrom, slotTo);
        }
    }

    public static class Pauldron extends BaseEquipment {
        public Pauldron(Player player, ItemStack previousStack, ItemStack newStack, int slotFrom, int slotTo) {
            super(player, previousStack, newStack, slotFrom, slotTo);
        }
    }

}
