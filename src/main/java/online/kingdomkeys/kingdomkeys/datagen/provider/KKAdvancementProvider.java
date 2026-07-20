package online.kingdomkeys.kingdomkeys.datagen.provider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.datagen.init.AdvancementsGen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class KKAdvancementProvider extends AdvancementProvider {

    public KKAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, existingFileHelper, List.of(new AdvancementsGen()));
    }

}
