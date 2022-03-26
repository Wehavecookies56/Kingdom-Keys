package online.kingdomkeys.kingdomkeys.datagen.provider;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.advancements.AdvancementProvider;

public class KKAdvancementProvider extends AdvancementProvider {

    DataGenerator generator;

    public KKAdvancementProvider(DataGenerator generatorIn) {
        super(generatorIn);
        generator = generatorIn;
    }



    public void run(HashCache cache) { }

    // used for logger. will be removed when advancements are done.
    public String getName() {
        return "KingdomKeys Advancements";
    }

}
