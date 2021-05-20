package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModReactionCommands {

	public static IForgeRegistry<ReactionCommand> registry;

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerDriveFormRegistry(RegistryEvent.NewRegistry event) {
			registry = new RegistryBuilder<ReactionCommand>().setName(new ResourceLocation(KingdomKeys.MODID, "reactioncommands")).setType(ReactionCommand.class).create();
		}

		@SubscribeEvent
		public static void registerLimits(RegistryEvent.Register<ReactionCommand> event) {
			//int order = 0;
			event.getRegistry().registerAll(
					new ReactionAutoForm(KingdomKeys.MODID+":"+Strings.autoValorRC, Strings.autoValor, Strings.Form_Valor),
					new ReactionAutoForm(KingdomKeys.MODID+":"+Strings.autoWisdomRC, Strings.autoWisdom, Strings.Form_Wisdom),
					new ReactionAutoForm(KingdomKeys.MODID+":"+Strings.autoLimitRC, Strings.autoLimit, Strings.Form_Limit),
					new ReactionAutoForm(KingdomKeys.MODID+":"+Strings.autoMasterRC, Strings.autoMaster, Strings.Form_Master),
					new ReactionAutoForm(KingdomKeys.MODID+":"+Strings.autoFinalRC, Strings.autoFinal, Strings.Form_Final),
					new ReactionMagic(Strings.Magic_Fire),
					new ReactionMagic(Strings.Magic_Blizzard),
					new ReactionMagic(Strings.Magic_Water),
					new ReactionMagic(Strings.Magic_Thunder)
			);
		}
	}
}