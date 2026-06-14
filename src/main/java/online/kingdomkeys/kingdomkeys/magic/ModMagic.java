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
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class ModMagic {

	public static DeferredRegister<Magic> MAGIC = DeferredRegister.create(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "magics"), KingdomKeys.MODID);
	public static final Supplier<Magic>
			FIRE = MAGIC.register(ResourceLocation.parse(Strings.Magic_Fire).getPath(), () -> new MagicFire(ResourceLocation.parse(Strings.Magic_Fire), 0, Strings.firaza)),
			FIRA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Fira).getPath(), () -> new MagicFire(ResourceLocation.parse(Strings.Magic_Fira), 1, Strings.firaza)),
			FIRAGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Firaga).getPath(), () -> new MagicFire(ResourceLocation.parse(Strings.Magic_Firaga), 2, Strings.firaza)),
			FIRAZA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Firaza).getPath(), () -> new MagicFire(ResourceLocation.parse(Strings.Magic_Firaza), 3, Strings.firaza)),

			BLIZZARD = MAGIC.register(ResourceLocation.parse(Strings.Magic_Blizzard).getPath(), () -> new MagicBlizzard(ResourceLocation.parse(Strings.Magic_Blizzard), 0, Strings.blizzaza)),
			BLIZZARA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Blizzara).getPath(), () -> new MagicBlizzard(ResourceLocation.parse(Strings.Magic_Blizzara), 1, Strings.blizzaza)),
			BLIZZAGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Blizzaga).getPath(), () -> new MagicBlizzard(ResourceLocation.parse(Strings.Magic_Blizzaga), 2, Strings.blizzaza)),
			BLIZZAZA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Blizzaza).getPath(), () -> new MagicBlizzard(ResourceLocation.parse(Strings.Magic_Blizzaza), 3, Strings.blizzaza)),

			WATER = MAGIC.register(ResourceLocation.parse(Strings.Magic_Water).getPath(), () -> new MagicWater(ResourceLocation.parse(Strings.Magic_Water), 0, Strings.waterza)),
			WATERA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Watera).getPath(), () -> new MagicWater(ResourceLocation.parse(Strings.Magic_Watera), 1, Strings.waterza)),
			WATERGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Waterga).getPath(), () -> new MagicWater(ResourceLocation.parse(Strings.Magic_Waterga), 2, Strings.waterza)),
			WATERZA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Waterza).getPath(), () -> new MagicWater(ResourceLocation.parse(Strings.Magic_Waterza), 3, Strings.waterza)),

			THUNDER = MAGIC.register(ResourceLocation.parse(Strings.Magic_Thunder).getPath(), () -> new MagicThunder(ResourceLocation.parse(Strings.Magic_Thunder), 0, Strings.thundaza)),
			THUNDARA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Thundara).getPath(), () -> new MagicThunder(ResourceLocation.parse(Strings.Magic_Thundara), 1, Strings.thundaza)),
			THUNDAGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Thundaga).getPath(), () -> new MagicThunder(ResourceLocation.parse(Strings.Magic_Thundaga), 2, Strings.thundaza)),
			THUNDAZA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Thundaza).getPath(), () -> new MagicThunder(ResourceLocation.parse(Strings.Magic_Thundaza), 3, Strings.thundaza)),

			CURE = MAGIC.register(ResourceLocation.parse(Strings.Magic_Cure).getPath(), () -> new MagicCure(ResourceLocation.parse(Strings.Magic_Cure), 0, Strings.curaza)),
			CURA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Cura).getPath(), () -> new MagicCure(ResourceLocation.parse(Strings.Magic_Cura), 1, Strings.curaza)),
			CURAGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Curaga).getPath(), () -> new MagicCure(ResourceLocation.parse(Strings.Magic_Curaga), 2, Strings.curaza)),
			CURAZA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Curaza).getPath(), () -> new MagicCure(ResourceLocation.parse(Strings.Magic_Curaza), 3, Strings.curaza)),

			AERO = MAGIC.register(ResourceLocation.parse(Strings.Magic_Aero).getPath(), () -> new MagicAero(ResourceLocation.parse(Strings.Magic_Aero), 0, null)),
			AERORA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Aerora).getPath(), () -> new MagicAero(ResourceLocation.parse(Strings.Magic_Aerora), 1, null)),
			AEROGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Aeroga).getPath(), () -> new MagicAero(ResourceLocation.parse(Strings.Magic_Aeroga), 2, null)),

			MAGNET = MAGIC.register(ResourceLocation.parse(Strings.Magic_Magnet).getPath(), () -> new MagicMagnet(ResourceLocation.parse(Strings.Magic_Magnet), 0, null)),
			MAGNERA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Magnera).getPath(), () -> new MagicMagnet(ResourceLocation.parse(Strings.Magic_Magnera), 1, null)),
			MAGNEGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Magnega).getPath(), () -> new MagicMagnet(ResourceLocation.parse(Strings.Magic_Magnega), 2, null)),

			REFLECT = MAGIC.register(ResourceLocation.parse(Strings.Magic_Reflect).getPath(), () -> new MagicReflect(ResourceLocation.parse(Strings.Magic_Reflect), 0, null)),
			REFLERA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Reflera).getPath(), () -> new MagicReflect(ResourceLocation.parse(Strings.Magic_Reflera), 1, null)),
			REFLEGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Reflega).getPath(), () -> new MagicReflect(ResourceLocation.parse(Strings.Magic_Reflega), 2, null)),

			GRAVITY = MAGIC.register(ResourceLocation.parse(Strings.Magic_Gravity).getPath(), () -> new MagicGravity(ResourceLocation.parse(Strings.Magic_Gravity), 0, null)),
			GRAVIRA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Gravira).getPath(), () -> new MagicGravity(ResourceLocation.parse(Strings.Magic_Gravira), 1, null)),
			GRAVIGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Graviga).getPath(), () -> new MagicGravity(ResourceLocation.parse(Strings.Magic_Graviga), 2, null)),

			ZERO_GRAVITY = MAGIC.register(ResourceLocation.parse(Strings.Magic_ZeroGravity).getPath(), () -> new MagicZeroGravity(ResourceLocation.parse(Strings.Magic_ZeroGravity), 0, null)),
			ZERO_GRAVIRA = MAGIC.register(ResourceLocation.parse(Strings.Magic_ZeroGravira).getPath(), () -> new MagicZeroGravity(ResourceLocation.parse(Strings.Magic_ZeroGravira), 1, null)),
			ZERO_GRAVIGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_ZeroGraviga).getPath(), () -> new MagicZeroGravity(ResourceLocation.parse(Strings.Magic_ZeroGraviga), 2, null)),

			STOP = MAGIC.register(ResourceLocation.parse(Strings.Magic_Stop).getPath(), () -> new MagicStop(ResourceLocation.parse(Strings.Magic_Stop), 0, null)),
			STOPRA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Stopra).getPath(), () -> new MagicStop(ResourceLocation.parse(Strings.Magic_Stopra), 1, null)),
			STOPGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Stopga).getPath(), () -> new MagicStop(ResourceLocation.parse(Strings.Magic_Stopga), 2, null)),

			DARK_FIRAGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_DarkFiraga).getPath(), () -> new MagicFire(ResourceLocation.parse(Strings.Magic_DarkFiraga), 1, null)),
			TRIPLE_FIRAGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_TripleFiraga).getPath(), () -> new MagicTripleFiraga(ResourceLocation.parse(Strings.Magic_TripleFiraga), 1, null)),
			CRAWLING_FIRAGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_CrawlingFiraga).getPath(), () -> new MagicCrawlingFiraga(ResourceLocation.parse(Strings.Magic_CrawlingFiraga), 1, null)),
			FISSION_FIRAGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_FissionFiraga).getPath(), () -> new MagicFissionFiraga(ResourceLocation.parse(Strings.Magic_FissionFiraga), 1, null)),
			FIRAGA_BURST = MAGIC.register(ResourceLocation.parse(Strings.Magic_FiragaBurst).getPath(), () -> new MagicFiragaBurst(ResourceLocation.parse(Strings.Magic_FiragaBurst), 1, null)),
			IGNITE = MAGIC.register(ResourceLocation.parse(Strings.Magic_Ignite).getPath(), () -> new MagicIgnite(ResourceLocation.parse(Strings.Magic_Ignite), 1, null)),

			DEEP_FREEZE = MAGIC.register(ResourceLocation.parse(Strings.Magic_DeepFreeze).getPath(), () -> new MagicDeepFreeze(ResourceLocation.parse(Strings.Magic_DeepFreeze), 1, null, false)),
			GLACIER = MAGIC.register(ResourceLocation.parse(Strings.Magic_Glacier).getPath(), () -> new MagicDeepFreeze(ResourceLocation.parse(Strings.Magic_Glacier), 1, null, true)),
			ICE_BARRAGE = MAGIC.register(ResourceLocation.parse(Strings.Magic_IceBarrage).getPath(), () -> new MagicIceBarrage(ResourceLocation.parse(Strings.Magic_IceBarrage), 1, null)),
			TRIPLE_BLIZZAGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_TripleBlizzard).getPath(), () -> new MagicTripleBlizzaga(ResourceLocation.parse(Strings.Magic_TripleBlizzard), 1, null)),

			THUNDAGA_SHOT = MAGIC.register(ResourceLocation.parse(Strings.Magic_ThundagaShot).getPath(), () -> new MagicThundagaShot(ResourceLocation.parse(Strings.Magic_ThundagaShot), 1, null)),
			TRIPLE_PLASMA = MAGIC.register(ResourceLocation.parse(Strings.Magic_TriplePlasma).getPath(), () -> new MagicTriplePlasma(ResourceLocation.parse(Strings.Magic_TriplePlasma), 1, null)),

			BLACKOUT = MAGIC.register(ResourceLocation.parse(Strings.Magic_Blackout).getPath(), () -> new MagicStatusEffectRadius(ResourceLocation.parse(Strings.Magic_Blackout), 1, null, MobEffects.DARKNESS, SoundEvents.BEACON_POWER_SELECT, ParticleTypes.SQUID_INK)),
			POISON = MAGIC.register(ResourceLocation.parse(Strings.Magic_Poison).getPath(), () -> new MagicStatusEffectRadius(ResourceLocation.parse(Strings.Magic_Poison), 1, null, MobEffects.POISON, ModSounds.poison.get(), new DustParticleOptions(new Vector3f(0.6F, 0.3F, 0.8F), 1F))),

			BALLOON = MAGIC.register(ResourceLocation.parse(Strings.Magic_Balloon).getPath(), () -> new MagicBalloon(ResourceLocation.parse(Strings.Magic_Balloon), false, 0, null)),
			BALLOONRA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Balloonra).getPath(), () -> new MagicBalloon(ResourceLocation.parse(Strings.Magic_Balloonra), false, 1, null)),
			BALLOONGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Balloonga).getPath(), () -> new MagicBalloon(ResourceLocation.parse(Strings.Magic_Balloonga), false, 2, null)),

			SPARK = MAGIC.register(ResourceLocation.parse(Strings.Magic_Spark).getPath(), () -> new MagicSpark(ResourceLocation.parse(Strings.Magic_Spark), false, 0, null)),
			SPARKRA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Sparkra).getPath(), () -> new MagicSpark(ResourceLocation.parse(Strings.Magic_Sparkra), false, 1, null)),
			SPARKGA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Sparkga).getPath(), () -> new MagicSpark(ResourceLocation.parse(Strings.Magic_Sparkga), false, 2, null)),


			MINE_SHIELD = MAGIC.register(ResourceLocation.parse(Strings.Magic_MineShield).getPath(), () -> new MagicMineShield(ResourceLocation.parse(Strings.Magic_MineShield), false, 0, null)),
			MINE_SQUARE = MAGIC.register(ResourceLocation.parse(Strings.Magic_MineSquare).getPath(), () -> new MagicMineShield(ResourceLocation.parse(Strings.Magic_MineSquare), false, 1, null)),
			SEEKER_MINE = MAGIC.register(ResourceLocation.parse(Strings.Magic_SeekerMine).getPath(), () -> new MagicMineShield(ResourceLocation.parse(Strings.Magic_SeekerMine), false, 2, null)),

			WARP = MAGIC.register(ResourceLocation.parse(Strings.Magic_Warp).getPath(), () -> new MagicWarp(ResourceLocation.parse(Strings.Magic_Warp), false, 1, null)),

			FAITH = MAGIC.register(ResourceLocation.parse(Strings.Magic_Faith).getPath(), () -> new MagicFaith(ResourceLocation.parse(Strings.Magic_Faith), false, 1, null)),

			BIND = MAGIC.register(ResourceLocation.parse(Strings.Magic_Bind).getPath(), () -> new MagicStatusEffectRadius(ResourceLocation.parse(Strings.Magic_Bind), 1, null, ModMobEffects.ZERO_GRAVITY, SoundEvents.BEACON_AMBIENT, ParticleTypes.ELECTRIC_SPARK)),
			ESUNA = MAGIC.register(ResourceLocation.parse(Strings.Magic_Esuna).getPath(), () -> new MagicEsuna(ResourceLocation.parse(Strings.Magic_Esuna), false, 1, null));

	public static Registry<Magic> registry = MAGIC.makeRegistry(builder -> builder.sync(true));
	public static int order = 0;
}
