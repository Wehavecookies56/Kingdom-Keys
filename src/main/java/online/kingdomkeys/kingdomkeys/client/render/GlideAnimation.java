package online.kingdomkeys.kingdomkeys.client.render;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import online.kingdomkeys.kingdomkeys.data.PlayerData;

@OnlyIn(Dist.CLIENT)
public class GlideAnimation {

	private static final float ARM_SPREAD = 1.35F;
	private static final float ARM_PITCH = -0.15F;

	private static final float LEG_SPREAD = 0.20F;
	private static final float LEG_PITCH = 0.05F;

	public static boolean isGliding(Entity entity) {
		if (!(entity instanceof Player player)) {
			return false;
		}

		PlayerData data = PlayerData.get(player);

		return data != null && data.getIsGliding();
	}

	public static void applyPose(PlayerModel<?> model) {
		model.rightArm.xRot = ARM_PITCH;
		model.leftArm.xRot = ARM_PITCH;
		model.rightArm.yRot = 0.0F;
		model.leftArm.yRot = 0.0F;
		model.rightArm.zRot = ARM_SPREAD;
		model.leftArm.zRot = -ARM_SPREAD;

		model.rightLeg.xRot = LEG_PITCH;
		model.leftLeg.xRot = LEG_PITCH;
		model.rightLeg.yRot = 0.0F;
		model.leftLeg.yRot = 0.0F;
		model.rightLeg.zRot = LEG_SPREAD;
		model.leftLeg.zRot = -LEG_SPREAD;

		model.leftSleeve.copyFrom(model.leftArm);
		model.rightSleeve.copyFrom(model.rightArm);
		model.leftPants.copyFrom(model.leftLeg);
		model.rightPants.copyFrom(model.rightLeg);
		model.jacket.copyFrom(model.body);
	}
}
