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

		public static DriveForm createForm(String name, int cost, String growthAbility, int ap, int[] levelupCosts) {
			return new DriveForm(KingdomKeys.MODID + ":" + Strings.DF_Prefix + name, cost, growthAbility, ap, levelupCosts);
		}

		@SubscribeEvent
		public static void registerDriveForms(RegistryEvent.Register<DriveForm> event) {
			event.getRegistry().registerAll(
				createForm("valor", 300, "High Jump", 1, new int[]{0, 80, 240, 520, 968, 1528, 2200}), 
				createForm("wisdom", 300, "Quick Run", 1, new int[]{0, 20, 80, 152, 242, 350, 500}),
				createForm("limit", 400, "Dodge Roll", 1, new int[]{0, 3, 9, 21, 40, 63, 90}),
				createForm("master", 400, "Aerial Dodge", 1, new int[] {0, 60, 240, 456, 726, 1050, 1500}),
				createForm("final", 500, "Glide", -10, new int[]{0, 20, 80, 152, 242, 350, 500})
			);
		}
	}
}
