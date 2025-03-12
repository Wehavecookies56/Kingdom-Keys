package online.kingdomkeys.kingdomkeys.datagen.provider;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.datagen.builder.KeybladeBuilder;

public abstract class KeybladeProvider implements DataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    protected final DataGenerator generator;
    protected final String modid;
    protected final Function<ResourceLocation, KeybladeBuilder> factory;
    @VisibleForTesting
    public final Map<ResourceLocation, KeybladeBuilder> generatedModels = new HashMap<>();
    @VisibleForTesting
    public final ExistingFileHelper existingFileHelper;

    public KeybladeProvider(DataGenerator generator, String modid, Function<ResourceLocation, KeybladeBuilder> factory, ExistingFileHelper existingFileHelper) {
        this.generator = generator;
        this.modid = modid;
        this.existingFileHelper = existingFileHelper;
        this.factory = factory;
    }
    public KeybladeProvider(DataGenerator generator, String modid, BiFunction<ResourceLocation, ExistingFileHelper, KeybladeBuilder> builderFromModId, ExistingFileHelper existingFileHelper) {
        this(generator, modid, loc->builderFromModId.apply(loc, existingFileHelper), existingFileHelper);
    }
    protected abstract void registerKeyblades();

    public KeybladeBuilder getBuilder(String path) {
        Preconditions.checkNotNull(path, "Path must not be null");
        ResourceLocation outputLoc = path.contains(":") ? new ResourceLocation(path) : new ResourceLocation(modid, path);
        return generatedModels.computeIfAbsent(outputLoc, factory);
    }

    protected void clear() {
        generatedModels.clear();
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        clear();
        registerKeyblades();
        return generateAll(cache);
    }

    protected CompletableFuture<?> generateAll(CachedOutput cache) {
        List<CompletableFuture<?>> list = new ArrayList<>();
        for (KeybladeBuilder model : generatedModels.values()) {
            Path target = getPath(model);
            list.add(DataProvider.saveStable(cache, model.toJson(), target));
        }
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    private Path getPath(KeybladeBuilder model) {
        ResourceLocation loc = model.getLocation();
        return generator.getPackOutput().createPathProvider(PackOutput.Target.DATA_PACK, "keyblades").json(loc);
    }
}