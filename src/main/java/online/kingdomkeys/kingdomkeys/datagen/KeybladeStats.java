package online.kingdomkeys.kingdomkeys.datagen;

import javafx.util.Pair;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeLevel;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeybladeStats extends  KeyBladeProvider{
    public KeybladeStats(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, KingdomKeys.MODID, KeybladeBuilder::new, existingFileHelper);
    }

    @Override
    public String getName() {
        return "keyblade json";
    }


    @Override
    protected void registerKeyblades() {
        getBuilder(Strings.ultimaWeaponKH3).keychain(Strings.kingdomKeyChain).baseStats(2,4).level( new KeybladeLevel.KeybladeLevelBuilder()
                .withStr(5).withMag(4).withMaterials(new ArrayList<Pair<Material, Integer>>() {}).build());
    }
}
