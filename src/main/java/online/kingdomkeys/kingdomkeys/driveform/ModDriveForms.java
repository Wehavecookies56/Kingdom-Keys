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

		/*public static DriveForm createForm(String name, int cost, String growthAbility, int ap, int[] levelupCosts) {
			return new DriveForm(KingdomKeys.MODID + ":" + Strings.DF_Prefix + name, cost, growthAbility, ap, levelupCosts);
		}*/

		@SubscribeEvent
		public static void registerDriveForms(RegistryEvent.Register<DriveForm> event) {
			event.getRegistry().registerAll(
				new DriveFormValor(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "valor"), 
				new DriveFormWisdom(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "wisdom"),
				new DriveFormLimit(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "limit"),
				new DriveFormMaster(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "master"),
				new DriveFormFinal(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "final")
			);
		}
	}
}
