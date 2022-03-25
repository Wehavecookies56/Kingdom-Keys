package online.kingdomkeys.kingdomkeys.shotlock;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.leveling.Level;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.util.function.Supplier;

public class ModShotlocks {

	public static DeferredRegister<Shotlock> SHOTLOCKS = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "shotlocks"), KingdomKeys.MODID);

	public static Supplier<IForgeRegistry<Shotlock>> registry = SHOTLOCKS.makeRegistry(Shotlock.class, RegistryBuilder::new);;

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerLimits(RegistryEvent.Register<Shotlock> event) {
			int order = 0;
			event.getRegistry().registerAll(
					new ShotlockDarkVolley(KingdomKeys.MODID + ":" + Strings.DarkVolley, order++, 2, 18),
					new ShotlockRagnarok(KingdomKeys.MODID + ":" + Strings.Ragnarok, order++, 3, 16),
					new ShotlockPrismRain(KingdomKeys.MODID + ":" + Strings.PrismRain, order++, 3, 16)
			);
		}
	}
}