package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormAnti;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormFinal;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormLimit;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormMaster;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormNone;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormValor;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormWisdom;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public class ModLimits {

	public static IForgeRegistry<Limit> registry;

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerDriveFormRegistry(RegistryEvent.NewRegistry event) {
			registry = new RegistryBuilder<Limit>().setName(new ResourceLocation(KingdomKeys.MODID, "limits")).setType(Limit.class).create();
		}

		@SubscribeEvent
		public static void registerLimits(RegistryEvent.Register<Limit> event) {
			int order = 0;
			event.getRegistry().registerAll(
					new LimitLaserCircle(KingdomKeys.MODID + ":" + Strings.LaserCircle, order++, 100, 200, OrgMember.XEMNAS),
					new LimitLaserDome(KingdomKeys.MODID + ":" + Strings.LaserDome, order++, 400, 600, OrgMember.XEMNAS)
			);
		}
	}
}
