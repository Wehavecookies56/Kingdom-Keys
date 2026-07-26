package online.kingdomkeys.kingdomkeys.client.model.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class CrownModel extends Model {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(KingdomKeys.rl("crown"), "main");
	public final ModelPart root;
	public final ModelPart crown;

	public CrownModel(ModelPart root) {
		super(RenderType::entityCutoutNoCull);
		this.root = root.getChild("root");
		this.crown = this.root.getChild("crown");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition root = mesh.getRoot();

		// Pivote en el origen
		PartDefinition pivot = root.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.ZERO);

		// La base de la corona coincide con el pivote (y = 0)
		pivot.addOrReplaceChild("crown", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-4.0F, -6.0F, -4.0F, 8.0F, 6.0F, 1.0F)
				.texOffs(0, 7).addBox(-4.0F, -6.0F, 3.0F, 8.0F, 6.0F, 1.0F)
				.texOffs(0, 14).addBox(-4.0F, -6.0F, -3.0F, 1.0F, 6.0F, 6.0F)
				.texOffs(14, 14).addBox(3.0F, -6.0F, -3.0F, 1.0F, 6.0F, 6.0F),
			PartPose.ZERO
		);

		return LayerDefinition.create(mesh, 32, 32);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int colour) {
		root.render(poseStack, buffer, packedLight, packedOverlay, colour);
	}
}