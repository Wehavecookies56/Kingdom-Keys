package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import net.minecraft.resources.ResourceLocation;

public abstract class JsonRegistryObject {
    protected ResourceLocation registryName;

    public ResourceLocation getRegistryName() {
        return registryName;
    }
    @Override
    public String toString() {
        return registryName.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JsonRegistryObject jsonRegistryObject && jsonRegistryObject.registryName.equals(registryName);
    }
}
