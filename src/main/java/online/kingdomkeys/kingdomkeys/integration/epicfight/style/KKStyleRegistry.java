package online.kingdomkeys.kingdomkeys.integration.epicfight.style;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.api.event.RegisterFightingStylesEvent;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.HandStyle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class KKStyleRegistry {
    private static final Map<ResourceLocation, KKFightingStyle> STYLES = new LinkedHashMap<>();

    private static boolean gathered = false;

    private KKStyleRegistry() {
    }

    private static synchronized void gather() {
        if (gathered) {
            return;
        }

        gathered = true;

        KKFightingStyles.registerDefaults(style -> STYLES.put(style.getId(), style));
        NeoForge.EVENT_BUS.post(new RegisterFightingStylesEvent(STYLES));
    }

    public static Collection<KKFightingStyle> all() {
        gather();
        return Collections.unmodifiableCollection(STYLES.values());
    }

    public static List<KKFightingStyle> of(HandStyle hand) {
        List<KKFightingStyle> styles = new ArrayList<>();

        for (KKFightingStyle style : all()) {
            if (style.getHand() == hand) {
                styles.add(style);
            }
        }

        return styles;
    }

    public static KKFightingStyle get(ResourceLocation id) {
        gather();
        return id == null ? null : STYLES.get(id);
    }

    public static ResourceLocation fallback(HandStyle hand) {
        for (KKFightingStyle style : all()) {
            if (style.getHand() == hand) {
                return style.getId();
            }
        }

        return null;
    }

    public static ResourceLocation orFallback(ResourceLocation id, HandStyle hand) {
        KKFightingStyle style = get(id);
        return style != null && style.getHand() == hand ? id : fallback(hand);
    }

    public static ResourceLocation read(String stored, HandStyle hand) {
        if (stored == null || stored.isEmpty()) {
            return fallback(hand);
        }

        ResourceLocation id = stored.indexOf(':') < 0 ? KingdomKeys.rl(stored.toLowerCase(Locale.ROOT)) : ResourceLocation.tryParse(stored);
        return id == null ? fallback(hand) : id;
    }

    public static boolean isChosen(Player player, ResourceLocation id) {
        KKFightingStyle style = get(id);

        if (player == null || style == null) {
            return false;
        }

        PlayerData playerData = PlayerData.get(player);

        if (playerData == null) {
            return false;
        }

        ResourceLocation chosen = style.getHand() == HandStyle.DUAL ? playerData.getDualStyle() : playerData.getSingleStyle();
        return id.equals(chosen);
    }
}
