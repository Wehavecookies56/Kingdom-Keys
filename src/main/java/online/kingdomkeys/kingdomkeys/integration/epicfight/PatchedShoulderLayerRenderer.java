package online.kingdomkeys.kingdomkeys.integration.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.ShoulderArmorItem;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.client.model.CustomModelBakery;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import static online.kingdomkeys.kingdomkeys.client.render.ShoulderLayerRenderer.models;

public class PatchedShoulderLayerRenderer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends EntityModel<E>, AM extends AnimatedMesh> extends PatchedLayer<E, T, M, RenderLayer<E, M>, AM> {
    public PatchedShoulderLayerRenderer(AM mesh) {
        super(mesh);
    }

    ResourceLocation texture;

    @Override
    protected void renderLayer(T t, E e, RenderLayer<E, M> emRenderLayer, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, OpenMatrix4f[] openMatrix4fs, float v, float v1, float v2) {
        HumanoidModel<LivingEntity> model = null;
        if (e instanceof Player player) {
            ItemStack armor = ModCapabilities.getPlayer(player).getEquippedKBArmor(0);
            String armorName = armor != null && armor.getItem() instanceof ShoulderArmorItem shoulderArmor ? shoulderArmor.getTextureName() : "";
            if(armorName.equals("") || !ItemStack.isSame(player.getInventory().getItem(38),ItemStack.EMPTY))
                return;

            texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/"+armorName+"_shoulder.png");
            VertexConsumer vertexconsumer = EpicFightRenderTypes.getArmorFoilBufferTriangles(multiBufferSource, RenderType.entityCutoutNoCull(texture), false, false);
            model = models.get(armorName);
            boolean steve = DefaultPlayerSkin.getSkinModelName(e.getUUID()).equals("default");;
            boolean debuggingMode = ClientEngine.getInstance().isArmorModelDebuggingMode();
            //Item doesn't matter
            AnimatedMesh modelAnimated = CustomModelBakery.bakeBipedCustomArmorModel(model, (ArmorItem) ModItems.terra_Chestplate.get(), EquipmentSlot.CHEST, debuggingMode);

            poseStack.pushPose();
            if(steve)
               poseStack.translate(-0.07, 0, 0);
            modelAnimated.drawModelWithPose(poseStack, vertexconsumer, i, 1,1,1,1, OverlayTexture.NO_OVERLAY, Armatures.BIPED, openMatrix4fs);
            poseStack.popPose();
        }
    }
}
