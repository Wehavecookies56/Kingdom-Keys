package online.kingdomkeys.kingdomkeys.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CheatModeCapability {

    public interface ICheatMode {
        boolean getCheatMode();
        void setCheatMode(boolean mode);
    }

    public static class Storage implements IStorage<ICheatMode> {

        @Override
        public NBTBase writeNBT(Capability<ICheatMode> capability, ICheatMode instance, EnumFacing side) {
            return new NBTTagInt(instance.getCheatMode() ? 1 : 0);
        }

        @Override
        public void readNBT(Capability<ICheatMode> capability, ICheatMode instance, EnumFacing side, NBTBase nbt) {
            instance.setCheatMode(((NBTPrimitive)nbt).getInt() == 1);
        }

    }

    public static class Default implements ICheatMode {
        boolean mode;
        @Override public boolean getCheatMode() { return this.mode; }
        @Override public void setCheatMode(boolean mode) { this.mode = mode; }

    }

}
