package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.resources.ResourceLocation;

import java.util.EnumSet;
import java.util.Set;

/**
 * Stores the data loaded from the magics datapack
 */
public class MagicData {

	private float dmgMult;
	private float dmgMultMax;
	private int cost;
	private int ct;
	private int cd;
	private boolean magicLockOn;
	private int maxExp;
	private int maxLevel;
	private ResourceLocation nextTier, magicRC;
	private SpellType spellType;
	private final Set<Interaction> interactions = EnumSet.noneOf(Interaction.class);

	public enum SpellType {
		PHYSICAL,
		MAGIC
	}

	public enum Interaction {
		LIGHT_LIGHTABLE("light_lightable"),
		TURN_OFF_LIGHTABLE("turn_off_lightable"),
		LIGHT_PORTAL("light_portal"),
		LIGHT_TNT("light_tnt"),
		EXTINGUISH_TNT("extinguish_tnt"),
		EXTINGUISH_FIRE("extinguish_fire"),
		DRY_SPONGE("dry_sponge"),
		WET_SPONGE("wet_sponge"),
		FREEZE_WATER("freeze_water"),
		FREEZE_LAVA("freeze_lava");

		private final String name;

		Interaction(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static Interaction byName(String name) {
			for (Interaction interaction : values()) {
				if (interaction.name.equalsIgnoreCase(name.trim())) {
					return interaction;
				}
			}
			return null;
		}
	}

	public Set<Interaction> getInteractions() {
		return interactions;
	}

	public boolean canInteract(Interaction interaction) {
		return interactions.contains(interaction);
	}

	public void addInteraction(Interaction interaction) {
		interactions.add(interaction);
	}

	public MagicData() {}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getCasttime() {
		return ct;
	}

	public void setCasttime(int ct) {
		this.ct = ct;
	}

	public int getCooldown() {
		return cd;
	}

	public void setCooldown(int cd) {
		this.cd = cd;
	}

	public float getDmgMult() {
		return dmgMult;
	}

	public void setDmgMult(float dmgMult) {
		this.dmgMult = dmgMult;
	}

	public float getDmgMultMax() {
		return dmgMultMax > 0 ? dmgMultMax : dmgMult;
	}

	public void setDmgMultMax(float dmgMultMax) {
		this.dmgMultMax = dmgMultMax;
	}

	public boolean getMagicLockOn() {
		return magicLockOn;
	}

	public void setMagicLockon(boolean lockOn) {
		this.magicLockOn = lockOn;
	}

	public int getMaxExp() {
		return maxExp;
	}

	public void setMaxExp(int maxExp) {
		this.maxExp = maxExp;
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int level) {
		this.maxLevel = level;
	}

	public ResourceLocation getNextTier() {
		return nextTier;
	}

	public void setNextTier(ResourceLocation nextTier) {
		this.nextTier = nextTier;
	}

	public ResourceLocation getMagicRC() {
		return magicRC;
	}

	public void setMagicRC(ResourceLocation rc) {
		this.magicRC = rc;
	}

	public SpellType getSpellType() {
		return spellType;
	}

	public void setSpellType(SpellType spellType) {
		this.spellType = spellType;
	}
}