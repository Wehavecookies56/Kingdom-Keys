package online.kingdomkeys.kingdomkeys.world.dimension.station_of_sorrow;

import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StationOfSorrowRenderInfo extends DimensionRenderInfo {

    public StationOfSorrowRenderInfo() {
        super(Float.NaN, true, FogType.NONE, true, true);
        setSkyRenderHandler((ticks, partialTicks, matrixStack, world, mc) -> {});
    }

    @Override
    public Vector3d func_230494_a_(Vector3d p_230494_1_, float p_230494_2_) {
        return Vector3d.ZERO;
    }

    @Override
    public boolean func_230493_a_(int p_230493_1_, int p_230493_2_) {
        return true;
    }
}
