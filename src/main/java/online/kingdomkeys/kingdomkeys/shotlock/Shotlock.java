package online.kingdomkeys.kingdomkeys.shotlock;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class Shotlock extends ForgeRegistryEntry<Shotlock> {

	String name;
	int order;
	int cooldown;
	int max;

	String translationKey;


	public Shotlock(ResourceLocation registryName, int order, int cooldown, int max) {
		this.name = registryName.toString();
		this.cooldown = cooldown;
		this.max = max;
		setRegistryName(registryName);
		this.order = order;
		translationKey = "shotlock." + getRegistryName().getPath() + ".name";
	}

	public Shotlock(String registryName, int order, int cooldown, int max) {
		this(new ResourceLocation(registryName), order, cooldown, max);
	}

	public String getName() {
		return name;
	}
	
	public String getTranslationKey() {
		return translationKey;
	}
	
	public int getCooldown() {
		return cooldown;
	}
	
	public int getOrder() {
		return order;
	}
	
	public int getMaxLocks() {
		return max;
	}

	public abstract void onUse(PlayerEntity player, List<Entity> targetList);

}