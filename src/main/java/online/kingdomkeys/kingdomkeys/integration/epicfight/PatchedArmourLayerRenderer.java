package online.kingdomkeys.kingdomkeys.integration.epicfight;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.model.armor.ArmorBaseModel;
import online.kingdomkeys.kingdomkeys.client.render.KeybladeArmorRenderer;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KeybladeArmorItem;
import online.kingdomkeys.kingdomkeys.util.Utils;
import yesman.epicfight.api.client.event.types.render.PrepareModelEvent;
import yesman.epicfight.api.client.model.Mesh;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.client.model.transformer.HumanoidModelBaker;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.mesh.HumanoidMesh;
import yesman.epicfight.client.renderer.EpicFightRenderTypes;
import yesman.epicfight.client.renderer.patched.layer.PatchedLayer;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static online.kingdomkeys.kingdomkeys.client.render.KeybladeArmorRenderer.armorModels;

public class PatchedArmourLayerRenderer<E extends LivingEntity, T extends LivingEntityPatch<E>, M extends HumanoidModel<E>> extends PatchedLayer<E, T, M, KeybladeArmorRenderer<E, M>> {

    boolean hideHelmet;

    // Cache to store generated epic fight model
    public static final Map<Item, SkinnedMesh> epicfight_armorModels = new HashMap<>();

    public static void clearModels(PrepareModelEvent meshBuildEvent) {
        epicfight_armorModels.values().forEach(SkinnedMesh::destroy);
        epicfight_armorModels.clear();
    }

    public PatchedArmourLayerRenderer(boolean hideHelmet) {
        this.hideHelmet = hideHelmet;
    }

    ResourceLocation texture;

    @Override
    public void renderLayer(T t, E e, KeybladeArmorRenderer<E, M> emRenderLayer, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLightIn, OpenMatrix4f[] poses, float bob, float netYawHead, float pitchHead, float partialTicks) {
        if (e instanceof Player player) {
            PlayerData playerData = PlayerData.get(player);
            int color = playerData.getArmorColor();
            float red = ((color >> 16) & 0xff) / 255F;
            float green = ((color >> 8) & 0xff) / 255F;
            float blue = (color & 0xff) / 255F;
            boolean glint = playerData.getArmorGlint();

            List<EquipmentSlot> slots = List.of(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD);

            NonNullList<ItemStack> armor = player.getInventory().armor;
            for (int i = 0; i <= 3; i++) {
                if (hideHelmet && i == 3) {
                    break;
                }

                ItemStack itemStack = armor.get(i);

                if (itemStack.getItem() instanceof KeybladeArmorItem item) {
                    ArmorBaseModel<LivingEntity> model = armorModels.get(item);

                    if (!epicfight_armorModels.containsKey(item) || ClientEngine.getInstance().isVanillaModelDebuggingMode()) {
                        HumanoidModel<LivingEntity> humanoidModel = new HumanoidModel<>(model.root);
                        setPartVisibility(humanoidModel, slots.get(i));
                        epicfight_armorModels.put(item, HumanoidModelBaker.bakeArmor(player, itemStack, item, slots.get(i), emRenderLayer.getParentModel(), humanoidModel, emRenderLayer.getParentModel(), Meshes.BIPED.get()));
                        humanoidModel.setAllVisible(true);
                    }

                    SkinnedMesh modelAnimated = epicfight_armorModels.get(item);
                    String armorName = Utils.getItemRegistryName(item).getPath().substring(0,Utils.getItemRegistryName(item).getPath().indexOf("_"));
                    String textureIndex = i == 1 ? "2" : "1";
                    texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/models/armor/"+armorName+textureIndex+".png");
                    //VertexConsumer vertexconsumer = EpicFightRenderTypes.getArmorFoilBufferTriangles(multiBufferSource, RenderType.entityCutoutNoCull(texture), false, glint && itemStack.isEnchanted());
                    VertexConsumer bufferBuilder = multiBufferSource.getBuffer(EpicFightRenderTypes.getTriangulated(EpicFightRenderTypes.armorCutoutNoCull(texture)));
                    modelAnimated.drawPosed(poseStack, bufferBuilder, Mesh.DrawingFunction.NEW_ENTITY, packedLightIn, red, green, blue, 1, OverlayTexture.NO_OVERLAY, Armatures.BIPED.get(), poses);
                }
            }
        }
    }

    /** Copied from {@link HumanoidArmorLayer#setPartVisibility} **/
    protected void setPartVisibility(HumanoidModel<LivingEntity> pModel, EquipmentSlot pSlot) {
        pModel.setAllVisible(false);
        switch (pSlot) {
            case HEAD:
                pModel.head.visible = true;
                pModel.hat.visible = true;
                break;
            case CHEST:
                pModel.body.visible = true;
                pModel.rightArm.visible = true;
                pModel.leftArm.visible = true;
                break;
            case LEGS:
                pModel.body.visible = true;
                pModel.rightLeg.visible = true;
                pModel.leftLeg.visible = true;
                break;
            case FEET:
                pModel.rightLeg.visible = true;
                pModel.leftLeg.visible = true;
        }
    }

    public HumanoidMesh getModel(E e) {
        return ((AbstractClientPlayer)e).getSkin().model() == PlayerSkin.Model.WIDE ? Meshes.BIPED.get() : Meshes.ALEX.get();
    }
}
