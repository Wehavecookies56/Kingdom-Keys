package online.kingdomkeys.kingdomkeys.limit;

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
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public class ModLimits {

	public static DeferredRegister<Limit> LIMITS = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "limits"), KingdomKeys.MODID);

	public static Supplier<IForgeRegistry<Limit>> registry = LIMITS.makeRegistry(Limit.class, RegistryBuilder::new);;

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class Registry {

		@SubscribeEvent
		public static void registerLimits(RegistryEvent.Register<Limit> event) {
			int order = 0;
			event.getRegistry().registerAll(
					new LimitLaserCircle(KingdomKeys.MODID + ":" + Strings.LaserCircle, order++, 100, 200, OrgMember.XEMNAS),
					new LimitLaserDome(KingdomKeys.MODID + ":" + Strings.LaserDome, order++, 400, 600, OrgMember.XEMNAS),
					new LimitArrowRain(KingdomKeys.MODID + ":" + Strings.ArrowRain, order++, 300, 600, OrgMember.XIGBAR)
			);
		}
	}
}