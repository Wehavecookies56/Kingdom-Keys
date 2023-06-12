package online.kingdomkeys.kingdomkeys.datagen.init;

import java.util.Optional;

import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SpriteSourceProvider;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class AtlasProvider extends SpriteSourceProvider {

	public AtlasProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator.getPackOutput(), existingFileHelper, KingdomKeys.MODID);
	}

	@Override
	protected void addSources() {
		atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(new ResourceLocation(KingdomKeys.MODID, "entity/models/heart"), Optional.empty()));
		atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(new ResourceLocation(KingdomKeys.MODID, "entity/models/portal"), Optional.empty()));
	}
}
