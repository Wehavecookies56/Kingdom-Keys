/*
package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class MunnyCapability {

    public interface IMunny {
        int getMunny();
        void setMunny(int amount);
        void addMunny(int amount);
        void remMunny(int amount);
    }

    public static class Storage implements IStorage<IMunny> {

        @Override
        public NBTBase writeNBT(Capability<IMunny> capability, IMunny instance, EnumFacing side) {
            return new NBTTagInt(instance.getMunny());
        }

        @Override
        public void readNBT(Capability<IMunny> capability, IMunny instance, EnumFacing side, NBTBase nbt) {
            instance.setMunny(((NBTPrimitive)nbt).getInt());

        }
    }

    public static class Default implements IMunny {
        private int munny = 0;
        @Override public int getMunny() { return this.munny; }
        @Override public void setMunny(int value) { this.munny = value; }
        @Override public void addMunny(int value) { this.munny += value; }
        @Override public void remMunny(int value) { 
            if(this.munny-value >= 0){
                this.munny -= value;
            }else{
                this.munny = 0;
            }

        }

    }
}
*/

