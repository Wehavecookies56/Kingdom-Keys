package online.kingdomkeys.kingdomkeys.banners;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.function.Supplier;

public class ModBannerPatterns {
    public static final DeferredRegister<BannerPattern> PATTERNS = DeferredRegister.create(Registries.BANNER_PATTERN, KingdomKeys.MODID);

    public static final Supplier<BannerPattern>
            HEARTLESS_OUTLINE = PATTERNS.register("heartless_outline", () -> createBanner("heartless_outline")),
            NOBODY_OUTLINE = PATTERNS.register("nobody_outline", () -> createBanner("nobody_outline")),
            HEARTLESS_FILLED = PATTERNS.register("heartless_filled", () -> createBanner("heartless_filled")),
            NOBODY_FILLED = PATTERNS.register("nobody_filled", () -> createBanner("nobody_filled"));

    public static BannerPattern createBanner(String name){
        return new BannerPattern(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, name), name);
    }
}