package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModDriveForms {

	public static IForgeRegistry<DriveForm> registry;

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerDriveFormRegistry(RegistryEvent.NewRegistry event) {
			registry = new RegistryBuilder<DriveForm>().setName(new ResourceLocation(KingdomKeys.MODID, "driveforms")).setType(DriveForm.class).create();
		}

		public static DriveForm createForm(String name, int cost) {
			return new DriveForm(KingdomKeys.MODID + ":" + Strings.DF_Prefix + name, cost);
		}

		@SubscribeEvent
		public static void registerDriveForms(RegistryEvent.Register<DriveForm> event) {
			event.getRegistry().registerAll(
				createForm("valor", 300), 
				createForm("wisdom", 300),
				createForm("limit", 400),
				createForm("master", 400),
				createForm("final", 500)
			);
		}
	}
}
