package online.kingdomkeys.kingdomkeys.datagen.provider;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.AdvancementProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;

import java.io.IOException;
import java.nio.file.Path;

public class KKAdvancementProvider extends AdvancementProvider {

    DataGenerator generator;

    public KKAdvancementProvider(DataGenerator generatorIn) {
        super(generatorIn);
        generator = generatorIn;
    }

    public void act(DirectoryCache cache) throws IOException {
    }

    // used for logger. will be removed when advancements are done.
    public String getName() {
        return "KingdomKeys Advancements";
    }

}
