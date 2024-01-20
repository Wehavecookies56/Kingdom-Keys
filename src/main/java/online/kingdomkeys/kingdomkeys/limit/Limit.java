package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public abstract class Limit {

	ResourceLocation name;
	int order;

	String translationKey;

	OrgMember owner;
	private LimitData data;	


	public Limit(ResourceLocation registryName, int order, OrgMember owner) {
		this.name = registryName;
		this.order = order;
		this.owner = owner;
		translationKey = "limit." + registryName.getPath() + ".name";
	}

	public Limit(String registryName, int order, OrgMember owner) {
		this(new ResourceLocation(registryName), order, owner);
	}

	public String getName() {
		return name.toString();
	}

	public String getTranslationKey() {
		return translationKey;
	}

	public int getCost() {
		return data.getCost();
	}
	
	public int getCooldown() {
		return data.getCooldown();
	}
	
	public int getOrder() {
		return order;
	}

	public OrgMember getOwner() {
		return owner;
	}

	public abstract void onUse(Player player, LivingEntity target);
	
	public LimitData getLimitData() {
		return data;
	}

	public void setLimitData(LimitData data) {
		this.data = data;
	}

	public ResourceLocation getRegistryName() {
		return name;
	}

}