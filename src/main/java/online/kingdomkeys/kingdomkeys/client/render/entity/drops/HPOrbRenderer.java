package online.kingdomkeys.kingdomkeys.client.render.entity.drops;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fmlclient.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.HPOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.ItemDropEntity;

public class HPOrbRenderer extends EntityItemDropRenderer {
	
	public static final Factory FACTORY = new HPOrbRenderer.Factory();

	public HPOrbRenderer(EntityRenderDispatcher renderManagerIn) {
		super(renderManagerIn);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	@Override
	public ResourceLocation getTextureLocation(ItemDropEntity entity) {
		this.texture = new ResourceLocation(KingdomKeys.MODID + ":textures/entity/hp_orb.png"); 
		return texture;
	}
	
			

	public static class Factory implements IRenderFactory<HPOrbEntity> {
		@Override
		public EntityRenderer<? super HPOrbEntity> createRenderFor(EntityRenderDispatcher entityRendererManager) {
			return new HPOrbRenderer(entityRendererManager);
		}
	}
}
