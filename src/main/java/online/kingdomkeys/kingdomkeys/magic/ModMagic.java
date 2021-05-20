package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModMagic {

	public static IForgeRegistry<Magic> registry;

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerDriveFormRegistry(RegistryEvent.NewRegistry event) {
			registry = new RegistryBuilder<Magic>().setName(new ResourceLocation(KingdomKeys.MODID, "magics")).setType(Magic.class).create();
		}

		@SubscribeEvent
		public static void registerMagic(RegistryEvent.Register<Magic> event) {
			int order = 0;
			MagicFire fire = new MagicFire(Strings.Magic_Fire, 10, 3, true, order++);		
			MagicBlizzard blizzard = new MagicBlizzard(Strings.Magic_Blizzard, 12, 3, true, order++);
			MagicWater water = new MagicWater(Strings.Magic_Water, 12, 3, true, order++);
			MagicThunder thunder = new MagicThunder(Strings.Magic_Thunder, 15, 3, true, order++);
			MagicCure cure = new MagicCure(Strings.Magic_Cure, 300, 3, false, order++);
			MagicAero aero = new MagicAero(Strings.Magic_Aero, 12, 3, false, order++);
			MagicMagnet magnet = new MagicMagnet(Strings.Magic_Magnet, 15, 3, false, order++);
			MagicReflect reflect = new MagicReflect(Strings.Magic_Reflect, 10, 3, false, order++);
			MagicGravity gravity = new MagicGravity(Strings.Magic_Gravity, 14, 3, false, order++);
			MagicStop stop = new MagicStop(Strings.Magic_Stop, 20, 3, false, order++);

			event.getRegistry().registerAll(fire, blizzard, water, thunder, cure, aero, magnet, reflect, gravity, stop);
		}
	}
}
