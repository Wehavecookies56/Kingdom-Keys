package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CastleOblivionRenderInfo extends DimensionSpecialEffects {

    public CastleOblivionRenderInfo() {
        super(Float.NaN, true, DimensionSpecialEffects.SkyType.NONE, false, true);
        setSkyRenderHandler((ticks, partialTicks, matrixStack, world, mc) -> {});
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 p_230494_1_, float p_230494_2_) {
        return Vec3.ZERO;
    }

    @Override
    public boolean isFoggyAt(int p_230493_1_, int p_230493_2_) {
        return false;
    }

}
