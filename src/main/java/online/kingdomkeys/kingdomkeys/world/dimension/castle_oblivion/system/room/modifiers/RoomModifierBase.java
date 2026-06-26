package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.room.modifiers;

import net.minecraft.resources.ResourceLocation;

public class RoomModifierBase implements RoomModifier {

    ResourceLocation registryName;

    public RoomModifierBase(ResourceLocation registryName) {
        this.registryName = registryName;
    }

    @Override
    public ResourceLocation getModifierName() {
        return registryName;
    }
}
