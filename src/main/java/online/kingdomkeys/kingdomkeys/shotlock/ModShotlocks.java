package online.kingdomkeys.kingdomkeys.shotlock;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModShotlocks {

	public static DeferredRegister<Shotlock> SHOTLOCKS = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "shotlocks"), KingdomKeys.MODID);

	public static Supplier<IForgeRegistry<Shotlock>> registry = SHOTLOCKS.makeRegistry(RegistryBuilder::new);

	static int order = 0;
	public static final RegistryObject<Shotlock>
		DARK_VOLLEY = SHOTLOCKS.register(Strings.DarkVolley, () -> new ShotlockDarkVolley(KingdomKeys.MODID + ":" + Strings.DarkVolley, order++, 2, 18)),
		RAGNAROK = SHOTLOCKS.register(Strings.Ragnarok, () -> new ShotlockRagnarok(KingdomKeys.MODID + ":" + Strings.Ragnarok, order++, 3, 16)),
		SONIC_SHADOW = SHOTLOCKS.register(Strings.SonicShadow, () -> new ShotlockSonicBlade(KingdomKeys.MODID + ":" + Strings.SonicShadow, order++, 4, 10)),
		PRISM_RAIN = SHOTLOCKS.register(Strings.PrismRain, () -> new ShotlockPrismRain(KingdomKeys.MODID + ":" + Strings.PrismRain, order++, 3, 16)),
		ULTIMA_CANNON = SHOTLOCKS.register(Strings.UltimaCannon, () -> new ShotlockUltimaCannon(KingdomKeys.MODID + ":" + Strings.UltimaCannon, order++, 2*20, 1))
	;
}