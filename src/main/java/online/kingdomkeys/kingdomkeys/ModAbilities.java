package online.kingdomkeys.kingdomkeys;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.Ability.Type;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModAbilities {
	public static IForgeRegistry<Ability> registry;

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerAbilitiesRegistry(RegistryEvent.NewRegistry event) {
			registry = new RegistryBuilder<Ability>().setName(new ResourceLocation(KingdomKeys.MODID, "abilities")).setType(Ability.class).create();
		}

		public static Ability createAbility(String name, int apCost, Type type, int order) {
			return new Ability(KingdomKeys.MODID + ":" + Strings.AB_Prefix + name, apCost, type, order);
		}

		@SubscribeEvent
		public static void registerAbilities(RegistryEvent.Register<Ability> event) {
			int order = 0;
			event.getRegistry().registerAll(
				// Growth
				createAbility("high_jump", 2, Type.GROWTH, order++), 
				createAbility("quick_run", 2, Type.GROWTH, order++), 
				createAbility("dodge_roll", 3, Type.GROWTH, order++),
				createAbility("aerial_dodge", 3, Type.GROWTH, order++),
				createAbility("glide", 3, Type.GROWTH, order++),
				
				// Support
				createAbility("scan", 1, Type.SUPPORT, order++)
			);
		}
	}
}