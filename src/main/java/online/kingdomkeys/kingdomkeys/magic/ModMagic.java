package online.kingdomkeys.kingdomkeys.magic;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.reactioncommands.ModReactionCommands;

public class ModMagic {

	public static DeferredRegister<Magic> MAGIC = DeferredRegister.create(new ResourceLocation(KingdomKeys.MODID, "magics"), KingdomKeys.MODID);

	public static Supplier<IForgeRegistry<Magic>> registry = MAGIC.makeRegistry(Magic.class, RegistryBuilder::new);;

	static int order = 0;

	public static final RegistryObject<Magic>
		FIRE = MAGIC.register(new ResourceLocation(Strings.Magic_Fire).getPath(), () -> new MagicFire(Strings.Magic_Fire, 3, Strings.firaza, order++)),
		BLIZZARD = MAGIC.register(new ResourceLocation(Strings.Magic_Blizzard).getPath(), () -> new MagicBlizzard(Strings.Magic_Blizzard, 3, Strings.blizzaza, order++)),
		WATER = MAGIC.register(new ResourceLocation(Strings.Magic_Water).getPath(), () -> new MagicWater(Strings.Magic_Water, 3, Strings.waterza, order++)),
		THUNDER = MAGIC.register(new ResourceLocation(Strings.Magic_Thunder).getPath(), () -> new MagicThunder(Strings.Magic_Thunder, 3, Strings.thundaza, order++)),
		CURE = MAGIC.register(new ResourceLocation(Strings.Magic_Cure).getPath(), () -> new MagicCure(Strings.Magic_Cure, 3, null, order++)),
		AERO = MAGIC.register(new ResourceLocation(Strings.Magic_Aero).getPath(), () -> new MagicAero(Strings.Magic_Aero, 3, null, order++)),
		MAGNET = MAGIC.register(new ResourceLocation(Strings.Magic_Magnet).getPath(), () -> new MagicMagnet(Strings.Magic_Magnet, 3, null, order++)),
		REFLECT = MAGIC.register(new ResourceLocation(Strings.Magic_Reflect).getPath(), () -> new MagicReflect(Strings.Magic_Reflect, 3, null, order++)),
		GRAVITY = MAGIC.register(new ResourceLocation(Strings.Magic_Gravity).getPath(), () -> new MagicGravity(Strings.Magic_Gravity, 3, null, order++)),
		STOP = MAGIC.register(new ResourceLocation(Strings.Magic_Stop).getPath(), () -> new MagicStop(Strings.Magic_Stop, 3, null, order++))
	;
}
