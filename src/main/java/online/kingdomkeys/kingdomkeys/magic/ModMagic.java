package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.lib.KKSupplier;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class ModMagic {

	public static DeferredRegister<Magic> MAGIC = DeferredRegister.create(KingdomKeys.rl("magics"), KingdomKeys.MODID);
	public static final KKSupplier<Magic>
			FIRE = register(Strings.Magic_Fire, () -> new MagicFire(KingdomKeys.rl(Strings.Magic_Fire), 0, KingdomKeys.rl(Strings.firaza))),
			FIRA = register(Strings.Magic_Fira, () -> new MagicFire(KingdomKeys.rl(Strings.Magic_Fira), 1, KingdomKeys.rl(Strings.firaza))),
			FIRAGA = register(Strings.Magic_Firaga, () -> new MagicFire(KingdomKeys.rl(Strings.Magic_Firaga), 2, KingdomKeys.rl(Strings.firaza))),
			FIRAZA = register(Strings.Magic_Firaza, () -> new MagicFire(KingdomKeys.rl(Strings.Magic_Firaza), 3, KingdomKeys.rl(Strings.firaza))),

			BLIZZARD = register(Strings.Magic_Blizzard, () -> new MagicBlizzard(KingdomKeys.rl(Strings.Magic_Blizzard), 0, KingdomKeys.rl(Strings.blizzaza))),
			BLIZZARA = register(Strings.Magic_Blizzara, () -> new MagicBlizzard(KingdomKeys.rl(Strings.Magic_Blizzara), 1, KingdomKeys.rl(Strings.blizzaza))),
			BLIZZAGA = register(Strings.Magic_Blizzaga, () -> new MagicBlizzard(KingdomKeys.rl(Strings.Magic_Blizzaga), 2, KingdomKeys.rl(Strings.blizzaza))),
			BLIZZAZA = register(Strings.Magic_Blizzaza, () -> new MagicBlizzard(KingdomKeys.rl(Strings.Magic_Blizzaza), 3, KingdomKeys.rl(Strings.blizzaza))),

			WATER = register(Strings.Magic_Water, () -> new MagicWater(KingdomKeys.rl(Strings.Magic_Water), 0, KingdomKeys.rl(Strings.waterza))),
			WATERA = register(Strings.Magic_Watera, () -> new MagicWater(KingdomKeys.rl(Strings.Magic_Watera), 1, KingdomKeys.rl(Strings.waterza))),
			WATERGA = register(Strings.Magic_Waterga, () -> new MagicWater(KingdomKeys.rl(Strings.Magic_Waterga), 2, KingdomKeys.rl(Strings.waterza))),
			WATERZA = register(Strings.Magic_Waterza, () -> new MagicWater(KingdomKeys.rl(Strings.Magic_Waterza), 3, KingdomKeys.rl(Strings.waterza))),

			THUNDER = register(Strings.Magic_Thunder, () -> new MagicThunder(KingdomKeys.rl(Strings.Magic_Thunder), 0, KingdomKeys.rl(Strings.thundaza))),
			THUNDARA = register(Strings.Magic_Thundara, () -> new MagicThunder(KingdomKeys.rl(Strings.Magic_Thundara), 1, KingdomKeys.rl(Strings.thundaza))),
			THUNDAGA = register(Strings.Magic_Thundaga, () -> new MagicThunder(KingdomKeys.rl(Strings.Magic_Thundaga), 2, KingdomKeys.rl(Strings.thundaza))),
			THUNDAZA = register(Strings.Magic_Thundaza, () -> new MagicThunder(KingdomKeys.rl(Strings.Magic_Thundaza), 3, KingdomKeys.rl(Strings.thundaza))),

			CURE = register(Strings.Magic_Cure, () -> new MagicCure(KingdomKeys.rl(Strings.Magic_Cure), 0, KingdomKeys.rl(Strings.curaza))),
			CURA = register(Strings.Magic_Cura, () -> new MagicCure(KingdomKeys.rl(Strings.Magic_Cura), 1, KingdomKeys.rl(Strings.curaza))),
			CURAGA = register(Strings.Magic_Curaga, () -> new MagicCure(KingdomKeys.rl(Strings.Magic_Curaga), 2, KingdomKeys.rl(Strings.curaza))),
			CURAZA = register(Strings.Magic_Curaza, () -> new MagicCure(KingdomKeys.rl(Strings.Magic_Curaza), 3, KingdomKeys.rl(Strings.curaza))),

			AERO = register(Strings.Magic_Aero, () -> new MagicAero(KingdomKeys.rl(Strings.Magic_Aero), 0, null)),
			AERORA = register(Strings.Magic_Aerora, () -> new MagicAero(KingdomKeys.rl(Strings.Magic_Aerora), 1, null)),
			AEROGA = register(Strings.Magic_Aeroga, () -> new MagicAero(KingdomKeys.rl(Strings.Magic_Aeroga), 2, null)),

			MAGNET = register(Strings.Magic_Magnet, () -> new MagicMagnet(KingdomKeys.rl(Strings.Magic_Magnet), 0, null)),
			MAGNERA = register(Strings.Magic_Magnera, () -> new MagicMagnet(KingdomKeys.rl(Strings.Magic_Magnera), 1, null)),
			MAGNEGA = register(Strings.Magic_Magnega, () -> new MagicMagnet(KingdomKeys.rl(Strings.Magic_Magnega), 2, null)),

			REFLECT = register(Strings.Magic_Reflect, () -> new MagicReflect(KingdomKeys.rl(Strings.Magic_Reflect), 0, null)),
			REFLERA = register(Strings.Magic_Reflera, () -> new MagicReflect(KingdomKeys.rl(Strings.Magic_Reflera), 1, null)),
			REFLEGA = register(Strings.Magic_Reflega, () -> new MagicReflect(KingdomKeys.rl(Strings.Magic_Reflega), 2, null)),

			GRAVITY = register(Strings.Magic_Gravity, () -> new MagicGravity(KingdomKeys.rl(Strings.Magic_Gravity), 0, null)),
			GRAVIRA = register(Strings.Magic_Gravira, () -> new MagicGravity(KingdomKeys.rl(Strings.Magic_Gravira), 1, null)),
			GRAVIGA = register(Strings.Magic_Graviga, () -> new MagicGravity(KingdomKeys.rl(Strings.Magic_Graviga), 2, null)),

			ZERO_GRAVITY = register(Strings.Magic_ZeroGravity, () -> new MagicZeroGravity(KingdomKeys.rl(Strings.Magic_ZeroGravity), 0, null)),
			ZERO_GRAVIRA = register(Strings.Magic_ZeroGravira, () -> new MagicZeroGravity(KingdomKeys.rl(Strings.Magic_ZeroGravira), 1, null)),
			ZERO_GRAVIGA = register(Strings.Magic_ZeroGraviga, () -> new MagicZeroGravity(KingdomKeys.rl(Strings.Magic_ZeroGraviga), 2, null)),

			STOP = register(Strings.Magic_Stop, () -> new MagicStop(KingdomKeys.rl(Strings.Magic_Stop), 0, null)),
			STOPRA = register(Strings.Magic_Stopra, () -> new MagicStop(KingdomKeys.rl(Strings.Magic_Stopra), 1, null)),
			STOPGA = register(Strings.Magic_Stopga, () -> new MagicStop(KingdomKeys.rl(Strings.Magic_Stopga), 2, null)),

			DARK_FIRAGA = register(Strings.Magic_DarkFiraga, () -> new MagicFire(KingdomKeys.rl(Strings.Magic_DarkFiraga), 1, null)),
			TRIPLE_FIRAGA = register(Strings.Magic_TripleFiraga, () -> new MagicTripleFiraga(KingdomKeys.rl(Strings.Magic_TripleFiraga), 1, null)),
			CRAWLING_FIRAGA = register(Strings.Magic_CrawlingFiraga, () -> new MagicCrawlingFiraga(KingdomKeys.rl(Strings.Magic_CrawlingFiraga), 1, null)),
			FISSION_FIRAGA = register(Strings.Magic_FissionFiraga, () -> new MagicFissionFiraga(KingdomKeys.rl(Strings.Magic_FissionFiraga), 1, null)),
			FIRAGA_BURST = register(Strings.Magic_FiragaBurst, () -> new MagicFiragaBurst(KingdomKeys.rl(Strings.Magic_FiragaBurst), 1, null)),
			IGNITE = register(Strings.Magic_Ignite, () -> new MagicIgnite(KingdomKeys.rl(Strings.Magic_Ignite), 1, null)),

			DEEP_FREEZE = register(Strings.Magic_DeepFreeze, () -> new MagicDeepFreeze(KingdomKeys.rl(Strings.Magic_DeepFreeze), 1, null, false)),
			GLACIER = register(Strings.Magic_Glacier, () -> new MagicDeepFreeze(KingdomKeys.rl(Strings.Magic_Glacier), 1, null, true)),
			ICE_BARRAGE = register(Strings.Magic_IceBarrage, () -> new MagicIceBarrage(KingdomKeys.rl(Strings.Magic_IceBarrage), 1, null)),
			TRIPLE_BLIZZAGA = register(Strings.Magic_TripleBlizzard, () -> new MagicTripleBlizzaga(KingdomKeys.rl(Strings.Magic_TripleBlizzard), 1, null)),

			THUNDAGA_SHOT = register(Strings.Magic_ThundagaShot, () -> new MagicThundagaShot(KingdomKeys.rl(Strings.Magic_ThundagaShot), 1, null)),
			TRIPLE_PLASMA = register(Strings.Magic_TriplePlasma, () -> new MagicTriplePlasma(KingdomKeys.rl(Strings.Magic_TriplePlasma), 1, null)),

			BLACKOUT = register(Strings.Magic_Blackout, () -> new MagicStatusEffectRadius(KingdomKeys.rl(Strings.Magic_Blackout), 1, null, MobEffects.DARKNESS, SoundEvents.BEACON_POWER_SELECT, ParticleTypes.SQUID_INK)),
			POISON = register(Strings.Magic_Poison, () -> new MagicStatusEffectRadius(KingdomKeys.rl(Strings.Magic_Poison), 1, null, MobEffects.POISON, ModSounds.poison.get(), new DustParticleOptions(new Vector3f(0.6F, 0.3F, 0.8F), 1F))),

			BALLOON = register(Strings.Magic_Balloon, () -> new MagicBalloon(KingdomKeys.rl(Strings.Magic_Balloon), false, 0, null)),
			BALLOONRA = register(Strings.Magic_Balloonra, () -> new MagicBalloon(KingdomKeys.rl(Strings.Magic_Balloonra), false, 1, null)),
			BALLOONGA = register(Strings.Magic_Balloonga, () -> new MagicBalloon(KingdomKeys.rl(Strings.Magic_Balloonga), false, 2, null)),

			SPARK = register(Strings.Magic_Spark, () -> new MagicSpark(KingdomKeys.rl(Strings.Magic_Spark), false, 0, null)),
			SPARKRA = register(Strings.Magic_Sparkra, () -> new MagicSpark(KingdomKeys.rl(Strings.Magic_Sparkra), false, 1, null)),
			SPARKGA = register(Strings.Magic_Sparkga, () -> new MagicSpark(KingdomKeys.rl(Strings.Magic_Sparkga), false, 2, null)),


			MINE_SHIELD = register(Strings.Magic_MineShield, () -> new MagicMineShield(KingdomKeys.rl(Strings.Magic_MineShield), false, 0, null)),
			MINE_SQUARE = register(Strings.Magic_MineSquare, () -> new MagicMineShield(KingdomKeys.rl(Strings.Magic_MineSquare), false, 1, null)),
			SEEKER_MINE = register(Strings.Magic_SeekerMine, () -> new MagicMineShield(KingdomKeys.rl(Strings.Magic_SeekerMine), false, 2, null)),

			WARP = register(Strings.Magic_Warp, () -> new MagicWarp(KingdomKeys.rl(Strings.Magic_Warp), false, 1, null)),

			FAITH = register(Strings.Magic_Faith, () -> new MagicFaith(KingdomKeys.rl(Strings.Magic_Faith), false, 1, null)),

			BIND = register(Strings.Magic_Bind, () -> new MagicStatusEffectRadius(KingdomKeys.rl(Strings.Magic_Bind), 1, null, ModMobEffects.ZERO_GRAVITY, SoundEvents.BEACON_AMBIENT, ParticleTypes.ELECTRIC_SPARK)),
			CONFUSE = register(Strings.Magic_Confuse, () -> new MagicStatusEffectRadius(KingdomKeys.rl(Strings.Magic_Confuse), 1, null, ModMobEffects.CONFUSE, SoundEvents.BEACON_AMBIENT, ParticleTypes.EFFECT)),
			ESUNA = register(Strings.Magic_Esuna, () -> new MagicEsuna(KingdomKeys.rl(Strings.Magic_Esuna), false, 1, null)),
			MINI = register(Strings.Magic_Mini, () -> new MagicStatusEffectRadius(KingdomKeys.rl(Strings.Magic_Mini), 0, null, ModMobEffects.MINI, SoundEvents.BEACON_AMBIENT, ParticleTypes.EFFECT)),
			SLOW = register(Strings.Magic_Slow, () -> new MagicStatusEffectRadius(KingdomKeys.rl(Strings.Magic_Slow), 0, null, MobEffects.MOVEMENT_SLOWDOWN, SoundEvents.BEACON_AMBIENT, ParticleTypes.EFFECT));

	public static Registry<Magic> registry = MAGIC.makeRegistry(builder -> builder.sync(true));
	public static int order = 0;

	private static KKSupplier<Magic> register(String name, Supplier<Magic> magicSupplier) {
		return new KKSupplier<>(MAGIC.register(name, magicSupplier));
	}
}
