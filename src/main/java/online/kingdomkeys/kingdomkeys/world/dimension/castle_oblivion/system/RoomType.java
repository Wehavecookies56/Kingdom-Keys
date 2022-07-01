package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public class RoomType implements IForgeRegistryEntry<RoomType> {

    ResourceLocation registryName;
    RoomProperties properties;

    public RoomType(RoomProperties.Builder props) {
        this.properties = props.build();
    }

    public RoomProperties getProperties() {
        return properties;
    }

    @Override
    public RoomType setRegistryName(ResourceLocation name) {
        registryName = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    public Class<RoomType> getRegistryType() {
        return RoomType.class;
    }

    public String getTranslationKey() {
        return "room." + registryName.getPath();
    }
}
