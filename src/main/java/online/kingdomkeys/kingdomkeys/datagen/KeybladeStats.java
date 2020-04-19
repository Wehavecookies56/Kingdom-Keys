package online.kingdomkeys.kingdomkeys.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeLevel;

public class KeybladeStats extends  KeyBladeProvider{
    public KeybladeStats(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, KingdomKeys.MODID, KEYBLADE_FOLDER, KeybladeBuilder::new, existingFileHelper);
    }

    @Override
    protected void registerKeyblades() {
        getBuilder(Strings.ultimaWeaponKH3).keychain(Strings.kingdomKeyChain).baseStats(2,4).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(5).withMag(4).withAbilty("Test_Abiltity").build()).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(6).withMag(5).build()).desc("test");
    }

    @Override
    public String getName() {
        return "Keyblade json";
    }
}
