package online.kingdomkeys.kingdomkeys.ability;

import online.kingdomkeys.kingdomkeys.ability.Ability.AbilityType;

public class AbilityData {
	private Integer apCost;
	private AbilityType type;
	private Integer order;
	private String exclusionGroup;

	public AbilityData() {}

	public Integer getAPCost() {
		return apCost;
	}

	public void setAPCost(Integer apCost) {
		this.apCost = apCost;
	}

	public AbilityType getType() {
		return type;
	}

	public void setType(AbilityType type) {
		this.type = type;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getExclusionGroup() {
		return exclusionGroup;
	}

	public void setExclusionGroup(String exclusionGroup) {
		this.exclusionGroup = exclusionGroup == null || exclusionGroup.isBlank() ? null : exclusionGroup;
	}
}
