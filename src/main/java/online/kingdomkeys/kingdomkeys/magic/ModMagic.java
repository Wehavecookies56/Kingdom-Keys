package online.kingdomkeys.kingdomkeys.magic;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModMagic {

	public static DeferredRegister<Magic> MAGIC = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "magics"), KingdomKeys.MODID);

	public static Supplier<IForgeRegistry<Magic>> registry = MAGIC.makeRegistry(Magic.class, RegistryBuilder::new);;

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerMagic(RegistryEvent.Register<Magic> event) {
			int order = 0;
			MagicFire fire = new MagicFire(Strings.Magic_Fire, 3, true, order++);		
			MagicBlizzard blizzard = new MagicBlizzard(Strings.Magic_Blizzard, 3, true, order++);
			MagicWater water = new MagicWater(Strings.Magic_Water, 3, true, order++);
			MagicThunder thunder = new MagicThunder(Strings.Magic_Thunder, 3, true, order++);
			MagicCure cure = new MagicCure(Strings.Magic_Cure, 3, false, order++);
			MagicAero aero = new MagicAero(Strings.Magic_Aero, 3, false, order++);
			MagicMagnet magnet = new MagicMagnet(Strings.Magic_Magnet, 3, false, order++);
			MagicReflect reflect = new MagicReflect(Strings.Magic_Reflect, 3, false, order++);
			MagicGravity gravity = new MagicGravity(Strings.Magic_Gravity, 3, false, order++);
			MagicStop stop = new MagicStop(Strings.Magic_Stop, 3, false, order++);
			
			event.getRegistry().registerAll(fire, blizzard, water, thunder, cure, aero, magnet, reflect, gravity, stop);
		}
	}
}
