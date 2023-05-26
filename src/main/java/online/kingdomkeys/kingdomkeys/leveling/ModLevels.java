package online.kingdomkeys.kingdomkeys.leveling;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ModLevels {

	public static DeferredRegister<Level> LEVELS = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "levels"), KingdomKeys.MODID);

	public static Supplier<IForgeRegistry<Level>> registry = LEVELS.makeRegistry(RegistryBuilder::new);

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerDriveForms(RegisterEvent event) {
			event.register(registry.get().getRegistryKey(), helper -> {
				helper.register("warrior", new Level(KingdomKeys.MODID + ":" + "warrior"));
				helper.register("mystic", new Level(KingdomKeys.MODID + ":" + "mystic"));
				helper.register("guardian", new Level(KingdomKeys.MODID + ":" + "guardian"));
			});
		}
	}
}
