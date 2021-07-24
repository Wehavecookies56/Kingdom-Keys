package online.kingdomkeys.kingdomkeys.client.render.entity.drops;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmlclient.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.ItemDropEntity;
import online.kingdomkeys.kingdomkeys.entity.MPOrbEntity;

public class MPOrbRenderer extends EntityItemDropRenderer {
	
	public static final Factory FACTORY = new MPOrbRenderer.Factory();

	public MPOrbRenderer(EntityRenderDispatcher renderManagerIn) {
		super(renderManagerIn);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	@Override
	public ResourceLocation getTextureLocation(ItemDropEntity entity) {
		this.texture = new ResourceLocation(KingdomKeys.MODID + ":textures/entity/mp_orb.png"); 
		return texture;
	}

	public static class Factory implements IRenderFactory<MPOrbEntity> {
		@Override
		public EntityRenderer<? super MPOrbEntity> createRenderFor(EntityRenderDispatcher entityRendererManager) {
			return new MPOrbRenderer(entityRendererManager);
		}
	}
}
