package online.kingdomkeys.kingdomkeys.datagen.init;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BannerPatternTags;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.registries.DeferredHolder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.banners.ModBannerPatterns;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BannerPatternJsonProvider implements DataProvider {

    private final PackOutput output;

    public BannerPatternJsonProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (DeferredHolder<BannerPattern, ? extends BannerPattern> patternEntry : ModBannerPatterns.PATTERNS.getEntries()) {
            ResourceKey<BannerPattern> banner = patternEntry.getKey();
            futures.add(generatePattern(cache, banner.location()));
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> generatePattern(CachedOutput cache, ResourceLocation name) {
        JsonObject json = new JsonObject();
        json.addProperty("asset_id", name.toString());
        json.addProperty("translation_key", "kingdomkeys.banner." + name.getPath());

        Path path = output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(KingdomKeys.MODID +"/banner_pattern/" + name.getPath() + ".json");

        return DataProvider.saveStable(cache, json, path);
    }

    @Override
    public String getName() {
        return "KingdomKeys Banner Pattern JSONs";
    }
}