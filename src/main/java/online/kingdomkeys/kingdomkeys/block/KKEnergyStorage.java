package online.kingdomkeys.kingdomkeys.block;

import net.neoforged.neoforge.energy.EnergyStorage;

public class KKEnergyStorage extends EnergyStorage {
    public KKEnergyStorage(int capacity, int maxReceive, int maxExtract) {
        super(capacity, maxReceive, maxExtract);
    }

    public int setEnergy(int energy){
        this.energy = energy;
        return energy;
    }
}
