package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.item.Item;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class DriveForm extends ForgeRegistryEntry<DriveForm> {

    String name;
    int cost;

    public DriveForm(String registryName, int cost) {
    	this.name = registryName;
    	this.cost = cost;
        setRegistryName(registryName);
    }

}