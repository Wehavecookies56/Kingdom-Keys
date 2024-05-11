package online.kingdomkeys.kingdomkeys.shotlock;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;

public abstract class Shotlock {

	ResourceLocation name;
	int order;
	int cooldown;
	int max;

	String translationKey;


	public Shotlock(ResourceLocation registryName, int order, int cooldown, int max) {
		this.name = registryName;
		this.cooldown = cooldown;
		this.max = max;
		this.order = order;
		translationKey = "shotlock." + registryName.getPath() + ".name";
	}

	public Shotlock(String registryName, int order, int cooldown, int max) {
		this(new ResourceLocation(registryName), order, cooldown, max);
	}

	public String getName() {
		return name.toString();
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

    public void onUse(Player player, List<Entity> targetList) {
		if(targetList.size() == getMaxLocks()){
			doFullShotlock(player,targetList);
		} else {
			doPartialShotlock(player,targetList);
		}
    }

    public ResourceLocation getRegistryName() {
		return name;
	}

	public float getDamage(Player player){
		return (DamageCalculation.getMagicDamage(player)*0.7F + DamageCalculation.getStrengthDamage(player)*0.3F) * (float) ModConfigs.shotlockMult;
	}

	public abstract void doPartialShotlock(Player player, List<Entity> targetList);
	public abstract void doFullShotlock(Player player, List<Entity> targetList);

}