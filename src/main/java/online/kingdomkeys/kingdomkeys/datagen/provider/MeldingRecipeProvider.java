package online.kingdomkeys.kingdomkeys.datagen.provider;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.MeldingRecipeBuilder;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class MeldingRecipeProvider<T extends MeldingRecipeBuilder> implements DataProvider {

    protected final DataGenerator generator;
    protected final String modid;
    protected final Function<ResourceLocation, T> factory;
    @VisibleForTesting
    public final Map<ResourceLocation, T> generatedModels = new HashMap<>();
    @VisibleForTesting
    public final ExistingFileHelper existingFileHelper;

    public MeldingRecipeProvider(DataGenerator generator, String modid, Function<ResourceLocation, T> factory, ExistingFileHelper existingFileHelper) {
        this.generator = generator;
        this.modid = modid;
        this.existingFileHelper = existingFileHelper;
        this.factory = factory;
    }

    protected abstract void registerRecipe();

    public T getBuilder(String path) {
        Preconditions.checkNotNull(path, "Path must not be null");
        ResourceLocation outputLoc = path.contains(":") ? KingdomKeys.rl(path) : KingdomKeys.rl(modid, path);
        return generatedModels.computeIfAbsent(outputLoc, factory);
    }

    protected void clear() {
        generatedModels.clear();
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        clear();
        registerRecipe();
        return generateAll(cache);
    }

    @Override
    public String getName() {
        return "Melding Recipes";
    }

    protected CompletableFuture<?> generateAll(CachedOutput cache) {
        List<CompletableFuture<?>> list = new ArrayList<>();
        for (T model : generatedModels.values()) {
            Path target = getPath(model);
            list.add(DataProvider.saveStable(cache, model.toJson(), target));
        }
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    private Path getPath(T model) {
        ResourceLocation loc = model.getLocation();
        return generator.getPackOutput().createPathProvider(PackOutput.Target.DATA_PACK, "melding").json(loc);
    }
}