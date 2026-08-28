package online.kingdomkeys.kingdomkeys.datagen.builder;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.particles.ModParticles;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.datagen.init.KeybladeStats;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class KeybladeBuilder extends ModelFile {

    private ResourceLocation keychain;
    private int baseStr, baseMag;
    private String desc;
    private ResourceLocation baseAbility;
    private float reach;
    private SoundEvent sound;
    private final ArrayList<ResourceLocation> hitParticles = new ArrayList<>();
    private final ArrayList<KeybladeLevel> keybladeLevels = new ArrayList<>();

    public KeybladeBuilder(Object o, Object o1) {
        super((ResourceLocation) o);
    }

    private KeybladeBuilder self() {
        return (KeybladeBuilder) this;
    }

    public KeybladeBuilder keychain(String keyChain) {
        Preconditions.checkNotNull(keyChain, "Texture must not be null");
        ResourceLocation asLoc;
        if (keyChain.contains(":")) {
            asLoc = KingdomKeys.rl(keyChain);
        } else {
            asLoc = KingdomKeys.rl(getLocation().getNamespace(), keyChain);
        }
        return keychain(asLoc);
    }

    public KeybladeBuilder keychain(ResourceLocation keychain) {
        Preconditions.checkNotNull(keychain, "Keychain must not be null");
        this.keychain = keychain;
        return self();
    }

    public KeybladeBuilder baseStats(int baseStr, int baseMag) {
        this.baseMag = baseMag;
        this.baseStr = baseStr;
        return self();
    }

    public KeybladeBuilder level(KeybladeLevel keybladeLevel) {
        keybladeLevels.add(keybladeLevel);
        return self();
    }

    public KeybladeBuilder levels(KeybladeStats.Recipe[] recipes) {
        int baseMag = this.baseMag;
        int baseStr = this.baseStr;
        for (int i = 0; i < recipes.length; i++) {
            if (i % 2 == 0) {
                ++baseMag;
            } else {
                ++baseStr;
            }
            keybladeLevels.add(new KeybladeLevel.KeybladeLevelBuilder().withStats(baseStr, baseMag).withMaterials(recipes[i]).build());
        }
        return self();
    }

    public KeybladeBuilder desc(String desc) {
        this.desc = desc;
        return self();
    }

    public KeybladeBuilder ability(ResourceLocation ability) {
        this.baseAbility = ability;
        return self();
    }

    public KeybladeBuilder reach(float reach) {
        this.reach = reach;
        return self();
    }

    public KeybladeBuilder sound(SoundEvent sound) {
        this.sound = sound;
        return self();
    }

    public KeybladeBuilder hitParticles(ResourceLocation... particles) {
        this.hitParticles.clear();
        this.hitParticles.addAll(Arrays.asList(particles));
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
        if (baseAbility != null) {
            root.addProperty("ability", baseAbility.toString());
        }
        root.addProperty("reach", reach);
	    root.addProperty("sound", BuiltInRegistries.SOUND_EVENT.getKey(Objects.requireNonNullElseGet(sound, ModSounds.generic_hit::get)).toString());

        JsonArray particles = new JsonArray();
        if (hitParticles.isEmpty()) {
            particles.add(ModParticles.GENERIC_HIT.toString());
        } else {
            hitParticles.forEach(particle -> particles.add(particle.toString()));
        }
        root.add("hit_particles", particles);
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
                   matObj.addProperty("material", BuiltInRegistries.ITEM.getKey(key).toString());
                   matObj.addProperty("quantity", value);
                   recipe.add(matObj);
               });
            obj1.add("recipe", recipe);
            if (k.getAbility() != null)
                obj1.addProperty("ability", k.getAbility().toString());

        }
        root.add("levels", levels);
        root.addProperty("description", this.desc);
        return root;
    }
}
