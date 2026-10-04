package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.entity.ForetellerModel;
import online.kingdomkeys.kingdomkeys.entity.mob.ForetellerEntity;
import online.kingdomkeys.kingdomkeys.lib.Union;

import java.util.EnumMap;
import java.util.Map;

public class ForetellerRenderer extends HumanoidMobRenderer<ForetellerEntity, ForetellerModel<ForetellerEntity>> {

    private static final Map<Union, ResourceLocation> TEXTURES = new EnumMap<>(Union.class);

    static {
        TEXTURES.put(Union.UNICORNIS, KingdomKeys.rl("textures/entity/mob/foreteller_ira.png"));
        TEXTURES.put(Union.LEOPARDOS, KingdomKeys.rl("textures/entity/mob/foreteller_gula.png"));
        TEXTURES.put(Union.VULPES, KingdomKeys.rl("textures/entity/mob/foreteller_ava.png"));
        TEXTURES.put(Union.ANGUIS, KingdomKeys.rl("textures/entity/mob/foreteller_invi.png"));
        TEXTURES.put(Union.URSUS, KingdomKeys.rl("textures/entity/mob/foreteller_aced.png"));
    }

    private final ForetellerModel<ForetellerEntity> wide;
    private final ForetellerModel<ForetellerEntity> slim;

    public ForetellerRenderer(EntityRendererProvider.Context context) {
        super(context, new ForetellerModel<>(context.bakeLayer(ForetellerModel.LAYER_LOCATION)), 0.5F);
        this.wide = this.model;
        this.slim = new ForetellerModel<>(context.bakeLayer(ForetellerModel.SLIM_LAYER_LOCATION));
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)), new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)), context.getModelManager()));
    }

    @Override
    public void render(ForetellerEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = entity.isSlim() ? slim : wide;
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public boolean shouldRender(ForetellerEntity entity, Frustum frustum, double camX, double camY, double camZ) {
        return !entity.isInvisible() && super.shouldRender(entity, frustum, camX, camY, camZ);
    }

    @Override
    public ResourceLocation getTextureLocation(ForetellerEntity entity) {
        return TEXTURES.getOrDefault(entity.getUnion(), TEXTURES.get(Union.UNICORNIS));
    }
}
