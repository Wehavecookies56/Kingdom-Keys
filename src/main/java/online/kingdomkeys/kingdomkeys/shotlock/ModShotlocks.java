package online.kingdomkeys.kingdomkeys.shotlock;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.KKSupplier;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.util.function.Supplier;

public class ModShotlocks {

	public static DeferredRegister<Shotlock> SHOTLOCKS = DeferredRegister.create(KingdomKeys.rl("shotlocks"), KingdomKeys.MODID);
	public static Registry<Shotlock> registry = SHOTLOCKS.makeRegistry(builder -> builder.sync(true));

	static int order = 0;
	public static final KKSupplier<Shotlock>
		DARK_VOLLEY = register(Strings.DarkVolley, () -> new ShotlockDarkVolley(KingdomKeys.rl(Strings.DarkVolley), order++, 2, 18)),
		RAGNAROK = register(Strings.Ragnarok, () -> new ShotlockRagnarok(KingdomKeys.rl(Strings.Ragnarok), order++, 3, 16)),
		SONIC_SHADOW = register(Strings.SonicShadow, () -> new ShotlockSonicBlade(KingdomKeys.rl(Strings.SonicShadow), order++, 4, 10)),
		PRISM_RAIN = register(Strings.PrismRain, () -> new ShotlockPrismRain(KingdomKeys.rl(Strings.PrismRain), order++, 3, 16)),
		ULTIMA_CANNON = register(Strings.UltimaCannon, () -> new ShotlockUltimaCannon(KingdomKeys.rl(Strings.UltimaCannon), order++, 2*20, 1))
	;

	private static KKSupplier<Shotlock> register(String name, Supplier<Shotlock> shotlockSupplier) {
		return new KKSupplier<>(SHOTLOCKS.register(name, shotlockSupplier));
	}
}