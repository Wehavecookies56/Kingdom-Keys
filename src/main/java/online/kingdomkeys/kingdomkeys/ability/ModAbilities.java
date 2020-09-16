package online.kingdomkeys.kingdomkeys.ability;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModAbilities {
	public static IForgeRegistry<Ability> registry;

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerAbilitiesRegistry(RegistryEvent.NewRegistry event) {
			registry = new RegistryBuilder<Ability>().setName(new ResourceLocation(KingdomKeys.MODID, "abilities")).setType(Ability.class).create();
		}

		public static Ability createAbility(String name, int apCost, AbilityType type, int order) {
			return new Ability(name, apCost, type, order);
		}

		@SubscribeEvent
		public static void registerAbilities(RegistryEvent.Register<Ability> event) {
			int order = 0;
			event.getRegistry().registerAll(
				//Action
				createAbility(Strings.autoValor, 1, AbilityType.ACTION, order++),
				createAbility(Strings.autoWisdom, 1, AbilityType.ACTION, order++),
				createAbility(Strings.autoLimit, 1, AbilityType.ACTION, order++),
				createAbility(Strings.autoMaster, 1, AbilityType.ACTION, order++),
				createAbility(Strings.autoFinal, 1, AbilityType.ACTION, order++),

				// Growth
				createAbility(Strings.highJump, 2, AbilityType.GROWTH, order++), 
				createAbility(Strings.quickRun, 2, AbilityType.GROWTH, order++), 
				createAbility(Strings.dodgeRoll, 3, AbilityType.GROWTH, order++),
				createAbility(Strings.aerialDodge, 3, AbilityType.GROWTH, order++),
				createAbility(Strings.glide, 3, AbilityType.GROWTH, order++),

				// Support
				createAbility(Strings.zeroExp, 0, AbilityType.SUPPORT, order++),
				createAbility(Strings.scan, 1, AbilityType.SUPPORT, order++),
				createAbility(Strings.mpHaste, 3, AbilityType.SUPPORT, order++),
				createAbility(Strings.mpHastera, 4, AbilityType.SUPPORT, order++),
				createAbility(Strings.mpRage, 3, AbilityType.SUPPORT, order++)
			);
		}
	}
}