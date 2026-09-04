package online.kingdomkeys.kingdomkeys.synthesis.shop;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public enum Currency implements StringRepresentable {
    MUNNY("munny", Strings.Gui_Menu_Main_Munny),
    LUX("lux", Strings.Gui_Menu_Main_Lux),
    HEARTS("hearts", Strings.Gui_Menu_Main_Hearts);

    private final String name;
    private final String translationKey;

    Currency(String name, String translationKey) {
        this.name = name;
        this.translationKey = translationKey;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public static Currency fromName(String name) {
        for (Currency currency : values()) {
            if (currency.name.equalsIgnoreCase(name)) {
                return currency;
            }
        }
        return MUNNY;
    }

    // Basically a getter based on the currency type
    public int held(PlayerData playerData) {
        return switch (this) {
            case LUX -> playerData.getLux();
            case HEARTS -> playerData.getHearts();
            case MUNNY -> playerData.getMunny();
        };
    }

    // Basically a setter based on the currency type
    public void charge(PlayerData playerData, int amount, ServerPlayer player) {
        switch (this) {
            case LUX -> playerData.addLux(-amount);
            case HEARTS -> playerData.removeHearts(amount);
            case MUNNY -> playerData.setMunny(playerData.getMunny() - amount, player);
        }
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
