package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

import java.util.function.Supplier;

public class ModLimits {

	public static DeferredRegister<Limit> LIMITS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "limits"), KingdomKeys.MODID);

	public static final ResourceKey<Registry<Limit>> LIMITS_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "limits"));
	public static Registry<Limit> registry = new RegistryBuilder<>(LIMITS_KEY).sync(true).defaultKey(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "empty")).create();

	static int order = 0;

	public static final Supplier<Limit>
		LASER_CIRCLE = LIMITS.register(Strings.LaserCircle, () -> new LimitLaserCircle(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.LaserCircle), order++, OrgMember.XEMNAS)),
		LASER_DOME = LIMITS.register(Strings.LaserDome, () -> new LimitLaserDome(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.LaserDome), order++, OrgMember.XEMNAS)),
		ARROW_RAIN = LIMITS.register(Strings.ArrowRain, () -> new LimitArrowRain(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.ArrowRain), order++, OrgMember.XIGBAR)),
		SLOW_THUNDER_TRAIL = LIMITS.register(Strings.SlowThunderTrail, () -> new LimitThunderTrail(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.SlowThunderTrail), order++, OrgMember.LARXENE, 0.7F)),
		FAST_THUNDER_TRAIL = LIMITS.register(Strings.FastThunderTrail, () -> new LimitThunderTrail(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, Strings.FastThunderTrail), order++, OrgMember.LARXENE, 1.2F))
	;
}