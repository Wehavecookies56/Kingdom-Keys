package online.kingdomkeys.kingdomkeys.leveling;

import net.minecraft.nbt.CompoundTag;

public class StatModifier {

    //name to identify
    String name;
    //amount to modify by
    double amount;
    //whether to combine the amount or change the amount if duplicate is added
    boolean stackable;
    //whether the amount is a percentage modifier
    boolean percentage;

    protected StatModifier(String name, double amount, boolean stackable, boolean percentage) {
        this.name = name;
        this.amount = amount;
        this.stackable = stackable;
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putString("name", this.name);
        tag.putDouble("amount", this.amount);
        tag.putBoolean("stackable", this.stackable);
        tag.putBoolean("percentage", this.percentage);
        return tag;
    }

    public static StatModifier deserialize(CompoundTag tag) {
        return new StatModifier(tag.getString("name"), tag.getDouble("amount"), tag.getBoolean("stackable"), tag.getBoolean("percentage"));
    }
}
