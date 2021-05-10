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
					new ReactionAutoValor(KingdomKeys.MODID+":"+Strings.autoValorRC)
			);
		}
	}
}