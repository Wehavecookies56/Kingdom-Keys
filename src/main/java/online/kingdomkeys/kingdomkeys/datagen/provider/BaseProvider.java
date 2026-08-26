package online.kingdomkeys.kingdomkeys.datagen.provider;

import com.google.common.base.Preconditions;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.BuilderBase;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class BaseProvider<T extends BuilderBase> implements DataProvider {

    protected final DataGenerator generator;
    protected final String modid, folder;
    public final List<T> generated = new ArrayList<>();

    public BaseProvider(DataGenerator generator, String modid, String folder) {
        this.generator = generator;
        this.modid = modid;
        this.folder = folder;
    }

    protected abstract void build();

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cachedOutput) {
        generated.clear();
        build();
        List<CompletableFuture<?>> list = new ArrayList<>();
        for (T builder : generated) {
            Path target = getPath(builder);
            list.add(DataProvider.saveStable(cachedOutput, builder.build(), target));
        }
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }

    protected Path getPath(T builder) {
        return generator.getPackOutput().createPathProvider(PackOutput.Target.DATA_PACK, folder).json(builder.getLocation());
    }

    protected ResourceLocation getLocation(String path) {
        Preconditions.checkNotNull(path, "Path must not be null");
        return path.contains(":") ? KingdomKeys.rl(path) : KingdomKeys.rl(modid, path);
    }

    protected T addBuilder(T builder) {
        generated.add(builder);
        return builder;
    }

}
