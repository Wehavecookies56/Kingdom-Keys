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

		@SubscribeEvent
		public static void registerDriveForms(RegistryEvent.Register<DriveForm> event) {
			int order = 0;
			event.getRegistry().registerAll(
				new DriveFormNone(DriveForm.NONE.toString(), order++, true),
				new DriveFormValor(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "valor",order++, new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/valor.png"), true),
				new DriveFormWisdom(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "wisdom",order++, new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/wisdom.png"), false),
				new DriveFormLimit(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "limit",order++, new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/limit.png"), false),
				new DriveFormMaster(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "master",order++, new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/master.png"), true),
				new DriveFormFinal(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "final",order++, new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/final.png"), true),
				new DriveFormAnti(KingdomKeys.MODID + ":" + Strings.DF_Prefix + "anti", order++, new ResourceLocation(KingdomKeys.MODID, "textures/models/armor/anti.png"), false)
			);
		}
	}
}
