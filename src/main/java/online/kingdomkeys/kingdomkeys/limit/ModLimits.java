package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

import java.util.function.Supplier;

public class ModLimits {

	public static DeferredRegister<Limit> LIMITS = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "limits"), KingdomKeys.MODID);

	public static Supplier<IForgeRegistry<Limit>> registry = LIMITS.makeRegistry(Limit.class, RegistryBuilder::new);

	static int order = 0;

	public static final RegistryObject<Limit>
		LASER_CIRCLE = LIMITS.register(Strings.LaserCircle, () -> new LimitLaserCircle(KingdomKeys.MODID + ":" + Strings.LaserCircle, order++, OrgMember.XEMNAS)),
		LASER_DOME = LIMITS.register(Strings.LaserDome, () -> new LimitLaserDome(KingdomKeys.MODID + ":" + Strings.LaserDome, order++, OrgMember.XEMNAS)),
		ARROW_RAIN = LIMITS.register(Strings.ArrowRain, () -> new LimitArrowRain(KingdomKeys.MODID + ":" + Strings.ArrowRain, order++, OrgMember.XIGBAR)),
		SLOW_THUNDER_TRAIL = LIMITS.register(Strings.SlowThunderTrail, () -> new LimitThunderTrail(KingdomKeys.MODID + ":" + Strings.SlowThunderTrail, order++, OrgMember.LARXENE, 0.7F)),
		FAST_THUNDER_TRAIL = LIMITS.register(Strings.FastThunderTrail, () -> new LimitThunderTrail(KingdomKeys.MODID + ":" + Strings.FastThunderTrail, order++, OrgMember.LARXENE, 1.2F))
	;
}