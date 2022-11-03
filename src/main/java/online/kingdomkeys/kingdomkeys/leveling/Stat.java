package online.kingdomkeys.kingdomkeys.leveling;

import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class Stat {

    String name;
    int value;
    List<StatModifier> modifiers;

    public Stat(String name, int value) {
        this.name = name;
        this.value = value;
        modifiers = new ArrayList<>();
    }

    public Stat addModifier(String name, int amount, boolean stackable) {
        addModifier(new StatModifier(name, amount, stackable));
        return this;
    }

    //add modifier if it doesn't exist, change the amount otherwise
    protected void addModifier(StatModifier modifier) {
        if (modifiers.stream().noneMatch(m -> m.name.equals(modifier.name))) {
            modifiers.add(modifier);
        } else {
            modifiers.forEach(m -> {
                if (m.name.equals(modifier.name)) {
                    if (m.stackable) {
                        m.amount += modifier.amount;
                    } else {
                        m.amount = modifier.amount;
                    }
                }
            });
        }
    }

    public boolean hasModifier(String name) {
        return modifiers.stream().anyMatch(m -> m.name.equals(name));
    }

    public void removeModifier(String name) {
        if (modifiers.stream().anyMatch(m -> m.name.equals(name))) {
            modifiers.removeIf(modifier -> modifier.name.equals(name));
        }
    }

    //get value with modifiers applied
    public int getStat() {
        int modified = value;
        for (StatModifier modifier : modifiers) {
            modified += modifier.amount;
        }
        return modified;
    }

    public int get() {
        return value;
    }

    public void set(int value) {
        this.value = value;
    }

    public void add(int value) {
        this.value += value;
    }

    public CompoundTag serialize(CompoundTag tag) {
        tag.putInt(name, value);
        tag.putInt(name + "_modifiers", modifiers.size());
        CompoundTag modifierTag = new CompoundTag();
        for (int i = 0; i < modifiers.size(); ++i) {
            modifierTag.put("modifier_" + i, modifiers.get(i).serialize());
        }
        tag.put(name + "_modifiers_list", modifierTag);
        return tag;
    }

    public static Stat deserializeNBT(String name, CompoundTag tag) {
        Stat stat = new Stat(name, tag.getInt(name));
        CompoundTag modifierTag = tag.getCompound(name + "_modifiers_list");
        for (int i = 0; i < tag.getInt(name + "_modifiers"); ++i) {
            stat.addModifier(StatModifier.deserialize(modifierTag.getCompound("modifier_" + i)));
        }
        return stat;
    }

}
