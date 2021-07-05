package online.kingdomkeys.kingdomkeys.datagen.builder;

import java.util.ArrayList;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelFile;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeLevel;

public class KeybladeBuilder<T extends KeybladeBuilder<T>> extends ModelFile {

    private ResourceLocation keychain;
    private int baseStr, baseMag;
    private String desc;
    private String baseAbility;
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

    public T desc(String desc) {
        this.desc = desc;
        return self();
    }

    public T ability(String ability) {
        this.baseAbility = ability;
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
