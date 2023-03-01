package online.kingdomkeys.kingdomkeys.datagen.provider;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.datagen.builder.KeybladeBuilder;

public abstract class KeybladeProvider<T extends KeybladeBuilder<T>> implements DataProvider {

    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    protected final DataGenerator generator;
    protected final String modid;
    protected final Function<ResourceLocation, T> factory;
    @VisibleForTesting
    public final Map<ResourceLocation, T> generatedModels = new HashMap<>();
    @VisibleForTesting
    public final ExistingFileHelper existingFileHelper;

    public KeybladeProvider(DataGenerator generator, String modid, Function<ResourceLocation, T> factory, ExistingFileHelper existingFileHelper) {
        this.generator = generator;
        this.modid = modid;
        this.existingFileHelper = existingFileHelper;
        this.factory = factory;
    }
    public KeybladeProvider(DataGenerator generator, String modid, BiFunction<ResourceLocation, ExistingFileHelper, T> builderFromModId, ExistingFileHelper existingFileHelper) {
        this(generator, modid, loc->builderFromModId.apply(loc, existingFileHelper), existingFileHelper);
    }
    protected abstract void registerKeyblades();

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
        registerKeyblades();
        generateAll(cache);
    }

    protected void generateAll(HashCache cache) {
        for (T model : generatedModels.values()) {
            Path target = getPath(model);
            try {
                save(GSON, cache, model.toJson(), target);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("deprecation")
    static void save(Gson pGson, HashCache pCache, JsonElement pJsonElement, Path pPath) throws IOException {
        String s = pGson.toJson(pJsonElement);
        s = JavaUnicodeEscaper.outsideOf(0, 0x7f).translate(s);
        String s1 = SHA1.hashUnencodedChars(s).toString();
        if (!Objects.equals(pCache.getHash(pPath), s1) || !Files.exists(pPath)) {
            Files.createDirectories(pPath.getParent());
            BufferedWriter bufferedwriter = Files.newBufferedWriter(pPath);

            try {
                bufferedwriter.write(s);
            } catch (Throwable throwable1) {
                if (bufferedwriter != null) {
                    try {
                        bufferedwriter.close();
                    } catch (Throwable throwable) {
                        throwable1.addSuppressed(throwable);
                    }
                }

                throw throwable1;
            }

            if (bufferedwriter != null) {
                bufferedwriter.close();
            }
        }

        pCache.putNew(pPath, s1);
    }

    private Path getPath(T model) {
        ResourceLocation loc = model.getLocation();
        return generator.getOutputFolder().resolve("data/" + loc.getNamespace() + "/keyblades/" + loc.getPath() + ".json");
    }
}