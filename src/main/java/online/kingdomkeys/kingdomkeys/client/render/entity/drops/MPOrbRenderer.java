package online.kingdomkeys.kingdomkeys.client.render.entity.drops;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.MPOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.ItemDropEntity;

public class MPOrbRenderer extends EntityItemDropRenderer {
	
	public static final Factory FACTORY = new MPOrbRenderer.Factory();

	public MPOrbRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	@Override
	public ResourceLocation getEntityTexture(ItemDropEntity entity) {
		this.texture = new ResourceLocation(KingdomKeys.MODID + ":textures/entity/mp_orb.png"); 
		return texture;
	}

	public static class Factory implements IRenderFactory<MPOrbEntity> {
		@Override
		public EntityRenderer<? super MPOrbEntity> createRenderFor(EntityRendererManager entityRendererManager) {
			return new MPOrbRenderer(entityRendererManager);
		}
	}
}
