package online.kingdomkeys.kingdomkeys.datagen.provider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.HashCache;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;

public class KKAdvancementProvider extends AdvancementProvider {

    public KKAdvancementProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries, List<AdvancementSubProvider> pSubProviders) {
        super(pOutput, pRegistries, pSubProviders);
    }

    public void run(HashCache cache) { }

}
