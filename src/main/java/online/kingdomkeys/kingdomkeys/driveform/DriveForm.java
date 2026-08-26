package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.event.AbilityEvent;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.KKRegistryObject;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

import java.util.List;
import java.util.Optional;

public abstract class DriveForm implements KKRegistryObject {

	public static final ResourceLocation NONE = KingdomKeys.rl("none");
	public static final ResourceLocation KB2 = KingdomKeys.rl("kb2");
	public static final ResourceLocation KB3 = KingdomKeys.rl("kb3");
	public static final ResourceLocation SYNCH_BLADE = KingdomKeys.rl("synch_blade");

	// Level 0-7 (0 unused)
	public static final float[] VALOR_JUMP_BOOST = { 0, 0.02F, 0.02F, 0.03F, 0.03F, 0.04F, 0.04F, 0.06F };
	public static final float[] MASTER_AERIAL_DODGE_BOOST = { 0, 1, 1, 1.2F, 1.2F, 1.4F, 1.4F, 1.6F };
	public static final float[] FINAL_JUMP_BOOST = { 0, 0.02F, 0.02F, 0.025F, 0.025F, 0.03F, 0.03F, 0.055F };
	public static final float[] FINAL_GLIDE = { 0, -0.09F, -0.09F, -0.06F, -0.06F, -0.03F, -0.03F, -0.01F };
	public static final float[] FINAL_GLIDE_SPEED = { 0, 1.8F, 1.8F, 2.8F, 2.8F, 3.6F, 3.6F, 5F };

	public boolean isFakeForm;
	ResourceLocation name;
	int maxLevel;
	int order;
	public float[] color;
	public ResourceLocation skinRL;
	boolean baseGrowth;
	
	String translationKey;

	boolean hasKeychain = false;

	private DriveFormData data = new DriveFormData();

	public DriveForm(ResourceLocation registryName, int order, boolean hasKeychain, boolean baseGrowth) {
		this.name = registryName;
		this.maxLevel = 7;
		this.order = order;
		this.hasKeychain = hasKeychain;
		translationKey = "form." + registryName.getPath() + ".name";
		this.baseGrowth = baseGrowth;
	}

	public DriveForm(String registryName, int order, boolean hasKeychain, boolean baseGrowth) {
		this(KingdomKeys.rl(registryName), order, hasKeychain, baseGrowth);
	}

	public boolean isFakeForm(){
		return this.isFakeForm;
	}
	
	public void setDriveFormData(DriveFormData data) {
        this.data = data;
    }

    public DriveFormData getDriveFormData() {
        return data;
    }

	public boolean hasKeychain() {
		return hasKeychain;
	}

	public String getTranslationKey() {
		return translationKey;
	}

	public int getDriveCost() {
		return data.getCost();
	}

	public int getFormAntiPoints() {
		return data.getAP();
	}

	public int[] getLevelUpCosts() {
		if(data != null)
			return data.getLevelUp();
		else {
			return new int[0];
		}
	}
	
	public int getOrder() {
		return order;
	}
	
	public float[] getDriveColor() {
		return color;
	}
	
	public ResourceLocation getTextureLocation(Player player) {
		return skinRL;
	}

	public Optional<ResourceLocation> getBaseAbilityForLevel(int driveFormLevel) {
		if(driveFormLevel < 1)
			return Optional.empty();
		ResourceLocation ability = data.getBaseAbilityForLevel(driveFormLevel - 1);
		if(ability.getPath().isEmpty())
			return Optional.empty();
		return Optional.of(ability);
	}

	public Optional<ResourceLocation> getDFAbilityForLevel(int driveFormLevel) {
		if(driveFormLevel < 1)
			return Optional.empty();
		ResourceLocation ability = data.getDFAbilityForLevel(driveFormLevel - 1);
		if(ability.getPath().isEmpty())
			return Optional.empty();
		return Optional.of(ability);
	}
	
	public int getLevelUpCost(int level) {
		if (getLevelUpCosts() != null)
			return getLevelUpCosts()[level - 1];
		else
			return -1;
	}

	public int getLevelFromExp(int exp) {
		for (int i = 0; i < getLevelUpCosts().length; i++) {
			if (getLevelUpCosts()[i] > exp) {
				return i;
			}
		}
		return getMaxLevel();
	}

	public int getMaxLevel() {
		return maxLevel;
	}
	
	public boolean getBaseGrowthAbilities() {
		return baseGrowth;
	}
	
	public void setBaseGrowthAbilities(boolean growthAbilities) {
		this.baseGrowth = growthAbilities;
	}

	public void initDrive(Player player) {
		if (!getRegistryName().equals(NONE)) {
            PlayerData playerData = PlayerData.get(player);
			playerData.setActiveDriveForm(getRegistryName());
			int cost = ModDriveForms.registry.get(getRegistryName()).getDriveCost();
			playerData.remDP(cost);
			playerData.setFP(300 + playerData.getDriveFormLevel(playerData.getActiveDriveForm()) * 100);
			playerData.setAntiPoints(playerData.getAntiPoints() + getFormAntiPoints());
			player.heal(ModConfigs.driveHeal * player.getMaxHealth() / 100);
			playerData.setMP(playerData.getMaxMP());
			
			// Summon Keyblades
			if(getDriveSound() != null)
				player.level().playSound(null, player.blockPosition(), getDriveSound(), SoundSource.MASTER, 1.0f, 1.0f);

			pushEntities(player);

			if (!getBaseGrowthAbilities()) {
				getDFAbilityForLevel(playerData.getDriveFormLevel(getRegistryName())).ifPresent(location -> {
					NeoForge.EVENT_BUS.post(new AbilityEvent.Equip(ModAbilities.registry.get(location), playerData.getDriveFormLevel(getRegistryName()), player, false));
				});
			}
			for (ResourceLocation abilityLoc : getDriveFormData().getAbilities()) {
				Ability ability = ModAbilities.registry.get(abilityLoc);
				NeoForge.EVENT_BUS.post(new AbilityEvent.Equip(ability, 0, player, false));
			}
			PacketHandler.syncToAllAround(player, playerData);
		}
	}

	public SoundEvent getDriveSound() {
		return ModSounds.drive.get();
	}

	public SoundEvent getRevertSound() {
		return ModSounds.revert.get();
	}

	public void pushEntities(Player player) {
		List<Entity> list = player.level().getEntities(player, player.getBoundingBox().inflate(4.0D, 3.0D, 4.0D));
		if (!list.isEmpty()) {
            for (Entity e : list) {
                if (e instanceof LivingEntity) {
                    double d = e.getX() - player.getX();
                    double d1 = e.getZ() - player.getZ();
                    ((LivingEntity) e).knockback(1, -d, -d1);
                    e.setDeltaMovement(e.getDeltaMovement().x, 0.7F, e.getDeltaMovement().z);
                }
            }
		}
	}

	public void updateDrive(Player player) {
		if (getRegistryName().equals(NONE))
			return;

		double formDecrease = 0.2;
		PlayerData playerData = PlayerData.get(player);
		int driveBoosts = playerData.getNumberOfAbilitiesEquipped(ModAbilities.FORM_BOOST);
		for (int i = 0; i < driveBoosts; i++) {
			formDecrease /= 1.2;
		}
		if (playerData.getFP() > 0) {
			playerData.setFP(playerData.getFP() - formDecrease);
		} else {
			endDrive(player);
		}
	}

	public void endDrive(Player player) {
		PlayerData playerData = PlayerData.get(player);
		playerData.setActiveDriveForm(DriveForm.NONE);
		if(getDriveSound() != null)
			player.level().playSound(null, player.blockPosition(), getRevertSound(), SoundSource.MASTER, 1.0f, 1.0f);

		if(!getRegistryName().equals(ModDriveForms.ANTI.location())) {
			if (!getBaseGrowthAbilities()) {
				getDFAbilityForLevel(playerData.getDriveFormLevel(getRegistryName())).ifPresent(location -> {
					Ability ability = ModAbilities.registry.get(location);
					if(ability != null) {
						NeoForge.EVENT_BUS.post(new AbilityEvent.Unequip(ability, playerData.getDriveFormLevel(getRegistryName()), player, false));
					}
				});
			}
			for (ResourceLocation abilityLoc : getDriveFormData().getAbilities()) {
				Ability ability = ModAbilities.registry.get(abilityLoc);
				if(ability != null) {
					NeoForge.EVENT_BUS.post(new AbilityEvent.Unequip(ability, 0, player, false));
				}
			}
		}

		if(!player.level().isClientSide) {
			PacketHandler.syncToAllAround(player, playerData);
		}
	}

	public float getStrMult() {
		return data.strMult;
	}
	
	public float getMagMult() {
		return data.magMult;
	}
	public float getSpeedMult() {
		return data.speedMult;
	}

	@Override
	public ResourceLocation getRegistryName() {
		return name;
	}

	public boolean canGoAnti() {
		return data.canGoAnti;
	}
	
	public boolean canUseMagic() {
		return data.canUseMagic;
	}

	/**
	 * Make this slot show up in equipment inventory
	 * @param player
	 * @return
	 */
	public boolean isSlotVisible(Player player) {
		return hasKeychain();
	}

	public boolean displayInCommandMenu(Player player) {
		return true;
	}

}