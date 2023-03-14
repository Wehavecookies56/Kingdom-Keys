package online.kingdomkeys.kingdomkeys.datagen.builder;

import java.util.ArrayList;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.datagen.init.KeybladeStats;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeLevel;
import online.kingdomkeys.kingdomkeys.synthesis.material.ModMaterials;

public class KeybladeBuilder<T extends KeybladeBuilder<T>> extends ModelFile {

    private ResourceLocation keychain;
    private int baseStr, baseMag;
    private String desc;
    private String baseAbility;
    private float reach;
    private ArrayList<KeybladeLevel> keybladeLevels = new ArrayList<>();

    public KeybladeBuilder(Object o, Object o1) {
        super((ResourceLocation) o);
    }

    private T self() {
        return (T) this;
    }

    public T keychain(String keyChain) {
        Preconditions.checkNotNull(keyChain, "Texture must not be null");
        ResourceLocation asLoc;
        if (keyChain.contains(":")) {
            asLoc = new ResourceLocation(keyChain);
        } else {
            asLoc = new ResourceLocation(getLocation().getNamespace(), keyChain);
        }
        return keychain(asLoc);
    }

    public T keychain(ResourceLocation keychain) {
        Preconditions.checkNotNull(keychain, "Keychain must not be null");
        this.keychain = keychain;
        return self();
    }

    public T baseStats(int baseStr, int baseMag) {
        this.baseMag = baseMag;
        this.baseStr = baseStr;
        return self();
    }

    public T level(KeybladeLevel keybladeLevel) {
        keybladeLevels.add(keybladeLevel);
        return self();
    }

    public T levels(KeybladeStats.Recipe[] recipes) {
        int baseMag = this.baseMag;
        int baseStr = this.baseStr;
        for (int i = 0; i < 10; i++) {
            if (i % 2 == 0) {
                ++baseMag;
            } else {
                ++baseStr;
            }
            keybladeLevels.add(new KeybladeLevel.KeybladeLevelBuilder().withStats(baseStr, baseMag).withMaterials(recipes[i]).build());
        }
        return self();
    }

    public T desc(String desc) {
        this.desc = desc;
        return self();
    }

    public T ability(String ability) {
        this.baseAbility = ability;
        return self();
    }

    public T reach(float reach) {
    	this.reach = reach;
    	return self();
    }
    
    @Override
    protected boolean exists() {
        return true;
    }

    @VisibleForTesting
    public JsonObject toJson() {
        JsonObject root = new JsonObject();
        JsonObject baseStat = new JsonObject();
        JsonArray levels = new JsonArray();
        root.addProperty("ability", baseAbility);
        root.addProperty("reach", reach);
        
        if (this.keychain != null) {
            root.addProperty("keychain", this.keychain.toString());
        }
        
        // base stat
        baseStat.addProperty("str", baseStr);
        baseStat.addProperty("mag", baseMag);
        root.add("base_stats", baseStat);

        for (KeybladeLevel k : keybladeLevels) {
            JsonObject obj1 = new JsonObject();
            levels.add(obj1);
            obj1.addProperty("str", k.getStrength());
            obj1.addProperty("mag", k.getMagic());
            JsonArray recipe = new JsonArray();
            if (k.getMaterialList() != null)
               k.getMaterialList().forEach((key, value) -> {
                   JsonObject matObj = new JsonObject();
                   matObj.addProperty("material", key.getRegistryName().toString());
                   matObj.addProperty("quantity", value);
                   recipe.add(matObj); });
            obj1.add("recipe", recipe);
            if (k.getAbility() != null)
                obj1.addProperty("ability", k.getAbility());

        }
        root.add("levels", levels);
        root.addProperty("description", this.desc);
        return root;
    }
}
