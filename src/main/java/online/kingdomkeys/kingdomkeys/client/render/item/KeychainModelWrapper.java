package online.kingdomkeys.kingdomkeys.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.BakedModelWrapper;

import java.util.EnumMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class KeychainModelWrapper extends BakedModelWrapper<BakedModel> {

	/**
	 * One wrapper per perspective, kept around. applyTransform runs once per frame for every keyblade on
	 * screen, and the child it returns for a given perspective is always the same object, so there is nothing
	 * to gain from building a new wrapper each time.
	 */
	private final Map<ItemDisplayContext, CustomRendered> wrapped = new EnumMap<>(ItemDisplayContext.class);

	public KeychainModelWrapper(BakedModel originalModel) {
		super(originalModel);
	}

	@Override
	public BakedModel applyTransform(ItemDisplayContext displayContext, PoseStack poseStack, boolean applyLeftHandTransform) {
		BakedModel perspective = originalModel.applyTransform(displayContext, poseStack, applyLeftHandTransform);

		// The inventory icon is a plain sprite with no keychain to swing, so it goes down the ordinary path
		if (displayContext == ItemDisplayContext.GUI) {
			return perspective;
		}

		CustomRendered cached = wrapped.get(displayContext);

		if (cached == null || cached.child != perspective) {
			cached = new CustomRendered(perspective);
			wrapped.put(displayContext, cached);
		}

		return cached;
	}

	private static class CustomRendered extends BakedModelWrapper<BakedModel> {
		private final BakedModel child;

		private CustomRendered(BakedModel child) {
			super(child);
			this.child = child;
		}

		@Override
		public boolean isCustomRenderer() {
			return true;
		}
	}
}
