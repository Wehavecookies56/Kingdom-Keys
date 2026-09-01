package online.kingdomkeys.kingdomkeys.ability;

import net.minecraft.core.Registry;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;
import online.kingdomkeys.kingdomkeys.lib.KKSupplier;
import online.kingdomkeys.kingdomkeys.lib.Strings;

public class ModAbilities {

	public static DeferredRegister<Ability> ABILITIES = DeferredRegister.create(KingdomKeys.rl("abilities"), KingdomKeys.MODID);
	public static Registry<Ability> registry = ABILITIES.makeRegistry(builder -> builder.sync(true));

	public static int order = 0;

	public static final KKSupplier<Ability>
	//Action
			AUTO_VALOR = createAbility(Strings.autoValor, 1, AbilityType.ACTION),
			AUTO_WISDOM = createAbility(Strings.autoWisdom, 1, AbilityType.ACTION),
			AUTO_LIMIT = createAbility(Strings.autoLimit, 1, AbilityType.ACTION),
			AUTO_MASTER = createAbility(Strings.autoMaster, 1, AbilityType.ACTION),
			AUTO_FINAL = createAbility(Strings.autoFinal, 1, AbilityType.ACTION),
			STRIKE_RAID = createAbility(Strings.strikeRaid, 3, AbilityType.ACTION),
			GUARD = createAbility(Strings.guard, 2, AbilityType.ACTION),
			COUNTERGUARD = createAbility(Strings.counterguard, 3, AbilityType.ACTION),
			FLOWSTEP = createAbility(Strings.flowStep, 4, AbilityType.ACTION),

	// Growth
			HIGH_JUMP = createAbility(Strings.highJump, 2, AbilityType.GROWTH),
			QUICK_RUN = createAbility(Strings.quickRun, 2, AbilityType.GROWTH),
			DODGE_ROLL = createAbility(Strings.dodgeRoll, 3, AbilityType.GROWTH),
			AERIAL_DODGE = createAbility(Strings.aerialDodge, 3, AbilityType.GROWTH),
			GLIDE = createAbility(Strings.glide, 3, AbilityType.GROWTH),
			AIR_SLIDE = createAbility(Strings.airSlide, 3, AbilityType.GROWTH_STACKABLE),
			WALL_KICK = createAbility(Strings.wallKick, 2, AbilityType.GROWTH_STACKABLE),
			SUPERJUMP = createAbility(Strings.superJump, 3, AbilityType.GROWTH_STACKABLE),
			SUPERSLIDE = createAbility(Strings.superSlide, 2, AbilityType.GROWTH_STACKABLE),

	// Support
			ZERO_EXP = createAbility(Strings.zeroExp, 0, AbilityType.SUPPORT),
			SCAN = createAbility(Strings.scan, 1, AbilityType.SUPPORT),
			AERIAL_RECOVERY = createAbility(Strings.aerialRecovery, 2, AbilityType.SUPPORT),
			MAGIC_LOCK_ON = createAbility(Strings.magicLockOn, 4, AbilityType.SUPPORT),
			LEAF_BRACER = createAbility(Strings.leafBracer, 1, AbilityType.SUPPORT),
			HP_GAIN = createAbility(Strings.hpGain,6,AbilityType.SUPPORT),
			MP_SAFETY = createAbility(Strings.mpSafety, 0, AbilityType.SUPPORT),
			MP_HASTE = createAbility(Strings.mpHaste, 3, AbilityType.SUPPORT),
			MP_HASTERA = createAbility(Strings.mpHastera, 4, AbilityType.SUPPORT),
			MP_HASTEGA = createAbility(Strings.mpHastega, 5, AbilityType.SUPPORT),
			MP_RAGE = createAbility(Strings.mpRage, 3, AbilityType.SUPPORT),
			ENDLESS_MAGIC = createAbility(Strings.endlessMagic, 4, AbilityType.SUPPORT),
			DAMAGE_DRIVE = createAbility(Strings.damageDrive, 3, AbilityType.SUPPORT),
			DRIVE_CONVERTER = createAbility(Strings.driveConverter, 4, AbilityType.SUPPORT),
			FOCUS_CONVERTER = createAbility(Strings.focusConverter, 3, AbilityType.SUPPORT),
			DRIVE_BOOST = createAbility(Strings.driveBoost, 3, AbilityType.SUPPORT),
			FORM_BOOST = createAbility(Strings.formBoost, 3, AbilityType.SUPPORT),
			FULL_MP_BLAST = createAbility(Strings.fullMPBlast, 2, AbilityType.SUPPORT),
			MP_THRIFT = createAbility(Strings.mpThrift, 2, AbilityType.SUPPORT),
			LUCKY_STRIKE = createAbility(Strings.luckyStrike, 5, AbilityType.SUPPORT),
			JACKPOT = createAbility(Strings.jackpot, 4, AbilityType.SUPPORT),
			FIRE_BOOST = createAbility(Strings.fireBoost, 3, AbilityType.SUPPORT),
			BLIZZARD_BOOST = createAbility(Strings.blizzardBoost, 4, AbilityType.SUPPORT),
			WATER_BOOST = createAbility(Strings.waterBoost, 4, AbilityType.SUPPORT),
			THUNDER_BOOST = createAbility(Strings.thunderBoost, 5, AbilityType.SUPPORT),
			EXPERIENCE_BOOST = createAbility(Strings.experienceBoost, 4, AbilityType.SUPPORT),
            ENCOUNTER_PLUS = createAbility(Strings.encounterPlus, 5, AbilityType.SUPPORT),
			CRITICAL_BOOST = createAbility(Strings.criticalBoost, 3, AbilityType.SUPPORT),
			ITEM_BOOST = createAbility(Strings.itemBoost, 2, AbilityType.SUPPORT),
			TREASURE_MAGNET = createAbility(Strings.treasureMagnet, 3, AbilityType.SUPPORT),
			SECOND_CHANCE = createAbility(Strings.secondChance, 4, AbilityType.SUPPORT),
			ONCE_MORE = createAbility(Strings.onceMore, 4, AbilityType.SUPPORT),
			WIZARDS_RUSE = createAbility(Strings.wizardsRuse, 4, AbilityType.SUPPORT),
			EXTRA_CAST = createAbility(Strings.extraCast, 3, AbilityType.SUPPORT),
			DAMAGE_CONTROL = createAbility(Strings.damageControl, 5, AbilityType.SUPPORT),
			LIGHT_AND_DARKNESS = createAbility(Strings.lightAndDarkness, 2, AbilityType.SUPPORT),
			SYNCH_BLADE = createAbility(Strings.synchBlade, 5, AbilityType.SUPPORT),
			GRAND_MAGIC_HASTE = createAbility(Strings.grandMagicHaste, 3, AbilityType.SUPPORT),
			GRAND_MAGIC_EXTENDER = createAbility(Strings.grandMagicExtender, 3, AbilityType.SUPPORT),
			COMBO_PLUS = createAbility(Strings.comboPlus, 1, AbilityType.SUPPORT),
			NEGATIVE_COMBO = createAbility(Strings.negativeCombo, 1, AbilityType.SUPPORT),
			FINISHING_PLUS = createAbility(Strings.finishingPlus, 1, AbilityType.SUPPORT),
			BERSERK_CHARGE = createAbility(Strings.berserkCharge, 5, AbilityType.SUPPORT),
			DARK_DOMINATION = createAbility(Strings.darkDomination, 5, AbilityType.SUPPORT),
			PROTECT = createAbility(Strings.protect, 2, AbilityType.SUPPORT),
			PROTECTRA = createAbility(Strings.protectra,4, AbilityType.SUPPORT),
			PROTECTGA = createAbility(Strings.protectga,6, AbilityType.SUPPORT),
			
			
			FIRAZA = createAbility(Strings.firaza,3,AbilityType.SUPPORT),
			BLIZZAZA = createAbility(Strings.blizzaza,3,AbilityType.SUPPORT),
			WATERZA = createAbility(Strings.waterza,3,AbilityType.SUPPORT),
			THUNDAZA = createAbility(Strings.thundaza,3,AbilityType.SUPPORT),
			CURAZA = createAbility(Strings.curaza,3,AbilityType.SUPPORT);
		


	private static KKSupplier<Ability> createAbility(String name, int apCost, AbilityType type) {
		return createAbility(name, apCost, type, null);
	}

	private static KKSupplier<Ability> createAbility(String name, int apCost, AbilityType type, String exclusionGroup) {
		return new KKSupplier<>(KingdomKeys.rl(ABILITIES.getNamespace(), name), ABILITIES.register(name, () -> new Ability(KingdomKeys.rl(name), apCost, type, order++, exclusionGroup)));
	}
}