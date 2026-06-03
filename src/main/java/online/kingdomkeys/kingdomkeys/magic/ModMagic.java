package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class ModMagic {

	public static DeferredRegister<Magic> MAGIC = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "magics"), KingdomKeys.MODID);
	public static Registry<Magic> registry = MAGIC.makeRegistry(builder -> builder.sync(true));

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
		ZERO_GRAVITY = MAGIC.register(ResourceLocation.parse(Strings.Magic_ZeroGravity).getPath(), () -> new MagicZeroGravity(ResourceLocation.parse(Strings.Magic_ZeroGravity), 3, null)),
		STOP = MAGIC.register(ResourceLocation.parse(Strings.Magic_Stop).getPath(), () -> new MagicStop(ResourceLocation.parse(Strings.Magic_Stop), 3, null)),

		DARK_FIRAGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_DarkFiraga).getPath(), () -> new MagicFire(ResourceLocation.parse(Strings.Magic_DarkFiraga), 1, null)),
		TRIPLE_FIRAGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_TripleFiraga).getPath(), () -> new MagicTripleFiraga(ResourceLocation.parse(Strings.Magic_TripleFiraga), 1, null)),
		CRAWLING_FIRAGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_CrawlingFiraga).getPath(), () -> new MagicCrawlingFiraga(ResourceLocation.parse(Strings.Magic_CrawlingFiraga), 1, null)),
		FISSION_FIRAGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_FissionFiraga).getPath(), () -> new MagicFissionFiraga(ResourceLocation.parse(Strings.Magic_FissionFiraga), 1, null)),
		FIRAGA_BURST = MAGIC.register(ResourceLocation.parse(Strings.Magic_FiragaBurst).getPath(), () -> new MagicFiragaBurst(ResourceLocation.parse(Strings.Magic_FiragaBurst), 1, null)),

		DEEP_FREEZE = MAGIC.register(ResourceLocation.parse(Strings.Magic_DeepFreeze).getPath(), () -> new MagicDeepFreeze(ResourceLocation.parse(Strings.Magic_DeepFreeze), 1, null,false)),
		GLACIER = MAGIC.register(ResourceLocation.parse(Strings.Magic_Glacier).getPath(), () -> new MagicDeepFreeze(ResourceLocation.parse(Strings.Magic_Glacier), 1, null,true)),
		ICE_BARRAGE = MAGIC.register(ResourceLocation.parse(Strings.Magic_IceBarrage).getPath(), () -> new MagicIceBarrage(ResourceLocation.parse(Strings.Magic_IceBarrage), 1, null,false)),

		THUNDAGA_SHOT = MAGIC.register(ResourceLocation.parse(Strings.Magic_ThundagaShot).getPath(), () -> new MagicThundagaShot(ResourceLocation.parse(Strings.Magic_ThundagaShot), 1, null)),

		TRIPLE_BLIZZAGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_TripleBlizzard).getPath(), () -> new MagicTripleBlizzaga(ResourceLocation.parse(Strings.Magic_TripleBlizzard), 1, null)),
		BLACKOUT = MAGIC.register(ResourceLocation.parse(Strings.Magic_Blackout).getPath(), () -> new MagicStatusEffectRadius(ResourceLocation.parse(Strings.Magic_Blackout), 1, null, MobEffects.DARKNESS, SoundEvents.BEACON_POWER_SELECT, ParticleTypes.SQUID_INK)),
		POISON = MAGIC.register(ResourceLocation.parse(Strings.Magic_Poison).getPath(), () -> new MagicStatusEffectRadius(ResourceLocation.parse(Strings.Magic_Poison), 1, null, MobEffects.POISON, ModSounds.poison.get(), new DustParticleOptions(new Vector3f(0.6F,0.3F,0.8F),1F))),

		BALLOON = MAGIC.register(ResourceLocation.parse(Strings.Magic_Balloon).getPath(), () -> new MagicBalloon(ResourceLocation.parse(Strings.Magic_Balloon), false, 3, null)),
		SPARK = MAGIC.register(ResourceLocation.parse(Strings.Magic_Spark).getPath(), () -> new MagicSpark(ResourceLocation.parse(Strings.Magic_Spark), false, 3, null)),
		MINE_SHIELD = MAGIC.register(ResourceLocation.parse(Strings.Magic_MineShield).getPath(), () -> new MagicMineShield(ResourceLocation.parse(Strings.Magic_MineShield), false, 3, null)),
		WARP = MAGIC.register(ResourceLocation.parse(Strings.Magic_Warp).getPath(), () -> new MagicWarp(ResourceLocation.parse(Strings.Magic_Warp), false, 1, null)),
		ESUNA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Esuna).getPath(), () -> new MagicEsuna(ResourceLocation.parse(Strings.Magic_Esuna), false, 1, null))
	;
}
