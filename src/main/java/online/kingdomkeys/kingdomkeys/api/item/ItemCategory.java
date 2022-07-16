package online.kingdomkeys.kingdomkeys.api.item;

public enum ItemCategory {
	TOOL(0, 60),
	EQUIPMENT(0, 100),
    CONSUMABLE(0, 40),
    BUILDING(0, 80),
    MISC(0, 120),
    ACCESSORIES(0, 140),
    SHOTLOCK(0,160), ;

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
