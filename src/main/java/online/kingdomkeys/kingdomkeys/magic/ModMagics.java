package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModMagics {

	public static IForgeRegistry<Magic> registry;

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerDriveFormRegistry(RegistryEvent.NewRegistry event) {
			registry = new RegistryBuilder<Magic>().setName(new ResourceLocation(KingdomKeys.MODID, "magics")).setType(Magic.class).create();
		}

		@SubscribeEvent
		public static void registerMagics(RegistryEvent.Register<Magic> event) {
			int order = 0;
			event.getRegistry().registerAll(
					new MagicFire(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "fire", 5, order++),
					new MagicBlizzard(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "blizzard", 30, order++),
					new MagicWater(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "water", 300, order++),
					new MagicThunder(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "thunder", 300, order++),
					new MagicCure(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "cure", 300, order++),
					new MagicAero(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "aero", 300, order++),
					new MagicMagnet(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "magnet", 300, order++),
					new MagicReflect(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "reflect", 300, order++),
					new MagicGravity(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "gravity", 300, order++),
					new MagicStop(KingdomKeys.MODID + ":" + Strings.Mag_Prefix + "stop", 300, order++)
			);
		}
	}
}
