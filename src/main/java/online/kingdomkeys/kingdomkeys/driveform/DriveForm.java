package online.kingdomkeys.kingdomkeys.driveform;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public abstract class DriveForm {

	public static final ResourceLocation NONE = new ResourceLocation(KingdomKeys.MODID + ":none");
	public static final ResourceLocation SYNCH_BLADE = new ResourceLocation(KingdomKeys.MODID + ":synch_blade");

	// Level 0-7 (0 unused)
	public static final float[] VALOR_JUMP_BOOST = { 0, 0.02F, 0.02F, 0.03F, 0.03F, 0.04F, 0.04F, 0.06F };
	public static final float[] MASTER_AERIAL_DODGE_BOOST = { 0, 1, 1, 1.2F, 1.2F, 1.4F, 1.4F, 1.6F };
	public static final float[] FINAL_JUMP_BOOST = { 0, 0.02F, 0.02F, 0.025F, 0.025F, 0.03F, 0.03F, 0.055F };
	public static final float[] FINAL_GLIDE = { 0, -0.12F, -0.12F, -0.08F, -0.08F, -0.04F, -0.04F, -0.01F };
	public static final float[] FINAL_GLIDE_SPEED = { 0, 0.4F, 0.4F, 0.5F, 0.5F, 0.6F, 0.6F, 0.7F };
	
	ResourceLocation name;
	int maxLevel;
	int order;
	float[] color;
	ResourceLocation skinRL;
	boolean baseGrowth;
	
	String translationKey;

	boolean hasKeychain = false;

	private DriveFormData data;	

	public DriveForm(ResourceLocation registryName, int order, boolean hasKeychain, boolean baseGrowth) {
		this.name = registryName;
		this.maxLevel = 7;
		this.order = order;
		this.hasKeychain = hasKeychain;
		translationKey = "form." + registryName.getPath() + ".name";
		this.baseGrowth = baseGrowth;
	}

	public DriveForm(String registryName, int order, boolean hasKeychain, boolean baseGrowth) {
		this(new ResourceLocation(registryName), order, hasKeychain, baseGrowth);
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

	public String getName() {
		return name.toString();
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
	
	public final float[] getDriveColor() {
		return color;
	}
	
	public ResourceLocation getTextureLocation() {
		return skinRL;
	}

	public String getBaseAbilityForLevel(int driveFormLevel) {
		if(driveFormLevel < 1)
			return "";
		return data.getBaseAbilityForLevel(driveFormLevel-1); //-1 so we don't have empty "" at the beginning of the file
	}

	public String getDFAbilityForLevel(int driveFormLevel) {
		if(driveFormLevel < 1)
			return "";
		return data.getDFAbilityForLevel(driveFormLevel-1);
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
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.setActiveDriveForm(getName());
			int cost = ModDriveForms.registry.get().getValue(new ResourceLocation(getName())).getDriveCost();
			playerData.remDP(cost);
			playerData.setFP(300 + playerData.getDriveFormLevel(playerData.getActiveDriveForm()) * 100);
			playerData.setAntiPoints(playerData.getAntiPoints() + getFormAntiPoints());
			player.heal(ModConfigs.driveHeal * player.getMaxHealth() / 100);
			
			// Summon Keyblades
			player.level.playSound(player, player.blockPosition(), ModSounds.drive.get(), SoundSource.MASTER, 1.0f, 1.0f);
			pushEntities(player);
			PacketHandler.syncToAllAround(player, playerData);
		}
	}

	private void pushEntities(Player player) {
		List<Entity> list = player.level.getEntities(player, player.getBoundingBox().inflate(4.0D, 3.0D, 4.0D));
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Entity e = (Entity) list.get(i);
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
		if (!getRegistryName().equals(NONE)) {
			double formDecrease = 0.2;
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			for (int i = 0; i < playerData.getNumberOfAbilitiesEquipped(Strings.formBoost); i++) {
				formDecrease /= 1.2;
			}
			if (playerData.getFP() > 0) {
				playerData.setFP(playerData.getFP() - formDecrease);
			} else {
				endDrive(player);
			}
		}
	}

	public void endDrive(Player player) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setActiveDriveForm(DriveForm.NONE.toString());
		player.level.playSound(player, player.blockPosition(), ModSounds.unsummon.get(), SoundSource.MASTER, 1.0f, 1.0f);
		if(!player.level.isClientSide) {
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

	public ResourceLocation getRegistryName() {
		return name;
	}

}