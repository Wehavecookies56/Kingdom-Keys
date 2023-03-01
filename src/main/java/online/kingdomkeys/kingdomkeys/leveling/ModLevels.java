package online.kingdomkeys.kingdomkeys.leveling;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ModLevels {

	public static DeferredRegister<Level> LEVELS = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "levels"), KingdomKeys.MODID);

	public static Supplier<IForgeRegistry<Level>> registry = LEVELS.makeRegistry(Level.class, RegistryBuilder::new);

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerDriveForms(RegistryEvent.Register<Level> event) {
			event.getRegistry().registerAll(
				new Level(KingdomKeys.MODID + ":" + "warrior"),
				new Level(KingdomKeys.MODID + ":" + "mystic"),
				new Level(KingdomKeys.MODID + ":" + "guardian")
			);
		}
	}
}
