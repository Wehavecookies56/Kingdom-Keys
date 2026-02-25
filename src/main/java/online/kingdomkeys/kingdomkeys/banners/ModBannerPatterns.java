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
            HEARTLESS = PATTERNS.register("heartless", () -> new BannerPattern(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"heartless"), "heartless")),
            NOBODY = PATTERNS.register("nobody", () -> new BannerPattern(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID,"nobody"), "nobody"));

}