package online.kingdomkeys.kingdomkeys.client.render.entity.drops;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.EntityItemDrop;
import online.kingdomkeys.kingdomkeys.entity.EntityMunny;

public class MunnyRenderer extends EntityItemDropRenderer {

	public static final Factory FACTORY = new MunnyRenderer.Factory();

	public MunnyRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	@Override
	public ResourceLocation getEntityTexture(EntityItemDrop entity) {
		this.texture = new ResourceLocation(KingdomKeys.MODID + ":textures/entity/munny.png"); 
		return texture;
	}
	
			

	public static class Factory implements IRenderFactory<EntityMunny> {
		@Override
		public EntityRenderer<? super EntityMunny> createRenderFor(EntityRendererManager entityRendererManager) {
			return new MunnyRenderer(entityRendererManager);
		}
	}
}
