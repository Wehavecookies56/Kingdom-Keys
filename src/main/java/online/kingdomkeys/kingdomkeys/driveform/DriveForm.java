package online.kingdomkeys.kingdomkeys.driveform;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.registries.ForgeRegistryEntry;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public abstract class DriveForm extends ForgeRegistryEntry<DriveForm> {

	public static final ResourceLocation NONE = new ResourceLocation(KingdomKeys.MODID + ":none");

	// Level 0-7 (0 unused)
	public static final float[] VALOR_JUMP_BOOST = { 0, 0.02F, 0.02F, 0.03F, 0.03F, 0.04F, 0.04F, 0.05F };
	public static final float[] MASTER_AERIAL_DODGE_BOOST = { 0, 1, 1, 1.2F, 1.2F, 1.4F, 1.4F, 1.6F };
	public static final float[] FINAL_JUMP_BOOST = { 0, 0.02F, 0.02F, 0.025F, 0.025F, 0.03F, 0.03F, 0.04F };
	public static final float[] FINAL_GLIDE = { 0, -0.12F, -0.12F, -0.08F, -0.08F, -0.04F, -0.04F, -0.01F };
	String name;
	int driveCost;
	int ap;
	int[] levelUpCosts;// {0,X,X,X,X,X,X}
	int maxLevel;
	int order;
	float[] color;
	ResourceLocation skinRL;
	
	String translationKey;

	boolean hasKeychain = false;

	public DriveForm(ResourceLocation registryName, int order, boolean hasKeychain) {
		this.name = registryName.toString();
		this.maxLevel = 7;
		setRegistryName(registryName);
		this.order = order;
		this.hasKeychain = hasKeychain;
		translationKey = "form." + getRegistryName().getPath() + ".name";
	}

	public DriveForm(String registryName, int order, boolean hasKeychain) {
		this(new ResourceLocation(registryName), order, hasKeychain);
	}

	public boolean hasKeychain() {
		return hasKeychain;
	}

	public String getName() {
		return name;
	}

	public String getTranslationKey() { return translationKey; }

	public int getDriveCost() {
		return driveCost;
	}

	public int getFormAntiPoints() {
		return ap;
	}

	public int[] getLevelUpCosts() {
		return levelUpCosts;
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

	public abstract String getBaseAbilityForLevel(int driveFormLevel);

	public abstract String getDFAbilityForLevel(int driveFormLevel); // TODO make the ability registry

	public int getLevelUpCost(int level) {
		if (levelUpCosts != null)
			return levelUpCosts[level - 1];
		else
			return -1;
	}

	public int getLevelFromExp(int exp) {
		for (int i = 0; i < levelUpCosts.length; i++) {
			if (levelUpCosts[i] > exp) {
				return i;
			}
		}
		return getMaxLevel();
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void initDrive(PlayerEntity player) {
		if (!getRegistryName().equals(NONE)) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.setActiveDriveForm(getName());
			int cost = ModDriveForms.registry.getValue(new ResourceLocation(getName())).getDriveCost();
			playerData.remDP(cost);
			playerData.setFP(300 + playerData.getDriveFormLevel(playerData.getActiveDriveForm()) * 100);
			// Summon Keyblades
			playerData.setAntiPoints(playerData.getAntiPoints() + getFormAntiPoints());
			player.world.playSound(player, player.getPosition(), ModSounds.drive.get(), SoundCategory.MASTER, 1.0f, 1.0f);
			pushEntities(player);
			PacketHandler.syncToAllAround(player, playerData);
		}
	}

	private void pushEntities(PlayerEntity player) {
		List<Entity> list = player.world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(4.0D, 3.0D, 4.0D));
		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Entity e = (Entity) list.get(i);
				if (e instanceof LivingEntity) {
					double d = e.getPosX() - player.getPosX();
					double d1 = e.getPosZ() - player.getPosZ();
					((LivingEntity) e).applyKnockback(1, -d, -d1);
					e.setMotion(e.getMotion().x, 0.7F, e.getMotion().z);
				}
			}
		}
	}

	public void updateDrive(PlayerEntity player) {
		double formDecrease = 0.2;
		if (!getRegistryName().equals(NONE)) {
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			if(playerData.isAbilityEquipped(Strings.formBoost))
				formDecrease = .15;
			if (playerData.getFP() > 0) {
				playerData.setFP(playerData.getFP() - formDecrease);
			} else {
				endDrive(player);
			}
		}
		// Consume FP
		// Check if FP <= 0 then end
	}

	public void endDrive(PlayerEntity player) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		playerData.setActiveDriveForm(DriveForm.NONE.toString());
		player.world.playSound(player, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
		if(!player.world.isRemote) {
			PacketHandler.syncToAllAround(player, playerData);
		}
	}

}