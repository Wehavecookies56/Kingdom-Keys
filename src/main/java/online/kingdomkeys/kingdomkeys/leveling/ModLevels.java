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

	public static final ResourceKey<Registry<Level>> LEVE£L_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "levels"));
	public static Registry<Level> registry = new RegistryBuilder<>(LEVE£L_KEY).sync(true).defaultKey(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "empty")).create();


	public static final Supplier<Level>
		WARRIOR = LEVELS.register("warrior", () -> new Level(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "warrior"))),
		MYSTIC = LEVELS.register("mystic", () -> new Level(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "mystic"))),
		GUARDIAN = LEVELS.register("guardian", () -> new Level(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "guardian")));
}
