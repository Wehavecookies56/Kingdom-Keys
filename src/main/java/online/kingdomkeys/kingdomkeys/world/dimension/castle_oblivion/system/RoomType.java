package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;

public class RoomType {

    ResourceLocation registryName;
    RoomProperties properties;

    public RoomType(RoomProperties.Builder props) {
        this.properties = props.build();
    }

    public RoomProperties getProperties() {
        return properties;
    }

    public RoomType setRegistryName(ResourceLocation name) {
        registryName = name;
        return this;
    }

    @Nullable
    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public String getTranslationKey() {
        return "room." + registryName.getPath();
    }
}
