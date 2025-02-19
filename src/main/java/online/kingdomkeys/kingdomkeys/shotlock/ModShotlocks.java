package online.kingdomkeys.kingdomkeys.shotlock;

import java.util.function.Supplier;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModShotlocks {

	public static DeferredRegister<Shotlock> SHOTLOCKS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "shotlocks"), KingdomKeys.MODID);
	public static Registry<Shotlock> registry = SHOTLOCKS.makeRegistry(builder -> builder.sync(true));

	static int order = 0;
	public static final Supplier<Shotlock>
		DARK_VOLLEY = SHOTLOCKS.register(Strings.DarkVolley, () -> new ShotlockDarkVolley(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.DarkVolley), order++, 2, 18)),
		RAGNAROK = SHOTLOCKS.register(Strings.Ragnarok, () -> new ShotlockRagnarok(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.Ragnarok), order++, 3, 16)),
		SONIC_SHADOW = SHOTLOCKS.register(Strings.SonicShadow, () -> new ShotlockSonicBlade(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.SonicShadow), order++, 4, 10)),
		PRISM_RAIN = SHOTLOCKS.register(Strings.PrismRain, () -> new ShotlockPrismRain(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.PrismRain), order++, 3, 16)),
		ULTIMA_CANNON = SHOTLOCKS.register(Strings.UltimaCannon, () -> new ShotlockUltimaCannon(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.UltimaCannon), order++, 2*20, 1))
	;
}