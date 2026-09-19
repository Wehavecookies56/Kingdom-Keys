package online.kingdomkeys.kingdomkeys.integration.epicfight.style;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.HandStyle;

import java.util.function.Predicate;

public final class KKFightingStyle {
    public static final Predicate<PlayerData> ALWAYS = playerData -> true;
    public static final Predicate<PlayerData> NEVER = playerData -> false;

    private final ResourceLocation id;
    private final HandStyle hand;
    private final String name;
    private final Predicate<PlayerData> unlocked;

    private KKFightingStyle(ResourceLocation id, HandStyle hand, String name, Predicate<PlayerData> unlocked) {
        this.id = id;
        this.hand = hand;
        this.name = name;
        this.unlocked = unlocked;
    }

    public static Builder builder(ResourceLocation id, HandStyle hand) {
        return new Builder(id, hand);
    }

    public ResourceLocation getId() {
        return id;
    }

    public HandStyle getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public boolean isUnlocked(PlayerData playerData) {
        return playerData != null && unlocked.test(playerData);
    }

    private static String defaultName(ResourceLocation id) {
        String path = id.getPath().replace("_", "");
        return KingdomKeys.MODID.equals(id.getNamespace()) ? "gui.menu.style." + path : id.getNamespace() + ".gui.menu.style." + path;
    }

    @Override
    public String toString() {
        return id.toString();
    }

    public static class Builder {
        private final ResourceLocation id;
        private final HandStyle hand;
        private String name;
        private Predicate<PlayerData> unlocked = ALWAYS;

        private Builder(ResourceLocation id, HandStyle hand) {
            this.id = id;
            this.hand = hand;
            this.name = defaultName(id);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder unlockedWhen(Predicate<PlayerData> unlocked) {
            this.unlocked = unlocked;
            return this;
        }

        public Builder unavailable() {
            return unlockedWhen(NEVER);
        }

        public KKFightingStyle build() {
            return new KKFightingStyle(id, hand, name, unlocked);
        }
    }
}
