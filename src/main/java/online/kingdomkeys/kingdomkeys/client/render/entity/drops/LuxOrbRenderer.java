package online.kingdomkeys.kingdomkeys.client.render.entity.drops;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.drops.ItemDropEntity;

public class LuxOrbRenderer extends EntityItemDropRenderer {
	private static final ResourceLocation TEXTURE = KingdomKeys.rl("textures/entity/lux_orb.png");

	public LuxOrbRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected float getScale() {
		return 0.6F;
	}

	@Override
	public ResourceLocation getTextureLocation(ItemDropEntity entity) {
		return TEXTURE;
	}
}
