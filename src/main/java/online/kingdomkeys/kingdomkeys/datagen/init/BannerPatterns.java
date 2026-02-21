package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BannerPatternTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BannerPatternTags;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import java.util.concurrent.CompletableFuture;

public class BannerPatterns extends BannerPatternTagsProvider {

    public BannerPatterns(DataGenerator generator, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(generator.getPackOutput(), provider, KingdomKeys.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BannerPatternTags.NO_ITEM_REQUIRED).add(create("heartless"));
    }

    private static ResourceKey<BannerPattern> create(String name) {
        return ResourceKey.create(Registries.BANNER_PATTERN, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, name));
    }
}
