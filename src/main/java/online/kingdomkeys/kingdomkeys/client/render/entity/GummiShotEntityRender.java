package online.kingdomkeys.kingdomkeys.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.block.gummi.GummiWeaponBlock;
import online.kingdomkeys.kingdomkeys.entity.GummiShotEntity;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class GummiShotEntityRender extends EntityRenderer<GummiShotEntity> {


	public GummiShotEntityRender(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0F;
	}

    @Override
    public void render(GummiShotEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        if (entity.tickCount < 1)
            return;

        if(entity.getShotType().isEmpty())
            return;

        poseStack.pushPose();
        {
            poseStack.translate(0, 0.05, 0);
            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            Quaternionf q = new Quaternionf(camera.rotation());
            poseStack.mulPose(q);
            float scale = 0.3F;

            VertexConsumer vertex = bufferIn.getBuffer(RenderType.entityCutout(getTextureLocation(entity)));

            Matrix4f matrix = poseStack.last().pose();
            int overlay = OverlayTexture.NO_OVERLAY;
            GummiWeaponBlock.ShotType projectileType = GummiWeaponBlock.ShotType.valueOf(entity.getShotType().toUpperCase());
            if(projectileType.getRootType() == GummiWeaponBlock.ShotType.GRAVITY){
                float maxSize = 2F;
                if(projectileType == GummiWeaponBlock.ShotType.GRAVIRA || projectileType == GummiWeaponBlock.ShotType.GRAVIGA){
                    maxSize = 4F;
                }
                if(entity.getTicks() > 80 && entity.getTicks() < 95){
                    float progress = (entity.getTicks() - 80) / 15f;      // 0 → 1
                    scale = Mth.lerp(progress, 0.3f, 0.05f);
                } else if(entity.getTicks() >= 95){
                    float progress = (entity.getTicks() - 95) / 5f;      // 0 → 1
                    scale = Mth.lerp(progress, 0.05f, maxSize);
                }
            }
            matrix.scale(scale,scale,scale);

            vertex.addVertex(matrix, -0.5f, -0.5f, 0.0f)
                    .setColor(1f, 1f, 1f, 1f)
                    .setUv(0.0f, 1.0f)
                    .setUv1(overlay & 0xFFFF, overlay >> 16)
                    .setLight(packedLightIn)
                    .setNormal(0, 0, 1);

            vertex.addVertex(matrix, 0.5f, -0.5f, 0.0f)
                    .setColor(1f, 1f, 1f, 1f)
                    .setUv(1.0f, 1.0f)
                    .setUv1(overlay & 0xFFFF, overlay >> 16)
                    .setLight(packedLightIn)
                    .setNormal(0, 0, 1);

            vertex.addVertex(matrix, 0.5f, 0.5f, 0.0f)
                    .setColor(1f, 1f, 1f, 1f)
                    .setUv(1.0f, 0.0f)
                    .setUv1(overlay & 0xFFFF, overlay >> 16)
                    .setLight(packedLightIn)
                    .setNormal(0, 0, 1);

            vertex.addVertex(matrix, -0.5f, 0.5f, 0.0f)
                    .setColor(1f, 1f, 1f, 1f)
                    .setUv(0.0f, 0.0f)
                    .setUv1(overlay & 0xFFFF, overlay >> 16)
                    .setLight(packedLightIn)
                    .setNormal(0, 0, 1);
        }
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }


    @Nullable
	@Override
	public ResourceLocation getTextureLocation(GummiShotEntity entity) {
        if(entity.getShotType().isEmpty()) //Just in case
            return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/gummi_fire.png");
        GummiWeaponBlock.ShotType projectileType = GummiWeaponBlock.ShotType.valueOf(entity.getShotType().toUpperCase());

        return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/gummi_"+projectileType.getRootType().name().toLowerCase()+".png");
	}
}