package online.kingdomkeys.kingdomkeys.client.render.entity.drops;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.EntityDriveOrb;
import online.kingdomkeys.kingdomkeys.entity.EntityItemDrop;

public class DriveOrbRenderer extends EntityItemDropRenderer {
	
	public static final Factory FACTORY = new DriveOrbRenderer.Factory();

	public DriveOrbRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	@Override
	public ResourceLocation getEntityTexture(EntityItemDrop entity) {
		this.texture = new ResourceLocation(KingdomKeys.MODID + ":textures/entity/drive_orb.png"); 
		return texture;
	}
	
			

	public static class Factory implements IRenderFactory<EntityDriveOrb> {
		@Override
		public EntityRenderer<? super EntityDriveOrb> createRenderFor(EntityRendererManager entityRendererManager) {
			return new DriveOrbRenderer(entityRendererManager);
		}
	}
}
