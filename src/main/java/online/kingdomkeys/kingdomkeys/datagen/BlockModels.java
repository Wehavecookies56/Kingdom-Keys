package online.kingdomkeys.kingdomkeys.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class BlockModels extends BlockModelProvider {
    public BlockModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, KingdomKeys.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }

    @Override
    public String getName() {
        return null;
    }
}
