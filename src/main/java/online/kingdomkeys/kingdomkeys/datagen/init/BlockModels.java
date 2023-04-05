package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class BlockModels extends BlockModelProvider {
	public BlockModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
		super(generator.getPackOutput(), KingdomKeys.MODID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		// blox
		standardCube("bounce_blox");
		standardCube("hard_blox");
		standardCube("metal_blox");
		standardCube("ghost_blox_invisible", "cutout");
		standardCube("ghost_blox_visible");
		standardCube("danger_blox");
		standardCube("normal_blox");
		standardCube("prize_blox");
		standardCube("rare_prize_blox");
		standardCube("blast_blox");
		//standardCube("magnet_blox_on"); //has special on state
		standardCube("magnet_blox_off");

		// ore
		standardCube("blazing_ore");
		standardCube("blazing_ore_n");
		standardCube("soothing_ore");
		standardCube("writhing_ore");
		standardCube("writhing_ore_e");
		standardCube("writhing_ore_n");
		standardCube("betwixt_ore");
		standardCube("wellspring_ore");
		standardCube("wellspring_ore_n");
		standardCube("frost_ore");
		standardCube("lightning_ore");
		standardCube("lucid_ore");
		standardCube("pulsing_ore");
		standardCube("pulsing_ore_e");
		standardCube("remembrance_ore");
		standardCube("hungry_ore");
		standardCube("sinister_ore");
		standardCube("stormy_ore");
		standardCube("tranquility_ore");
		standardCube("twilight_ore");
		standardCube("twilight_ore_n");
		
		standardCube("rod_sand");
		standardCube("rod_stone");
		standardCube("rod_cracked_stone");

		standardCube("castle_oblivion_wall", "cutout");
		standardCube("castle_oblivion_wall2", "cutout");
		standardCube("castle_oblivion_wall3", "cutout");
		standardCube("castle_oblivion_wall_chiseled", "cutout");
	}

	public void standardCube(String name) {
		getBuilder(name).parent(new ModelFile.UncheckedModelFile("block/cube_all")).texture("all", "block/" + name);
	}

	public void standardCube(String name, String type) {
		getBuilder(name).parent(new ModelFile.UncheckedModelFile("block/cube_all")).renderType(type).texture("all", "block/" + name);
	}

	@Override
	public String getName() {
		return "Block Models";
	}
}
