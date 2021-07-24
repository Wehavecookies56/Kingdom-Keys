package online.kingdomkeys.kingdomkeys.leveling;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ModLevels {
	public static IForgeRegistry<Level> registry;

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerDriveFormRegistry(RegistryEvent.NewRegistry event) {
			registry = new RegistryBuilder<Level>().setName(new ResourceLocation(KingdomKeys.MODID, "levels")).setType(Level.class).create();
		}

		@SubscribeEvent
		public static void registerDriveForms(RegistryEvent.Register<Level> event) {
			//int order = 0;
			event.getRegistry().registerAll(
				new Level(KingdomKeys.MODID + ":" + "warrior"),
				new Level(KingdomKeys.MODID + ":" + "mystic"),
				new Level(KingdomKeys.MODID + ":" + "guardian")
			);
		}
	}
}
