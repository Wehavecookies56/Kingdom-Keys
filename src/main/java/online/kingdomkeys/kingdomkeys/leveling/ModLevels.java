package online.kingdomkeys.kingdomkeys.leveling;

import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.KKSupplier;

import java.util.function.Supplier;

public class ModLevels {

	public static DeferredRegister<Level> LEVELS = DeferredRegister.create(KingdomKeys.rl("levels"), KingdomKeys.MODID);
	public static Registry<Level> registry = LEVELS.makeRegistry(builder -> builder.sync(true));


	public static final KKSupplier<Level>
		WARRIOR = register("warrior", () -> new Level(KingdomKeys.rl("warrior"))),
		MYSTIC = register("mystic", () -> new Level(KingdomKeys.rl("mystic"))),
		GUARDIAN = register("guardian", () -> new Level(KingdomKeys.rl("guardian")));

	private static KKSupplier<Level> register(String name, Supplier<Level> levelSupplier) {
		return new KKSupplier<>(KingdomKeys.rl(LEVELS.getNamespace(), name), LEVELS.register(name, levelSupplier));
	}
}
