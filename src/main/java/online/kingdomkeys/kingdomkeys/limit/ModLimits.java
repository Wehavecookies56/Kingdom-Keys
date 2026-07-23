package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.core.Registry;
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
		FAST_THUNDER_TRAIL = register(Strings.FastThunderTrail, () -> new LimitThunderTrail(KingdomKeys.rl(Strings.FastThunderTrail), order++, OrgMember.LARXENE, 1.2F)),
		FLAME_WHEEL = register(Strings.FlameRing, () -> new LimitFlameRing(KingdomKeys.rl(Strings.FlameRing), order++, OrgMember.AXEL)),
		FIRE_WALL = register(Strings.FlameWall, () -> new LimitFlameWall(KingdomKeys.rl(Strings.FlameWall), order++, OrgMember.AXEL)),
		LANCE_STORM = register(Strings.LanceStorm, () -> new LimitLanceStorm(KingdomKeys.rl(Strings.LanceStorm), order++, OrgMember.XALDIN)),
		FALLING_SPEAR = register(Strings.FallingSpear, () -> new LimitFallingSpear(KingdomKeys.rl(Strings.FallingSpear), order++, OrgMember.XALDIN)),
		BERSERK_CLAYMORE = register(Strings.BerserkClaymore, () -> new LimitBerserkClaymore(KingdomKeys.rl(Strings.BerserkClaymore), order++, OrgMember.SAIX)),
		POWERUP = register(Strings.Powerup, () -> new LimitPowerup(KingdomKeys.rl(Strings.Powerup), order++, OrgMember.LEXAEUS)),
		ROCKY_PILLARS = register(Strings.RockyPillars, () -> new LimitRockyPillars(KingdomKeys.rl(Strings.RockyPillars), order++, OrgMember.LEXAEUS)),
		ICE_PILLARS = register(Strings.IcePillars, () -> new LimitIcePillars(KingdomKeys.rl(Strings.IcePillars), order++, OrgMember.VEXEN)),
		WATER_TRAIL = register(Strings.WaterTrail, () -> new LimitWaterTrail(KingdomKeys.rl(Strings.WaterTrail), order++, OrgMember.DEMYX)),
		WATER_WALL = register(Strings.WaterWall, () -> new LimitWaterWall(KingdomKeys.rl(Strings.WaterWall), order++, OrgMember.DEMYX)),
		CARD_RING = register(Strings.CardRing, () -> new LimitCardRing(KingdomKeys.rl(Strings.CardRing), order++, OrgMember.LUXORD)),
		LIGHT_BARRAGE = register(Strings.LightBarrage, () -> new LimitLightBarrage(KingdomKeys.rl(Strings.LightBarrage), order++, OrgMember.ROXAS)),
		SCYTHE_DASH = register(Strings.ScytheDash, () -> new LimitScytheDash(KingdomKeys.rl(Strings.ScytheDash), order++, OrgMember.MARLUXIA)),
		PETAL_VOID = register(Strings.PetalVoid, () -> new LimitPetalLaunchers(KingdomKeys.rl(Strings.PetalVoid), order++, OrgMember.MARLUXIA))
	;

	private static KKSupplier<Limit> register(String name, Supplier<Limit> limitSupplier) {
		return new KKSupplier<>(KingdomKeys.rl(LIMITS.getNamespace(), name), LIMITS.register(name, limitSupplier));
	}
}