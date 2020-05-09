package online.kingdomkeys.kingdomkeys.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.synthesis.KeybladeSynthLevelMap;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeLevel;

public class KeybladeStats extends  KeyBladeProvider{
    public KeybladeStats(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, KingdomKeys.MODID, KEYBLADE_FOLDER, KeybladeBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerKeyblades() {
        KeybladeSynthLevelMap kslm = new KeybladeSynthLevelMap();
        kslm.init();

        getBuilder(Strings.ultimaWeaponKH3).keychain(Strings.ultimaWeaponKH3Chain).baseStats(10,8).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(13).withMag(10).withAbilty("Test_Abiltity")
                .withMaterials(kslm.getMap("kh3ultLvl1")).build()).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(16).withMag(12).withMaterials(kslm.getMap("kh3ultLvl2")).build()).desc("test");


    }

    @Override
    public String getName() {
        return "Keyblade json";
    }
}
