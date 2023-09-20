package online.kingdomkeys.kingdomkeys.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.LivingEntity;

public class ArmorBaseModel<T extends LivingEntity> extends EntityModel<T> {

    public final ModelPart root;
    public final ModelPart head;
    public final ModelPart body;
    public final ModelPart leftArm;
    public final ModelPart rightArm;
    public final ModelPart leftLeg;
    public final ModelPart rightLeg;
    public ArmorBaseModel(ModelPart part) {
        super(RenderType::entityTranslucent);
        this.head = part.getChild("head");
        this.body = part.getChild("body");
        this.leftArm = part.getChild("left_arm");
        this.rightArm = part.getChild("right_arm");
        this.leftLeg = part.getChild("left_leg");
        this.rightLeg = part.getChild("right_leg");
        this.root = part;
    }

    @Override
    public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, float pRed, float pGreen, float pBlue, float pAlpha) {

    }
}
