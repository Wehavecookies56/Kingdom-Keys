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
			int order = 0;
			event.getRegistry().registerAll(
				new DriveFormNone(DriveForm.NONE.toString(), order++, true),
				new DriveFormValor(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "valor",order++, true),
				new DriveFormWisdom(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "wisdom",order++, false),
				new DriveFormLimit(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "limit",order++, false),
				new DriveFormMaster(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "master",order++, true),
				new DriveFormFinal(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "final",order++, true)
			);
		}
	}
}
