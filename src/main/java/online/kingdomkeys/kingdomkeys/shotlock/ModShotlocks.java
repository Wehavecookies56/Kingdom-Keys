package online.kingdomkeys.kingdomkeys.shotlock;

import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.KKSupplier;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.util.function.Supplier;

public class ModShotlocks {

	public static DeferredRegister<Shotlock> SHOTLOCKS = DeferredRegister.create(KingdomKeys.rl("shotlocks"), KingdomKeys.MODID);
	public static Registry<Shotlock> registry = SHOTLOCKS.makeRegistry(builder -> builder.sync(true));

	static int order = 0;
	public static final KKSupplier<Shotlock>
		DARK_VOLLEY = register(Strings.DarkVolley, () -> new ShotlockDarkVolley(KingdomKeys.rl(Strings.DarkVolley), order++)),
		RAGNAROK = register(Strings.Ragnarok, () -> new ShotlockRagnarok(KingdomKeys.rl(Strings.Ragnarok), order++)),
		SONIC_SHADOW = register(Strings.SonicShadow, () -> new ShotlockSonicBlade(KingdomKeys.rl(Strings.SonicShadow), order++)),
		PRISM_RAIN = register(Strings.PrismRain, () -> new ShotlockPrismRain(KingdomKeys.rl(Strings.PrismRain), order++)),
		ULTIMA_CANNON = register(Strings.UltimaCannon, () -> new ShotlockUltimaCannon(KingdomKeys.rl(Strings.UltimaCannon), order++)),
		FLAME_SALVO = register(Strings.FlameSalvo, () -> new ShotlockFlameSalvo(KingdomKeys.rl(Strings.FlameSalvo), order++)),
		ABSOLUTE_ZERO = register(Strings.AbsoluteZero, () -> new ShotlockAbsoluteZero(KingdomKeys.rl(Strings.AbsoluteZero), order++)),
		THUNDERSTORM = register(Strings.Thunderstorm, () -> new ShotlockThunderstorm(KingdomKeys.rl(Strings.Thunderstorm), order++)),
		CHAOS_SNAKE = register(Strings.ChaosSnake, () -> new ShotlockChaosSnake(KingdomKeys.rl(Strings.ChaosSnake), order++)),
		BUBBLE_BLASTER = register(Strings.BubbleBlaster, () -> new ShotlockBubbleBlaster(KingdomKeys.rl(Strings.BubbleBlaster), order++)),
		BIO_BARRAGE = register(Strings.BioBarrage, () -> new ShotlockBioBarrage(KingdomKeys.rl(Strings.BioBarrage), order++)),
		PULSE_BOMB = register(Strings.PulseBomb, () -> new ShotlockPulseBomb(KingdomKeys.rl(Strings.PulseBomb), order++)),
		PHOTON_CHARGE = register(Strings.PhotonCharge, () -> new ShotlockPhotonCharge(KingdomKeys.rl(Strings.PhotonCharge), order++)),
		LIGHTNING_RAY = register(Strings.LightningRay, () -> new ShotlockLightningRay(KingdomKeys.rl(Strings.LightningRay), order++)),
		METEOR_SHOWER = register(Strings.MeteorShower, () -> new ShotlockMeteorShower(KingdomKeys.rl(Strings.MeteorShower), order++))
	;

	private static KKSupplier<Shotlock> register(String name, Supplier<Shotlock> shotlockSupplier) {
		return new KKSupplier<>(KingdomKeys.rl(SHOTLOCKS.getNamespace(), name), SHOTLOCKS.register(name, shotlockSupplier));
	}
}