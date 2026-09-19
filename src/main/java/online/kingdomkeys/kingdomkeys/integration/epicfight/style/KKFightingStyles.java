package online.kingdomkeys.kingdomkeys.integration.epicfight.style;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.integration.epicfight.enums.HandStyle;

import java.util.function.Consumer;

public final class KKFightingStyles {
    public static final ResourceLocation SORA = KingdomKeys.rl("sora");
    public static final ResourceLocation RIKU = KingdomKeys.rl("riku");
    public static final ResourceLocation ROXAS = KingdomKeys.rl("roxas");
    public static final ResourceLocation TERRA = KingdomKeys.rl("terra");
    public static final ResourceLocation AQUA = KingdomKeys.rl("aqua");
    public static final ResourceLocation VENTUS = KingdomKeys.rl("ventus");

    public static final ResourceLocation KH2_ROXAS_DUAL = KingdomKeys.rl("kh2_roxas_dual");
    public static final ResourceLocation DAYS_ROXAS_DUAL = KingdomKeys.rl("days_roxas_dual");

    private KKFightingStyles() {
    }

    static void registerDefaults(Consumer<KKFightingStyle> registry) {
        registry.accept(KKFightingStyle.builder(SORA, HandStyle.SINGLE).build());
        registry.accept(KKFightingStyle.builder(RIKU, HandStyle.SINGLE).build());
        registry.accept(KKFightingStyle.builder(ROXAS, HandStyle.SINGLE).unavailable().build());
        registry.accept(KKFightingStyle.builder(TERRA, HandStyle.SINGLE).unavailable().build());
        registry.accept(KKFightingStyle.builder(AQUA, HandStyle.SINGLE).build());
        registry.accept(KKFightingStyle.builder(VENTUS, HandStyle.SINGLE).unavailable().build());

        registry.accept(KKFightingStyle.builder(KH2_ROXAS_DUAL, HandStyle.DUAL).build());
        registry.accept(KKFightingStyle.builder(DAYS_ROXAS_DUAL, HandStyle.DUAL).build());
    }
}
