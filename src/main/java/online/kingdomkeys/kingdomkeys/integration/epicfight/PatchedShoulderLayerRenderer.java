package online.kingdomkeys.kingdomkeys.integration.epicfight;

import static online.kingdomkeys.kingdomkeys.client.render.ShoulderLayerRenderer.models;

import com.mojang.blaze3d.vertex.PoseStack;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.render.ShoulderLayerRenderer;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.item.PauldronItem;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.client.model.transformer.HumanoidModelBaker;
import yesman.epicfight.api.client.neoevent.PrepareModelEvent;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.HashMap;
import java.util.Map;

public class PatchedShoulderLayerRenderer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends HumanoidModel<E>> extends PatchedLayer<E, T, M, ShoulderLayerRenderer<E, M>> {
    public PatchedShoulderLayerRenderer() {}

    ResourceLocation texture;
    public static final Map<Item, SkinnedMesh> epicfight_shoulderModels = new HashMap<>();
    @SubscribeEvent
    public static void clearModels(PrepareModelEvent meshBuildEvent) {
        epicfight_shoulderModels.values().forEach(SkinnedMesh::destroy);
        epicfight_shoulderModels.clear();
    }

    @Override
    protected void renderLayer(T t, E e, ShoulderLayerRenderer<E, M> emRenderLayer, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, OpenMatrix4f[] openMatrix4fs, float bob, float v, float v1, float v2) {
        HumanoidModel<LivingEntity> model = null;
        if (e instanceof Player player) {
            ItemStack armor = PlayerData.get(player).getEquippedKBArmor(0);
            String armorName = armor != null && armor.getItem() instanceof PauldronItem shoulderArmor ? shoulderArmor.getTextureName() : "";
            if(armorName.isEmpty() || !ItemStack.isSameItem(player.getInventory().getItem(38),ItemStack.EMPTY))
                return;

            model = models.get(armorName);
            if (model != null) {
                model.leftArm.visible = true;
                if (!epicfight_shoulderModels.containsKey(armor.getItem())) {
                    //Item doesn't matter
                    epicfight_shoulderModels.put(armor.getItem(), HumanoidModelBaker.bakeArmor(player, armor, (ArmorItem) ModItems.terra_Helmet.get(), EquipmentSlot.CHEST, emRenderLayer.getParentModel(), model, emRenderLayer.getParentModel(), Meshes.BIPED.get()));
                }
                texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/models/armor/"+armorName+"_shoulder.png");
                model.setAllVisible(true);
                AbstractClientPlayer clientPlayer = (AbstractClientPlayer) player;
                boolean steve = clientPlayer.getSkin().model() == PlayerSkin.Model.WIDE;
                poseStack.pushPose();
                VertexConsumer bufferBuilder = multiBufferSource.getBuffer(EpicFightRenderTypes.getTriangulated(EpicFightRenderTypes.armorCutoutNoCull(texture)));
                if (steve)
                    poseStack.translate(-0.07, 0, 0);
                epicfight_shoulderModels.get(armor.getItem()).drawPosed(poseStack, bufferBuilder, Mesh.DrawingFunction.NEW_ENTITY, packedLight, 1, 1, 1, 1, OverlayTexture.NO_OVERLAY, Armatures.BIPED.get(), openMatrix4fs);
                poseStack.popPose();
            }
        }
    }
}
