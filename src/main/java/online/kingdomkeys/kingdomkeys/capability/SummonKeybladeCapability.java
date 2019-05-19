package online.kingdomkeys.kingdomkeys.capability;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.items.ItemStackHandler;
import uk.co.wehavecookies56.kk.common.container.inventory.InventoryKeychain;

public class SummonKeybladeCapability {

    public interface ISummonKeyblade {
        boolean getIsKeybladeSummoned(EnumHand hand);
        void setIsKeybladeSummoned(EnumHand hand, boolean summoned);
        void setInventory(ItemStackHandler handler);
        int activeSlot();
        void setActiveSlot(int slot);

        ItemStackHandler getInventoryKeychain();
    }

    public static class Storage implements IStorage<ISummonKeyblade> {

        @Override
        public NBTBase writeNBT(Capability<ISummonKeyblade> capability, ISummonKeyblade instance, EnumFacing side) {
            NBTTagCompound properties = new NBTTagCompound();
            properties.setBoolean("Is Keyblade Summoned", instance.getIsKeybladeSummoned(EnumHand.MAIN_HAND));
            properties.setBoolean("Is Offhand Keyblade Summoned", instance.getIsKeybladeSummoned(EnumHand.OFF_HAND));
            properties.setInteger("activeSlot", instance.activeSlot());
            properties.setTag(InventoryKeychain.SAVE_KEY, instance.getInventoryKeychain().serializeNBT());

            return properties;
        }

        @Override
        public void readNBT(Capability<ISummonKeyblade> capability, ISummonKeyblade instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound properties = (NBTTagCompound) nbt;
            instance.setIsKeybladeSummoned(EnumHand.MAIN_HAND, properties.getBoolean("Is Keyblade Summoned"));
            instance.setIsKeybladeSummoned(EnumHand.OFF_HAND, properties.getBoolean("Is Offhand Keyblade Summoned"));
            instance.setActiveSlot(properties.getInteger("activeSlot"));
            instance.getInventoryKeychain().deserializeNBT(properties.getCompoundTag(InventoryKeychain.SAVE_KEY));
        }
    }

    public static class Default implements ISummonKeyblade {
        Map<EnumHand, Boolean> keybladeSummoned = new HashMap<>();
        int slot = -1;

        public Default() {
            keybladeSummoned.put(EnumHand.MAIN_HAND, false);
            keybladeSummoned.put(EnumHand.OFF_HAND, false);
        }

        private ItemStackHandler inventoryKeychain = new ItemStackHandler(InventoryKeychain.INV_SIZE);

        @Override
        public void setInventory(ItemStackHandler handler) {
            inventoryKeychain = handler;
        }

        @Override public boolean getIsKeybladeSummoned(EnumHand hand) {
            return keybladeSummoned.get(hand);
        }
        @Override public void setIsKeybladeSummoned(EnumHand hand, boolean summoned) {
            this.keybladeSummoned.replace(hand, keybladeSummoned.get(hand), summoned);
        }
        @Override public ItemStackHandler getInventoryKeychain(){
            return this.inventoryKeychain;
        }

        @Override
        public int activeSlot() {
            return slot;
        }

        @Override
        public void setActiveSlot(int slot) {
            this.slot = slot;
        }
    }
}


