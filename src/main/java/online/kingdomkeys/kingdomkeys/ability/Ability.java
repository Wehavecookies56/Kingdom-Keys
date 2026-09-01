package online.kingdomkeys.kingdomkeys.ability;

import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.lib.KKRegistryObject;

public class Ability implements KKRegistryObject {

	public enum AbilityType{
		ACTION, GROWTH, GROWTH_STACKABLE, SUPPORT, WEAPON, ACCESSORY
	}
	
    ResourceLocation name;
    int apCost;
    AbilityType type;
    int order;
    String translationKey;
    String exclusionGroup;

    private AbilityData data;

    public Ability(ResourceLocation registryName, int apCost, AbilityType type, int order) {
    	this(registryName, apCost, type, order, null);
    }

    public Ability(ResourceLocation registryName, int apCost, AbilityType type, int order, String exclusionGroup) {
    	this.name = registryName;
    	this.apCost = apCost;
    	this.type = type;
    	this.order = order;
    	this.exclusionGroup = exclusionGroup == null || exclusionGroup.isBlank() ? null : exclusionGroup;
        translationKey = "ability." + registryName.getPath() + ".name";
    }

    public String getTranslationKey() {
    	return translationKey;
	}

	public String getTranslationKey(int level) {
		if (level > 0) {
			return translationKey.replace(".name", "_" + level + ".name");
		} else {
			return getTranslationKey();
		}
	}

	public String getDescTranslationKey() {
		return translationKey.replace(".name", ".desc");
	}

	public AbilityData getAbilityData() {
		return data;
	}

	public void setAbilityData(AbilityData data) {
		this.data = data;
	}

    public int getAPCost() {
    	return data != null && data.getAPCost() != null ? data.getAPCost() : apCost;
    }

	/**
	 * Abilities sharing a group are mutually exclusive: only one of them can be equipped at a time, and
	 * the rest grey out the same way an ability you cannot afford does. Null means no restriction.
	 */
	public String getExclusionGroup() {
		return data != null && data.getExclusionGroup() != null ? data.getExclusionGroup() : exclusionGroup;
	}

	/** True when this ability and the other one cannot both be equipped. */
	public boolean conflictsWith(Ability other) {
		String group = getExclusionGroup();

		return group != null
				&& other != null
				&& other != this
				&& group.equals(other.getExclusionGroup());
	}


	public int getOrder() {
		return data != null && data.getOrder() != null ? data.getOrder() : order;
	}

	public AbilityType getType() {
		return data != null && data.getType() != null ? data.getType() : type;
	}

	@Override
	public String toString() {
		return name.toString();
	}

	@Override
	public ResourceLocation getRegistryName() {
		return name;
	}

	public int compareTo(Ability other) {
		// Through the getters so a datapack reordering or retyping an ability actually moves it
		int typeOrder = getType().compareTo(other.getType());
		return typeOrder == 0 ? Integer.compare(getOrder(), other.getOrder()) : typeOrder;
	}
}