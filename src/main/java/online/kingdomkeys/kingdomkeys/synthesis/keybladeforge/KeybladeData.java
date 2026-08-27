package online.kingdomkeys.kingdomkeys.synthesis.keybladeforge;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.item.KeychainItem;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Stores the data loaded from the keyblades datapack
 */
public class KeybladeData {

    //The keychain that summons the keyblade this is for, if null the upgrade levels are ignored and only base stats are used
    @Nullable
	public KeychainItem keychain;
    //The level 0 stats
    int baseStrength, baseMagic;
    ResourceLocation baseAbility;
    float reach;
    SoundEvent sound = ModSounds.generic_hit.get();
    float critChance;
    // Empty by default, so a keyblade that says nothing about particles simply throws none
    List<ResourceLocation> hitParticles = List.of();
    // How wide each one gets at the middle of its life, in blocks
    float hitParticleScale = 0.35F;
    //List of upgrades for the keyblade
    @Nullable List<KeybladeLevel> levels;
    //Description for the tooltip of the keyblade
    String description;

    public KeybladeData() {

    }

    public KeybladeData(@Nullable KeychainItem keychain, @Nullable List<KeybladeLevel> levels, String description, int baseStrength, int baseMagic) {
        this.keychain = keychain;
        this.levels = levels;
        this.description = description;
        this.baseStrength = baseStrength;
        this.baseMagic = baseMagic;
    }

    public int getMaxLevel(){
        return levels.size();
    }
    //Returns the base strength if level is 0
    public int getStrength(int level) {
        return level == 0 ? baseStrength : levels.get(level-1).getStrength();
    }

    //Returns the base magic if level is 0
    public int getMagic(int level) {
        return level == 0 ? baseMagic : levels.get(level-1).getMagic();
    }
    
    //Returns the base ability if level is 0
    public ResourceLocation getLevelAbility(int level) {
        return level == 0 ? baseAbility : levels.get(level-1).getAbility();
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setKeychain(Item keychain) {
    	if(keychain instanceof KeychainItem) {
    		this.keychain = (KeychainItem) keychain;
    	} else {
    		this.keychain = null;
    	}
    }
    
    public void setBaseAbility(ResourceLocation ability) {
    	this.baseAbility = ability;
    }
    
    public ResourceLocation getBaseAbility() {
    	return baseAbility;
    }

    public KeychainItem getKeychain() {
    	return keychain;
    }
    
    public void setLevels(List<KeybladeLevel> levels) {
        this.levels = levels;
    }

    public KeybladeLevel getLevelData(int level) {
        return levels.get(level);
    }

    public void setBaseMagic(int baseMagic) {
        this.baseMagic = baseMagic;
    }

    public void setBaseStrength(int baseStrength) {
        this.baseStrength = baseStrength;
    }
    
    public float getReach() {
        return reach;
    }
    
    public void setReach(float reach) {
        this.reach = reach;
    }

    public SoundEvent getSound() {
        return sound;
    }

    public void setSound(String sound) {
        ResourceLocation loc = ResourceLocation.parse(sound);
        this.sound = BuiltInRegistries.SOUND_EVENT.getOptional(loc).orElseThrow(() -> new IllegalArgumentException("Unknown sound event: " + loc));
    }

    public void setCritChance(float critChance){this.critChance = critChance;}

    public float getCritChance() { return critChance;}

    public List<ResourceLocation> getHitParticles() {
        return hitParticles;
    }

    public void setHitParticles(List<ResourceLocation> hitParticles) {
        this.hitParticles = hitParticles == null ? List.of() : List.copyOf(hitParticles);
    }

    public float getHitParticleScale() {
        return hitParticleScale;
    }

    public void setHitParticleScale(float hitParticleScale) {
        this.hitParticleScale = hitParticleScale;
    }
}
