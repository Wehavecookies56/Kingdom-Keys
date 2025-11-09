package online.kingdomkeys.kingdomkeys.block;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class ModEnergy {
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ModEntities.TYPE_GUMMI_HANGAR.get(), (blockEntity, direction) -> blockEntity.energyStorage);
    }
}
