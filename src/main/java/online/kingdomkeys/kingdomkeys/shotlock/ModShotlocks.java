package online.kingdomkeys.kingdomkeys.shotlock;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModShotlocks {

	public static IForgeRegistry<Shotlock> registry;

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerDriveFormRegistry(RegistryEvent.NewRegistry event) {
			registry = new RegistryBuilder<Shotlock>().setName(new ResourceLocation(KingdomKeys.MODID, "shotlocks")).setType(Shotlock.class).create();
		}

		@SubscribeEvent
		public static void registerLimits(RegistryEvent.Register<Shotlock> event) {
			int order = 0;
			event.getRegistry().registerAll(
					new ShotlockDarkVolley(KingdomKeys.MODID + ":" + Strings.DarkVolley, order++, 2, 18),
					new ShotlockRagnarok(KingdomKeys.MODID + ":" + Strings.RagnarokShotlock, order++, 3, 16)
			);
		}
	}
}