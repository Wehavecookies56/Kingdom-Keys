package online.kingdomkeys.kingdomkeys.item;

public enum KKResistanceType {
    light, darkness, fire, ice, lightning;
    private int resPercent;

    public int getResPercent() {
        return resPercent;
    }

    public KKResistanceType setResPercent(int resPercent) {
        this.resPercent = resPercent;
        return this;
    }
}
