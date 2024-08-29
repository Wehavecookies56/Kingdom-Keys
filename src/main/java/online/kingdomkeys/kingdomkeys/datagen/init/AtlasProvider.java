package online.kingdomkeys.kingdomkeys.datagen.init;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class AtlasProvider extends SpriteSourceProvider {

	public AtlasProvider(DataGenerator generator, CompletableFuture<HolderLookup.Provider> completableFuture, ExistingFileHelper existingFileHelper) {
		super(generator.getPackOutput(), completableFuture, KingdomKeys.MODID, existingFileHelper);
	}

	@Override
	protected void gather() {
		atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "entity/models/heart"), Optional.empty()));
		atlas(SpriteSourceProvider.BLOCKS_ATLAS).addSource(new SingleFile(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "entity/models/portal"), Optional.empty()));
	}
}
