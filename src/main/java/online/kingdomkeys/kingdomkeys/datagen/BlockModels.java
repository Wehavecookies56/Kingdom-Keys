package online.kingdomkeys.kingdomkeys.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class BlockModels extends BlockModelProvider {
    public BlockModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, KingdomKeys.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // blox
        standardCube("bounce_blox");
        standardCube("hard_blox");
        standardCube("metal_blox");
        standardCube("ghost_blox_invisible");
        standardCube("ghost_blox_visible");
        standardCube("danger_blox");
        standardCube("normal_blox");
        standardCube("prize_blox");
        standardCube("rare_prize_blox");
        standardCube("blast_blox");
        standardCube("magnet_blox_on");
        standardCube("magnet_blox_off");

        //ore
        standardCube("blazing_ore");
        standardCube("blazing_ore_n");
        standardCube("bright_ore");
        standardCube("dark_ore");
        standardCube("dark_ore_e");
        standardCube("dark_ore_n");
        standardCube("dense_ore");
        standardCube("energy_ore");
        standardCube("energy_ore_n");
        standardCube("frost_ore");
        standardCube("lightning_ore");
        standardCube("lucid_ore");
        standardCube("power_ore");
        standardCube("power_ore_e");
        standardCube("remembrance_ore");
        standardCube("serenity_ore");
        standardCube("stormy_ore");
        standardCube("tranquil_ore");
        standardCube("twilight_ore");
        standardCube("twilight_ore_n");
    }

    public void standardCube(String name) {
        getBuilder(name).parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/" + name);
    }

    @Override
    public String getName() {
        return "Block Models";
    }
}
