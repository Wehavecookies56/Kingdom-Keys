package online.kingdomkeys.kingdomkeys.client.render;

import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class CameraFrame {
	private static Matrix4f combined;
	private static Vec3 position;

	@SubscribeEvent
	public void render(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) {
			return;
		}

		combined = new Matrix4f(event.getProjectionMatrix()).mul(event.getModelViewMatrix());
		position = event.getCamera().getPosition();
	}

	@Nullable
	public static Vec3 toScreen(Vec3 at, int width, int height) {
		if (combined == null || position == null) {
			return null;
		}

		Vector4f clip = new Vector4f((float) (at.x - position.x), (float) (at.y - position.y), (float) (at.z - position.z), 1F);
		clip.mul(combined);

		if (clip.w <= 0F) {
			return new Vec3(width * 0.5D, height * 0.5D, -1D);
		}

		return new Vec3((clip.x / clip.w * 0.5F + 0.5F) * width, (1F - (clip.y / clip.w * 0.5F + 0.5F)) * height, 1D);
	}
}
