/*
package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class FirstTimeJoinCapability {

    public interface IFirstTimeJoin {
        boolean getFirstTimeJoin();

        int getPosX();
        int getPosY();
        int getPosZ();

        void setFirstTimeJoin(boolean bool);

        void setPosX(int posX);
        void setPosY(int posY);
        void setPosZ(int posZ);
    }

    public static class Storage implements IStorage<IFirstTimeJoin> {

        @Override
        public NBTBase writeNBT(Capability<IFirstTimeJoin> capability, IFirstTimeJoin instance, EnumFacing side) {
            NBTTagCompound properties = new NBTTagCompound();
            properties.setBoolean("FirstTimeJoin", instance.getFirstTimeJoin());
            properties.setInteger("posX", instance.getPosX());
            properties.setInteger("posY", instance.getPosY());
            properties.setInteger("posZ", instance.getPosZ());
            return properties;
        }

        @Override
        public void readNBT(Capability<IFirstTimeJoin> capability, IFirstTimeJoin instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound properties = (NBTTagCompound) nbt;
            instance.setFirstTimeJoin(properties.getBoolean("FirstTimeJoin"));
            instance.setPosX(properties.getInteger("posX"));
            instance.setPosY(properties.getInteger("posY"));
            instance.setPosZ(properties.getInteger("posZ"));
        }
    }

    public static class Default implements IFirstTimeJoin {
        private boolean firstTimeJoin = false;
        private int x = 0, y = 64, z = 0;
        @Override public boolean getFirstTimeJoin() { return this.firstTimeJoin; }
        @Override public void setFirstTimeJoin(boolean bool) { this.firstTimeJoin = bool; }

        @Override
        public int getPosX() {
            return x;
        }

        @Override
        public int getPosY() {
            return y;
        }

        @Override
        public int getPosZ() {
            return z;
        }

        @Override
        public void setPosX(int posX) {
            this.x = posX;
        }

        @Override
        public void setPosY(int posY) {
            this.y = posY;
        }

        @Override
        public void setPosZ(int posZ) {
            this.z = posZ;
        }
    }
}
*/

