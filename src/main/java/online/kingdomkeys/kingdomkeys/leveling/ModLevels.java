package online.kingdomkeys.kingdomkeys.leveling;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.function.Supplier;

public class ModLevels {

	public static DeferredRegister<Level> LEVELS = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "levels"), KingdomKeys.MODID);
	public static Registry<Level> registry = LEVELS.makeRegistry(builder -> builder.sync(true).defaultKey(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "empty")));


	public static final Supplier<Level>
		WARRIOR = LEVELS.register("warrior", () -> new Level(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "warrior"))),
		MYSTIC = LEVELS.register("mystic", () -> new Level(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "mystic"))),
		GUARDIAN = LEVELS.register("guardian", () -> new Level(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "guardian")));
}
