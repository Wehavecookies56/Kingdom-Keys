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
import net.neoforged.neoforge.registries.DeferredHolder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.banners.ModBannerPatterns;

import java.util.concurrent.CompletableFuture;

public class BannerPatterns extends BannerPatternTagsProvider {

    public BannerPatterns(DataGenerator generator, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(generator.getPackOutput(), provider, KingdomKeys.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (DeferredHolder<BannerPattern, ? extends BannerPattern> patternEntry : ModBannerPatterns.PATTERNS.getEntries()) {
            ResourceKey<BannerPattern> banner = patternEntry.getKey();
            this.tag(BannerPatternTags.NO_ITEM_REQUIRED).add(create(banner.location()));
        }
    }

    private static ResourceKey<BannerPattern> create(String name) {
        return ResourceKey.create(Registries.BANNER_PATTERN, KingdomKeys.rl(name));
    }

    private static ResourceKey<BannerPattern> create(ResourceLocation name) {
        return ResourceKey.create(Registries.BANNER_PATTERN, name);
    }
}
