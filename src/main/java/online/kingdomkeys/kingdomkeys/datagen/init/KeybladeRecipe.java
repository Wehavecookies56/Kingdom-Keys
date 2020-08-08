package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.datagen.builder.KeybladeRecipeBuilder;
import online.kingdomkeys.kingdomkeys.datagen.provider.KeybladeRecipeProvider;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class KeybladeRecipe extends KeybladeRecipeProvider {
    public KeybladeRecipe(DataGenerator generator,ExistingFileHelper existingFileHelper) {
        super(generator, KingdomKeys.MODID, KeybladeRecipeBuilder::new, existingFileHelper);
    }
    private static class Recipe {
        private List<Map.Entry<String, Integer>> recipe = new ArrayList<>();

        public Recipe() { }

        public Recipe addMaterial(String mat, int quantity) {
            recipe.add(Pair.of(mat, quantity));
            return this;
        }

        public Map<Material, Integer> asMap() {
            Map<Material, Integer> matMap = new HashMap<>();
            recipe.forEach(p -> matMap.put(GameRegistry.findRegistry(Material.class).getValue(new ResourceLocation(KingdomKeys.MODID + ":" + Strings.SM_Prefix + p.getKey())), p.getValue()));
            return matMap;
        }
    }

    @Override
    protected void registerKeyblades() {

    }
}
