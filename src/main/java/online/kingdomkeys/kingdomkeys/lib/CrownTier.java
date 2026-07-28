package online.kingdomkeys.kingdomkeys.lib;

import online.kingdomkeys.kingdomkeys.data.PlayerData;

public enum CrownTier {
	BRONZE("bronze"),
	SILVER("silver"),
	GOLD("gold");

	/** Texture name under textures/models/crown/, and what gets stored in the player data. */
	private final String name;

	CrownTier(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public String getTranslationKey() {
		return "kingdomkeys.crown." + this.name;
	}

	public static CrownTier byName(String name) {
		for (CrownTier tier : values()) {
			if (tier.name.equals(name)) {
				return tier;
			}
		}
		return null;
	}

	public static CrownTier next(PlayerData playerData) {
		for (CrownTier tier : values()) {
			if (!playerData.hasUnlockedCrown(tier.name)) {
				return tier;
			}
		}
		return null;
	}
}
