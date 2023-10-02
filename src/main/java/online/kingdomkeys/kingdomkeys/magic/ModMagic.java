package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.lib.Strings;

import java.util.function.Supplier;

public class ModMagic {

	public static DeferredRegister<Magic> MAGIC = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "magics"), KingdomKeys.MODID);
	//test magic from another mod
	//public static DeferredRegister<Magic> MAGIC2 = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "magics"), "keykingdoms");

	public static Supplier<IForgeRegistry<Magic>> registry = MAGIC.makeRegistry(RegistryBuilder::new);

	public static int order = 0;

	public static final RegistryObject<Magic>
		FIRE = MAGIC.register(new ResourceLocation(Strings.Magic_Fire).getPath(), () -> new MagicFire(new ResourceLocation(Strings.Magic_Fire), 3, Strings.firaza)),
		BLIZZARD = MAGIC.register(new ResourceLocation(Strings.Magic_Blizzard).getPath(), () -> new MagicBlizzard(new ResourceLocation(Strings.Magic_Blizzard), 3, Strings.blizzaza)),
		WATER = MAGIC.register(new ResourceLocation(Strings.Magic_Water).getPath(), () -> new MagicWater(new ResourceLocation(Strings.Magic_Water), 3, Strings.waterza)),
		THUNDER = MAGIC.register(new ResourceLocation(Strings.Magic_Thunder).getPath(), () -> new MagicThunder(new ResourceLocation(Strings.Magic_Thunder), 3, Strings.thundaza)),
		CURE = MAGIC.register(new ResourceLocation(Strings.Magic_Cure).getPath(), () -> new MagicCure(new ResourceLocation(Strings.Magic_Cure), 3, Strings.curaza)),
		AERO = MAGIC.register(new ResourceLocation(Strings.Magic_Aero).getPath(), () -> new MagicAero(new ResourceLocation(Strings.Magic_Aero), 3, null)),
		MAGNET = MAGIC.register(new ResourceLocation(Strings.Magic_Magnet).getPath(), () -> new MagicMagnet(new ResourceLocation(Strings.Magic_Magnet), 3, null)),
		REFLECT = MAGIC.register(new ResourceLocation(Strings.Magic_Reflect).getPath(), () -> new MagicReflect(new ResourceLocation(Strings.Magic_Reflect), 3, null)),
		GRAVITY = MAGIC.register(new ResourceLocation(Strings.Magic_Gravity).getPath(), () -> new MagicGravity(new ResourceLocation(Strings.Magic_Gravity), 3, null)),
		STOP = MAGIC.register(new ResourceLocation(Strings.Magic_Stop).getPath(), () -> new MagicStop(new ResourceLocation(Strings.Magic_Stop), 3, null))
		//FIRE2 = MAGIC2.register("test", () -> new MagicFire(new ResourceLocation("keykingdoms", "test"), 3, null, order++))

		;
}
