package online.kingdomkeys.kingdomkeys.api.item;

public enum ItemCategory {
    CONSUMABLE(0, 40), TOOL(0, 60), BUILDING(0, 80), EQUIPMENT(0, 100), MISC(0, 120), ACCESSORIES(0, 140);

    private int u, v;
    ItemCategory(int u, int v) {
        this.u = u;
        this.v = v;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

}
