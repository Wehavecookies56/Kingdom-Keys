package online.kingdomkeys.kingdomkeys.client.render.entity.drops;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.ItemDropEntity;

public class MPOrbRenderer extends EntityItemDropRenderer {

	public MPOrbRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	@Override
	public ResourceLocation getTextureLocation(ItemDropEntity entity) {
		this.texture = ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/mp_orb.png");
		return texture;
	}
}
