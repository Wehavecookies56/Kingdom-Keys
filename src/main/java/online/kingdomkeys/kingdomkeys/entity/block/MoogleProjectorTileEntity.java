package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class MoogleProjectorTileEntity extends TileEntity{
    public MoogleProjectorTileEntity() {
        super(ModEntities.TYPE_MOOGLE_PROJECTOR.get());
    }
}
