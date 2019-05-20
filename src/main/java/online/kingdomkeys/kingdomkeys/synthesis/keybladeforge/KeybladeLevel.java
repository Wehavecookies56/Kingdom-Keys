package online.kingdomkeys.kingdomkeys.synthesis.keybladeforge;

import java.util.Map;

import online.kingdomkeys.kingdomkeys.synthesis.material.Material;

/**
 * Class to contain keyblade level information
 */
public class KeybladeLevel {

    //The stats when upgraded to this level
    int strength;
    int magic;
    //The materials required to upgrade to this level, key is the material, value is the quantity
    Map<Material, Integer> materials;
    //The ability gained when upgrading to this level
    //TODO ability system and potentially multiple abilities here
    String ability;

    public KeybladeLevel() {

    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public void setMaterials(Map<Material, Integer> materials) {
        this.materials = materials;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getMagic() {
        return magic;
    }

    public int getStrength() {
        return strength;
    }

    public KeybladeLevel(int strength, int magic, Map<Material, Integer> materials, String ability) {
        this.strength = strength;
        this.magic = magic;
        this.materials = materials;
        this.ability = ability;
    }

    @Override
    public String toString() {
        return String.format("KeybladeLevel[strength:%d, magic:%d, materials[%d], ability:%s]", strength, magic, materials.size(), ability);
    }
}
