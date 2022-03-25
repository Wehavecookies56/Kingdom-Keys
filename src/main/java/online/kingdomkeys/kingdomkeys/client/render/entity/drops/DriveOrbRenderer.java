package online.kingdomkeys.kingdomkeys.client.render.entity.drops;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.DriveOrbEntity;
import online.kingdomkeys.kingdomkeys.entity.ItemDropEntity;

public class DriveOrbRenderer extends EntityItemDropRenderer {

	public DriveOrbRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	/**
	 * Returns the location of an entity's texture.
	 */
	@Override
	public ResourceLocation getTextureLocation(ItemDropEntity entity) {
		this.texture = new ResourceLocation(KingdomKeys.MODID + ":textures/entity/drive_orb.png"); 
		return texture;
	}
}
