package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.KKSupplier;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

import java.util.function.Supplier;

public class ModLimits {

	public static DeferredRegister<Limit> LIMITS = DeferredRegister.create(KingdomKeys.rl("limits"), KingdomKeys.MODID);
	public static Registry<Limit> registry = LIMITS.makeRegistry(builder -> builder.sync(true));

	static int order = 0;

	public static final KKSupplier<Limit>
		LASER_CIRCLE = register(Strings.LaserCircle, () -> new LimitLaserCircle(KingdomKeys.rl(Strings.LaserCircle), order++, OrgMember.XEMNAS)),
		LASER_DOME = register(Strings.LaserDome, () -> new LimitLaserDome(KingdomKeys.rl(Strings.LaserDome), order++, OrgMember.XEMNAS)),
		ARROW_RAIN = register(Strings.ArrowRain, () -> new LimitArrowRain(KingdomKeys.rl(Strings.ArrowRain), order++, OrgMember.XIGBAR)),
		SLOW_THUNDER_TRAIL = register(Strings.SlowThunderTrail, () -> new LimitThunderTrail(KingdomKeys.rl(Strings.SlowThunderTrail), order++, OrgMember.LARXENE, 0.7F)),
		FAST_THUNDER_TRAIL = register(Strings.FastThunderTrail, () -> new LimitThunderTrail(KingdomKeys.rl(Strings.FastThunderTrail), order++, OrgMember.LARXENE, 1.2F))
	;

	private static KKSupplier<Limit> register(String name, Supplier<Limit> limitSupplier) {
		return new KKSupplier<>(LIMITS.register(name, limitSupplier));
	}
}