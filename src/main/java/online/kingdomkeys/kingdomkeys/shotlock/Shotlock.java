package online.kingdomkeys.kingdomkeys.shotlock;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.KKRegistryObject;

import java.util.List;

public abstract class Shotlock implements KKRegistryObject {

	ResourceLocation name;
	int order;

	private ShotlockData data = new ShotlockData(4, 16, 1.0F, "");

	String translationKey;

	public Shotlock(ResourceLocation registryName, int order) {
		this.name = registryName;
		this.order = order;
		translationKey = "shotlock." + registryName.getPath() + ".name";
	}

	public Shotlock(String registryName, int order) {
		this(KingdomKeys.rl(registryName), order);
	}

	public void setShotlockData(ShotlockData data) {
		this.data = data;
	}

	public ShotlockData getShotlockData() {
		return data;
	}

	public String getTranslationKey() {
		return translationKey;
	}

	public int getCooldown() {
		return data.getCooldown();
	}

	public int getOrder() {
		return order;
	}

	public int getMaxLocks() {
		return data.getMax();
	}

	/** Which KKDamageTypes entry this Shotlock deals, or null for the default generic/blended damage
	 * (unset in the datapack, or an empty "element" field). */
	public ResourceKey<DamageType> getElement() {
		String element = data.getElement();
		if (element == null || element.isEmpty()) return null;
		return ResourceKey.create(Registries.DAMAGE_TYPE, KingdomKeys.rl(element));
	}

    public void onUse(Player player, List<Entity> targetList) {
		if(targetList.size() == getMaxLocks()){
			doFullShotlock(player,targetList);
		} else {
			doPartialShotlock(player,targetList);
		}
    }

	@Override
    public ResourceLocation getRegistryName() {
		return name;
	}

	public float getDamage(Player player){
		return (DamageCalculation.getMagicDamage(player)*0.7F + DamageCalculation.getStrengthDamage(player)*0.3F) * (float) ModConfigs.shotlockMult * data.getDmgMult();
	}

	public abstract void doPartialShotlock(Player player, List<Entity> targetList);
	public abstract void doFullShotlock(Player player, List<Entity> targetList);

}
