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
        getBuilder("bounce_blox").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/bounce_blox");
        getBuilder("hard_blox").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/hard_blox");
        getBuilder("metal_blox").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/metal_blox");
        getBuilder("ghost_blox_invisible").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/ghost_blox_invisible");
        getBuilder("ghost_blox_visible").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/ghost_blox_visible");
        getBuilder("danger_blox").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/danger_blox");
        getBuilder("normal_blox").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/normal_blox");
        getBuilder("prize_blox").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/prize_blox");
        getBuilder("rare_prize_blox").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/rare_prize_blox");
        getBuilder("blast_blox").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/blast_blox");

        //ore
        getBuilder("blazing_ore").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/blazing_ore");
        getBuilder("blazing_ore_n").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/blazing_ore_n");
        getBuilder("bright_ore").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/bright_ore");
        getBuilder("dark_ore").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/dark_ore");
        getBuilder("dark_ore_e").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/dark_ore_e");
        getBuilder("dark_ore_n").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/dark_ore_n");
        getBuilder("dense_ore").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/dense_ore");
        getBuilder("energy_ore").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/energy_ore");
        getBuilder("energy_ore_n").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/energy_ore_n");
        getBuilder("frost_ore").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/frost_ore");
        getBuilder("lightning_ore").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/lightning_ore");
        getBuilder("lucid_ore").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/lucid_ore");
        getBuilder("power_ore").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/power_ore");
        getBuilder("power_ore_e").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/power_ore_e");
        getBuilder("remembrance_ore").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/remembrance_ore");
        getBuilder("serenity_ore").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/serenity_ore");
        getBuilder("stormy_ore").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/stormy_ore");
        getBuilder("tranquil_ore").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/tranquil_ore");
        getBuilder("twilight_ore").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/twilight_ore");
        getBuilder("twilight_ore_n").parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all","block/twilight_ore_n");

    }

    @Override
    public String getName() {
        return "Block Models";
    }
}
