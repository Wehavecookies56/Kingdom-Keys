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
		static int order = 0;
		
		@SubscribeEvent
		public static void registerAbilitiesRegistry(RegistryEvent.NewRegistry event) {
			registry = new RegistryBuilder<Ability>().setName(new ResourceLocation(KingdomKeys.MODID, "abilities")).setType(Ability.class).create();
		}

		public static Ability createAbility(String name, int apCost, AbilityType type) {
			return new Ability(name, apCost, type, order++);
		}

		@SubscribeEvent
		public static void registerAbilities(RegistryEvent.Register<Ability> event) {
			event.getRegistry().registerAll(
				//Action
				createAbility(Strings.autoValor, 1, AbilityType.ACTION),
				createAbility(Strings.autoWisdom, 1, AbilityType.ACTION),
				createAbility(Strings.autoLimit, 1, AbilityType.ACTION),
				createAbility(Strings.autoMaster, 1, AbilityType.ACTION),
				createAbility(Strings.autoFinal, 1, AbilityType.ACTION),

				// Growth
				createAbility(Strings.highJump, 2, AbilityType.GROWTH), 
				createAbility(Strings.quickRun, 2, AbilityType.GROWTH), 
				createAbility(Strings.dodgeRoll, 3, AbilityType.GROWTH),
				createAbility(Strings.aerialDodge, 3, AbilityType.GROWTH),
				createAbility(Strings.glide, 3, AbilityType.GROWTH),

				// Support
				createAbility(Strings.zeroExp, 0, AbilityType.SUPPORT),
				createAbility(Strings.scan, 1, AbilityType.SUPPORT),
				createAbility(Strings.mpSafety, 0, AbilityType.SUPPORT),
				createAbility(Strings.mpHaste, 3, AbilityType.SUPPORT),
				createAbility(Strings.mpHastera, 4, AbilityType.SUPPORT),
				createAbility(Strings.mpHastega, 5, AbilityType.SUPPORT),
				createAbility(Strings.mpRage, 3, AbilityType.SUPPORT),
				createAbility(Strings.damageDrive, 3, AbilityType.SUPPORT),
				createAbility(Strings.driveConverter, 4, AbilityType.SUPPORT),
				createAbility(Strings.focusConverter, 3, AbilityType.SUPPORT),
				createAbility(Strings.driveBoost, 3, AbilityType.SUPPORT),
				createAbility(Strings.formBoost, 3, AbilityType.SUPPORT),
				createAbility(Strings.fullMPBlast, 2, AbilityType.SUPPORT),
				createAbility(Strings.mpThrift, 2, AbilityType.SUPPORT),
				//createAbility(Strings.luckyLucky, 5, AbilityType.SUPPORT),
				createAbility(Strings.jackpot, 4, AbilityType.SUPPORT),
				createAbility(Strings.fireBoost, 3, AbilityType.SUPPORT),
				createAbility(Strings.blizzardBoost, 4, AbilityType.SUPPORT),
				createAbility(Strings.thunderBoost, 5, AbilityType.SUPPORT),
				createAbility(Strings.experienceBoost, 4, AbilityType.SUPPORT),
				createAbility(Strings.criticalBoost, 3, AbilityType.SUPPORT),
				createAbility(Strings.treasureMagnet, 3, AbilityType.SUPPORT),
				createAbility(Strings.secondChance, 4, AbilityType.SUPPORT),
				createAbility(Strings.wizardsRuse, 4, AbilityType.SUPPORT),
				createAbility(Strings.extraCast, 3, AbilityType.SUPPORT),
				createAbility(Strings.damageControl, 5, AbilityType.SUPPORT),
				createAbility(Strings.lightAndDarkness, 2, AbilityType.SUPPORT),
				createAbility(Strings.synchBlade, 5, AbilityType.SUPPORT)
			);
		}
	}
}