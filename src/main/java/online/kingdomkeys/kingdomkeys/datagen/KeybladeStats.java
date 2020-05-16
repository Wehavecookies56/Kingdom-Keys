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
        kh3Keyblades(kslm);
        kh1Keyblades(kslm);
        kh2Keyblades(kslm);
        bbsKeyblades(kslm);
        dddKeyblades(kslm);
        uxKeyblades(kslm);


    }

    public void kh3Keyblades(KeybladeSynthLevelMap kslm)
    {
        getBuilder(Strings.ultimaWeaponKH3).keychain(Strings.ultimaWeaponKH3Chain).baseStats(10,8).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(13).withMag(10).withAbilty("Test_Abiltity")
                .withMaterials(kslm.getMap("kh3ultLvl1")).build()).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(16).withMag(12).withMaterials(kslm.getMap("kh3ultLvl2")).build()).desc("test");
    }

    public void kh1Keyblades(KeybladeSynthLevelMap kslm)
    {
        getBuilder(Strings.kingdomKey).keychain(Strings.kingdomKeyChain).baseStats(3,0).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(3).withMag(3).withAbilty("Test_Abiltity")
                .withMaterials(kslm.getMap(Strings.kingdomKey + "_Lvl1")).build()).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(4).withMag(4).withMaterials(kslm.getMap(Strings.kingdomKey + "_Lvl2")).build()).desc("test");
    }

    public void kh2Keyblades(KeybladeSynthLevelMap kslm)
    {
        getBuilder(Strings.kingdomKeyD).keychain(Strings.kingdomKeyDChain).baseStats(3,0).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(3).withMag(3).withAbilty("Test_Abiltity")
                .withMaterials(kslm.getMap(Strings.kingdomKeyD + "_Lvl1")).build()).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(4).withMag(4).withMaterials(kslm.getMap(Strings.kingdomKeyD + "_Lvl2")).build()).desc("test");

    }

    public void bbsKeyblades(KeybladeSynthLevelMap kslm)
    {

    }

    public void dddKeyblades(KeybladeSynthLevelMap kslm)
    {

    }

    public void uxKeyblades(KeybladeSynthLevelMap kslm)
    {

    }

    public void khdKeyblades(KeybladeSynthLevelMap kslm)
    {

    }

    @Override
    public String getName() {
        return "Keyblade json";
    }
}
