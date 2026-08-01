package online.kingdomkeys.kingdomkeys.shotlock;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.ShotlockItem;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.KKRegistryObject;
import online.kingdomkeys.kingdomkeys.shotlock.minigame.ShotlockMinigameType;

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

	/** Same idea as getRealDamageMult(...) - interpolates between the base cooldown and the fastest one
	 * (data.getCooldownMin()) based on the equipped item's current level, so a leveled-up Shotlock
	 * locks on faster. Falls back to the flat getCooldown() if this Shotlock doesn't level up. */
	public int getRealCooldown(Player player) {
		if (getMaxLevel() <= 1) {
			return getCooldown();
		}

		PlayerData playerData = PlayerData.get(player);
		ItemStack equipped = playerData.getEquippedShotlock();

		int localLevel = 1;
		if (equipped != null && equipped.getItem() instanceof ShotlockItem shotlockItem && shotlockItem.getShotlock().equals(getRegistryName())) {
			localLevel = shotlockItem.getLocalLevel(equipped);
		}

		float t = (float) (localLevel - 1) / (getMaxLevel() - 1);
		int base = getCooldown();
		int min = data.getCooldownMax();
		return Math.max(1, Math.round(base + (min - base) * t));
	}

	public int getOrder() {
		return order;
	}

	public int getMaxLocks() {
		return data.getMax();
	}

	/** Total exp needed to go from level 1 to getMaxLevel(). */
	public int getMaxExp() {
		return data.getMaxExp();
	}

	/** How many levels this Shotlock's item can reach - 1 means it doesn't level up at all. */
	public int getMaxLevel() {
		return data.getMaxLevel();
	}

	public float getDamageMult() {
		return data.getDmgMult();
	}

	public float getDamageMultMax() {
		return data.getDmgMultMax();
	}

	/** Same idea as Magic.getRealDamageMult(...) - interpolates between getDamageMult() and
	 * getDamageMultMax() based on the equipped item's current level, so a Shotlock that's been used
	 * enough actually hits harder. Falls back to the flat getDamageMult() if this Shotlock doesn't
	 * level up (getMaxLevel() <= 1), or if the player doesn't have this specific Shotlock equipped for
	 * whatever reason. */
	public float getRealDamageMult(Player player) {
		if (getMaxLevel() <= 1) {
			return getDamageMult();
		}

		PlayerData playerData = PlayerData.get(player);
		net.minecraft.world.item.ItemStack equipped = playerData.getEquippedShotlock();

		int localLevel = 1;
		if (equipped != null && equipped.getItem() instanceof ShotlockItem shotlockItem && shotlockItem.getShotlock().equals(getRegistryName())) {
			localLevel = shotlockItem.getLocalLevel(equipped);
		}

		float t = (float) (localLevel - 1) / (getMaxLevel() - 1);
		float base = getDamageMult();
		float max = getDamageMultMax();

		return base + (max - base) * t;
	}

	public ShotlockMinigameType getMinigameType() {
		return ShotlockMinigameType.parse(data.getMinigame());
	}

	// True when this Shotlock's mash minigame charges targets instead of throwing shots
	public boolean minigameUsesDash() {
		return ShotlockMinigameType.isDashVariant(data.getMinigame());
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
		return (DamageCalculation.getMagicDamage(player)*0.7F + DamageCalculation.getStrengthDamage(player)*0.3F) * (float) ModConfigs.shotlockMult * getRealDamageMult(player);
	}

	public abstract void doPartialShotlock(Player player, List<Entity> targetList);
	public abstract void doFullShotlock(Player player, List<Entity> targetList);

}
