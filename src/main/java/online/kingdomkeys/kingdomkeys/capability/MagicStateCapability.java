package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.items.ItemStackHandler;
import uk.co.wehavecookies56.kk.common.container.inventory.InventorySpells;

public class MagicStateCapability {

    public interface IMagicState {
        boolean getKH1Fire();
        int getMagicLevel(String magic);
        void setKH1Fire(boolean kh1fire);
        void setMagicLevel(String magic, int level);

        ItemStackHandler getInventorySpells();

    }

    public static class Storage implements IStorage<IMagicState> {

        @Override
        public NBTBase writeNBT(Capability<IMagicState> capability, IMagicState instance, EnumFacing side) {
            NBTTagCompound properties = new NBTTagCompound();

            properties.setInteger("MagicLevelFire", instance.getMagicLevel(Strings.Spell_Fire));
            properties.setInteger("MagicLevelBlizzard", instance.getMagicLevel(Strings.Spell_Blizzard));
            properties.setInteger("MagicLevelThunder", instance.getMagicLevel(Strings.Spell_Thunder));
            properties.setInteger("MagicLevelCure", instance.getMagicLevel(Strings.Spell_Cure));
            properties.setInteger("MagicLevelAero", instance.getMagicLevel(Strings.Spell_Aero));
            properties.setInteger("MagicLevelStop", instance.getMagicLevel(Strings.Spell_Stop));

            properties.setBoolean("KH1Fire", instance.getKH1Fire());

            properties.setTag(InventorySpells.SAVE_KEY, instance.getInventorySpells().serializeNBT());

            return properties;
        }

        @Override
        public void readNBT(Capability<IMagicState> capability, IMagicState instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound properties = (NBTTagCompound) nbt;
            instance.setMagicLevel(Strings.Spell_Fire, properties.getInteger("MagicLevelFire"));
            instance.setMagicLevel(Strings.Spell_Blizzard, properties.getInteger("MagicLevelBlizzard"));
            instance.setMagicLevel(Strings.Spell_Thunder, properties.getInteger("MagicLevelThunder"));
            instance.setMagicLevel(Strings.Spell_Cure, properties.getInteger("MagicLevelCure"));
            instance.setMagicLevel(Strings.Spell_Aero, properties.getInteger("MagicLevelAero"));
            instance.setMagicLevel(Strings.Spell_Stop, properties.getInteger("MagicLevelStop"));

            instance.setKH1Fire(properties.getBoolean("KH1Fire"));

            instance.getInventorySpells().deserializeNBT(properties.getCompoundTag(InventorySpells.SAVE_KEY));



        }
    }

    public static class Default implements IMagicState {
        private int fireLevel = 1, blizzardLevel = 1, thunderLevel = 1, cureLevel = 1, aeroLevel = 1, stopLevel = 1;
        private boolean kh1fire = false;
        private final ItemStackHandler inventorySpells = new ItemStackHandler(InventorySpells.INV_SIZE);


        @Override
        public int getMagicLevel(String magic) {
            switch(magic)
            {
            case Strings.Spell_Fire:
                return fireLevel;
            case Strings.Spell_Blizzard:
                return blizzardLevel;
            case Strings.Spell_Thunder:
                return thunderLevel;
            case Strings.Spell_Cure:
                return cureLevel;
            case Strings.Spell_Aero:
                return aeroLevel;
            case Strings.Spell_Stop:
                return stopLevel;
            }
            return 0;
        }
        @Override
        public void setMagicLevel(String magic, int level) {
            switch(magic)
            {
            case Strings.Spell_Fire:
                fireLevel = level;
                break;
            case Strings.Spell_Blizzard:
                blizzardLevel = level;
                break;
            case Strings.Spell_Thunder:
                thunderLevel = level;
                break;
            case Strings.Spell_Cure:
                cureLevel = level;
                break;
            case Strings.Spell_Aero:
                aeroLevel = level;
                break;
            case Strings.Spell_Stop:
                stopLevel = level;
                break;
            }
        }
        @Override
        public void setKH1Fire(boolean kh1fire)
        {
            this.kh1fire = kh1fire;
        }
        @Override
        public boolean getKH1Fire()
        {
            return this.kh1fire;
        }
        
        @Override public ItemStackHandler getInventorySpells(){return this.inventorySpells;}

    }
}


