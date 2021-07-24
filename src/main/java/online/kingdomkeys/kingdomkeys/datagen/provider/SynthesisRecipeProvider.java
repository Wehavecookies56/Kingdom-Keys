package online.kingdomkeys.kingdomkeys.datagen.provider;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.datagen.builder.SynthesisRecipeBuilder;

public abstract class SynthesisRecipeProvider<T extends SynthesisRecipeBuilder<T>> implements DataProvider {


    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    protected final DataGenerator generator;
    protected final String modid;
    protected final Function<ResourceLocation, T> factory;
    @VisibleForTesting
    public final Map<ResourceLocation, T> generatedModels = new HashMap<>();
    @VisibleForTesting
    public final ExistingFileHelper existingFileHelper;

    public SynthesisRecipeProvider(DataGenerator generator, String modid, Function<ResourceLocation, T> factory, ExistingFileHelper existingFileHelper) {
        this.generator = generator;
        this.modid = modid;
        this.existingFileHelper = existingFileHelper;
        this.factory = factory;
    }
    public SynthesisRecipeProvider(DataGenerator generator, String modid, BiFunction<ResourceLocation, ExistingFileHelper, T> builderFromModId, ExistingFileHelper existingFileHelper) {
        this(generator, modid, loc->builderFromModId.apply(loc, existingFileHelper), existingFileHelper);
    }
    protected abstract void registerRecipe();

    public T getBuilder(String path) {
        Preconditions.checkNotNull(path, "Path must not be null");
        ResourceLocation outputLoc = path.contains(":") ? new ResourceLocation(path) : new ResourceLocation(modid, path);
        return generatedModels.computeIfAbsent(outputLoc, factory);
    }

    protected void clear() {
        generatedModels.clear();
    }

    @Override
    public void run(HashCache cache) throws IOException {
        clear();
        registerRecipe();
        generateAll(cache);
    }

    @Override
    public String getName() {
        return "Recipes";
    }

    protected void generateAll(HashCache cache) {
        for (T model : generatedModels.values()) {
            Path target = getPath(model);
            try {
                DataProvider.save(GSON, cache, model.toJson(), target);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path getPath(T model) {
        ResourceLocation loc = model.getLocation();
        return generator.getOutputFolder().resolve("data/" + loc.getNamespace() + "/synthesis/" + loc.getPath() + ".json");
    }
}