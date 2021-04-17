package online.kingdomkeys.kingdomkeys.client.render.entity.drops;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.FocusOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.ItemDropEntity;

public class FocusOrbRenderer extends EntityItemDropRenderer {
	
	public static final Factory FACTORY = new FocusOrbRenderer.Factory();

	public FocusOrbRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	@Override
	public ResourceLocation getEntityTexture(ItemDropEntity entity) {
		this.texture = new ResourceLocation(KingdomKeys.MODID + ":textures/entity/focus_orb.png"); 
		return texture;
	}

	public static class Factory implements IRenderFactory<FocusOrbEntity> {
		@Override
		public EntityRenderer<? super FocusOrbEntity> createRenderFor(EntityRendererManager entityRendererManager) {
			return new FocusOrbRenderer(entityRendererManager);
		}
	}
}
