package online.kingdomkeys.kingdomkeys.leveling;

import net.minecraft.nbt.CompoundTag;

public class StatModifier {

    //name to identify
    String name;
    //amount to modify by
    int amount;
    //whether to combine the amount or change the amount if duplicate is added
    boolean stackable;

    protected StatModifier(String name, int amount, boolean stackable) {
        this.name = name;
        this.amount = amount;
        this.stackable = stackable;
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", this.name);
        tag.putInt("amount", this.amount);
        tag.putBoolean("stackable", this.stackable);
        return tag;
    }

    public static StatModifier deserialize(CompoundTag tag) {
        return new StatModifier(tag.getString("name"), tag.getInt("amount"), tag.getBoolean("stackable"));
    }
}
