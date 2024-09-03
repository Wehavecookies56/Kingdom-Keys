package online.kingdomkeys.kingdomkeys.integration.epicfight;

import static online.kingdomkeys.kingdomkeys.client.render.KeybladeArmorRenderer.armorModels;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.model.armor.ArmorBaseModel;
import online.kingdomkeys.kingdomkeys.item.KeybladeArmorItem;
import online.kingdomkeys.kingdomkeys.util.Utils;
import yesman.epicfight.api.client.model.AnimatedMesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.client.model.transformer.CustomModelBakery;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class PatchedArmourLayerRenderer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends EntityModel<E>, AM extends  AnimatedMesh> extends PatchedLayer<E, T, M, RenderLayer<E, M>> {

    boolean hideHelmet;

    public PatchedArmourLayerRenderer(boolean hideHelmet) {
        this.hideHelmet = hideHelmet;
    }

    ResourceLocation texture;

    @Override
    public void renderLayer(T t, E e, RenderLayer<E, M> emRenderLayer, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightIn, OpenMatrix4f[] poses, float bob, float netYawHead, float pitchHead, float partialTicks) {
        if (e instanceof Player player) {
            IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
            int color = playerData.getArmorColor();
            float red = ((color >> 16) & 0xff) / 255F;
            float green = ((color >> 8) & 0xff) / 255F;
            float blue = (color & 0xff) / 255F;
            boolean glint = playerData.getArmorGlint();

            NonNullList<ItemStack> armor = player.getInventory().armor;
            for (int i = 0; i <= 3; i++) {
                if (hideHelmet && i == 3) {
                    break;
                }
                ItemStack itemStack = armor.get(i);
                if (itemStack.getItem() instanceof KeybladeArmorItem item) {
                    ArmorBaseModel<LivingEntity> model = armorModels.get(item);
                    HumanoidModel<LivingEntity> humanoidModel = new HumanoidModel<>(model.root);
                    AnimatedMesh modelAnimated = CustomModelBakery.bakeArmor(humanoidModel, item, EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, i));
                    String armorName = Utils.getItemRegistryName(item).getPath().substring(0,Utils.getItemRegistryName(item).getPath().indexOf("_"));
                    String textureIndex = i == 1 ? "2" : "1";
                    texture = new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/"+armorName+textureIndex+".png");
                    //VertexConsumer vertexconsumer = EpicFightRenderTypes.getArmorFoilBufferTriangles(multiBufferSource, RenderType.entityCutoutNoCull(texture), false, glint && itemStack.isEnchanted());
                    modelAnimated.draw(poseStack, multiBufferSource, EpicFightRenderTypes.armorCutoutNoCull(texture), packedLightIn, red, green, blue, 1, OverlayTexture.NO_OVERLAY, Armatures.BIPED, poses);
                }
            }
        }
    }



    public HumanoidMesh getModel(E e) {
        boolean defaultSkin = ((AbstractClientPlayer)e).getModelName().equals("default");
        if (defaultSkin) {
            return Meshes.BIPED;
        } else {
            return Meshes.ALEX;
        }
    }
}
