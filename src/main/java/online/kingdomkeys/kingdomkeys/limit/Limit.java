package online.kingdomkeys.kingdomkeys.limit;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistryEntry;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

public abstract class Limit extends ForgeRegistryEntry<Limit> {

	String name;
	int order;
	int cost;
	int cooldown;

	String translationKey;

	OrgMember owner;

	public Limit(ResourceLocation registryName, int order, int cost, int cooldown, OrgMember owner) {
		this.name = registryName.toString();
		this.cost = cost;
		this.cooldown = cooldown;
		setRegistryName(registryName);
		this.order = order;
		this.owner = owner;
		translationKey = "limit." + getRegistryName().getPath() + ".name";
	}

	public Limit(String registryName, int order, int cost, int cooldown, OrgMember owner) {
		this(new ResourceLocation(registryName), order, cost, cooldown, owner);
	}

	public String getName() {
		return name;
	}

	public String getTranslationKey() {
		return translationKey;
	}

	public int getCost() {
		return cost;
	}
	
	public int getCooldown() {
		return cooldown;
	}
	
	public int getOrder() {
		return order;
	}

	public OrgMember getOwner() {
		return owner;
	}

	public abstract void onUse(Player player, LivingEntity target);

}