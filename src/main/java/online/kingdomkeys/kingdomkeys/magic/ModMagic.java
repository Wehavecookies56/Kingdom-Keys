package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryBuilder;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.util.function.Supplier;

public class ModMagic {

	public static DeferredRegister<Magic> MAGIC = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "magics"), KingdomKeys.MODID);
	public static Registry<Magic> registry = MAGIC.makeRegistry(builder -> builder.sync(true).defaultKey(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "empty")));

	public static int order = 0;

	public static final Supplier<Magic>
		FIRE = MAGIC.register(ResourceLocation.parse(Strings.Magic_Fire).getPath(), () -> new MagicFire(ResourceLocation.parse(Strings.Magic_Fire), 3, Strings.firaza)),
		BLIZZARD = MAGIC.register(ResourceLocation.parse(Strings.Magic_Blizzard).getPath(), () -> new MagicBlizzard(ResourceLocation.parse(Strings.Magic_Blizzard), 3, Strings.blizzaza)),
		WATER = MAGIC.register(ResourceLocation.parse(Strings.Magic_Water).getPath(), () -> new MagicWater(ResourceLocation.parse(Strings.Magic_Water), 3, Strings.waterza)),
		THUNDER = MAGIC.register(ResourceLocation.parse(Strings.Magic_Thunder).getPath(), () -> new MagicThunder(ResourceLocation.parse(Strings.Magic_Thunder), 3, Strings.thundaza)),
		CURE = MAGIC.register(ResourceLocation.parse(Strings.Magic_Cure).getPath(), () -> new MagicCure(ResourceLocation.parse(Strings.Magic_Cure), 3, Strings.curaza)),
		AERO = MAGIC.register(ResourceLocation.parse(Strings.Magic_Aero).getPath(), () -> new MagicAero(ResourceLocation.parse(Strings.Magic_Aero), 3, null)),
		MAGNET = MAGIC.register(ResourceLocation.parse(Strings.Magic_Magnet).getPath(), () -> new MagicMagnet(ResourceLocation.parse(Strings.Magic_Magnet), 3, null)),
		REFLECT = MAGIC.register(ResourceLocation.parse(Strings.Magic_Reflect).getPath(), () -> new MagicReflect(ResourceLocation.parse(Strings.Magic_Reflect), 3, null)),
		GRAVITY = MAGIC.register(ResourceLocation.parse(Strings.Magic_Gravity).getPath(), () -> new MagicGravity(ResourceLocation.parse(Strings.Magic_Gravity), 3, null)),
		STOP = MAGIC.register(ResourceLocation.parse(Strings.Magic_Stop).getPath(), () -> new MagicStop(ResourceLocation.parse(Strings.Magic_Stop), 3, null))
		;
}
