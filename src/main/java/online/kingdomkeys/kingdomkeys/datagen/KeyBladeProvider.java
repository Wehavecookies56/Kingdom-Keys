package online.kingdomkeys.kingdomkeys.datagen;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class KeyBladeProvider <T extends KeybladeBuilder<T>> implements IDataProvider {
    private class ExistingFileHelperIncludingGenerated extends ExistingFileHelper {
        private final ExistingFileHelper delegate;
        public ExistingFileHelperIncludingGenerated(ExistingFileHelper delegate) {
            super(Collections.emptyList(), true);
            this.delegate = delegate;
        }

        @Override
        public boolean exists(ResourceLocation loc, ResourcePackType type, String pathSuffix, String pathPrefix) {
            if (generatedModels.containsKey(loc)) {
                return true;
            }
            return delegate.exists(loc, type, pathSuffix, pathPrefix);
        }
    }
    public static final String KEYBLADE_FOLDER = "keyblade";

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
    protected final DataGenerator generator;
    protected final String modid;
    protected final String folder = KEYBLADE_FOLDER;
    protected final Function<ResourceLocation, T> factory;
    @VisibleForTesting
    public final Map<ResourceLocation, T> generatedModels = new HashMap<>();
    @VisibleForTesting
    public final ExistingFileHelper existingFileHelper;


    public KeyBladeProvider(DataGenerator generator,String modid,Function<ResourceLocation, T> factory, ExistingFileHelper existingFileHelper) {
        this.generator = generator;
        this.modid = modid;
        this.existingFileHelper = existingFileHelper;
        this.factory = factory;
    }
    protected abstract void registerKeyblades();

    public T getBuilder(String path) {
        Preconditions.checkNotNull(path, "Path must not be null");
        ResourceLocation outputLoc = extendWithFolder(path.contains(":") ? new ResourceLocation(path) : new ResourceLocation(modid, path));
        return generatedModels.computeIfAbsent(outputLoc, factory);
    }

    private ResourceLocation extendWithFolder(ResourceLocation rl) {
        if (rl.getPath().contains("/")) {
            return rl;
        }
        return new ResourceLocation(rl.getNamespace(), folder + "/" + rl.getPath());
    }

    public ResourceLocation modLoc(String name) {
        return new ResourceLocation(modid, name);
    }

    public ResourceLocation mcLoc(String name) {
        return new ResourceLocation(name);
    }

    public ModelFile.ExistingModelFile getExistingFile(ResourceLocation path) {
        ModelFile.ExistingModelFile ret = new ModelFile.ExistingModelFile(extendWithFolder(path), existingFileHelper);
        ret.assertExistence();
        return ret;
    }

    protected void clear() {
        generatedModels.clear();
    }

    @Override
    public void act(DirectoryCache cache) throws IOException {
        clear();
        registerKeyblades();
        generateAll(cache);
    }

    protected void generateAll(DirectoryCache cache) {
        for (T
                model : generatedModels.values()) {
            Path target = getPath(model);
            try {
                IDataProvider.save(GSON, cache, model.toJson(), target);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path getPath(T model) {
        ResourceLocation loc = model.getLocation();
        return generator.getOutputFolder().resolve("assets/" + loc.getNamespace() + "/models/" + loc.getPath() + ".json");
    }
}