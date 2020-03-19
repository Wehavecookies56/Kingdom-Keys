package online.kingdomkeys.kingdomkeys.datagen;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.util.Pair;
import net.minecraft.resources.ResourcePackType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeLevel;
import online.kingdomkeys.kingdomkeys.synthesis.material.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeybladeBuilder<T extends KeybladeBuilder<T>> extends ModelFile {

    protected final ExistingFileHelper existingFileHelper;
    private ResourceLocation keychain;
    private int baseStr, baseMag;
    private String desc;
    private List<KeybladeLevel> keybladeLevels = new ArrayList();
    public KeybladeBuilder(ResourceLocation outputLocation, ExistingFileHelper existingFileHelper) {
        super(outputLocation);
        this.existingFileHelper = existingFileHelper;
    }
    private T self() { return (T) this; }

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
        Preconditions.checkNotNull(keychain, "Texture must not be null");
        Preconditions.checkArgument(existingFileHelper.exists(keychain, ResourcePackType.CLIENT_RESOURCES, ".png", "textures"),
                "Texture %s does not exist in any known resource pack", keychain);
        this.keychain = keychain;
        return self();
    }

    public T baseStats(int baseStr, int baseMag)
    {
        this.baseMag = baseMag;
        this.baseStr = baseStr;
        return self();
    }

    public T level(KeybladeLevel keybladeLevel)
    {
        keybladeLevels.add(keybladeLevel);
        return self();
    }

    public T desc(String desc)
    {
        this.desc = desc;
        return self();
    }
    public
    String serializeLoc(ResourceLocation loc) {
        if (loc.getNamespace().equals("minecraft")) {
            return loc.getPath();
        }
        return loc.toString();
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
        if (this.keychain != null) {
            JsonObject textures = new JsonObject();
            textures.addProperty("keychain", keychain.toString());
            root.add("textures", textures);
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
            for (Pair<Material, Integer> m: k.getMaterialList()) {
                JsonObject matObj = new JsonObject();
                matObj.addProperty("material", m.getKey().toString());
                matObj.addProperty("quantity", m.getValue());
                recipe.add(matObj);
            }
            obj1.add("recipe",recipe);
            if(k.getAbility() != null)
                obj1.addProperty("ability", k.getAbility());

        }
        root.add("levels", levels);
        root.addProperty("description", this.desc);
        return root;
    }
}
