package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.lib.Utils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.packet.PacketSyncCapability;

public class DriveForm extends ForgeRegistryEntry<DriveForm> {
	// Level 0-7 (0 unused)
	public static final float[] MASTER_AERIAL_DODGE_BOOST = { 0, 1, 1, 1.2F, 1.2F, 1.4F, 1.4F, 1.6F };
	public static final float[] FINAL_GLIDE = { 0, -0.1F, -0.1F, -0.06F, -0.06F, -0.01F, -0.01F, -0.005F };
	String name;
	int driveCost;
	String growthAbility;
	int antiPoints;
	int[] levelUpCosts;// {0,X,X,X,X,X,X}

	public DriveForm() {}
	
	public DriveForm(String registryName, int cost, String growthAbility, int antiPoints, int[] levelUpCosts) {
		this.name = registryName;
		this.driveCost = cost;
		this.growthAbility = growthAbility;
		this.antiPoints = antiPoints;
		this.levelUpCosts = levelUpCosts;
		setRegistryName(registryName);
	}

	public String getName() {
		return name;
	}

	public int getDriveCost() {
		return driveCost;
	}

	public int getFormAntiPoints() {
		return antiPoints;
	}

	public int[] getLevelUpCosts() {
		return levelUpCosts;
	}

	public int getLevelUpCost(int level) {
		return levelUpCosts[level-1];
	}

	public int getLevelFromExp(int exp) {
		for (int i = 0; i < levelUpCosts.length; i++) {
			if (levelUpCosts[i] > exp) {
				return i;
			}
		}
		return -1;
	}
	
	public String getGrowthAbility() {
		return growthAbility;
	}

	public void initDrive(PlayerEntity player) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		props.setActiveDriveForm(getName());
		int cost = ModDriveForms.registry.getValue(new ResourceLocation(getName())).getDriveCost();
		props.remDP(cost);
		props.setFP(200 + Utils.getDriveFormLevel(props.getDriveFormsMap(), props.getActiveDriveForm()) * 100);
		// Summon Keyblades
		props.setAntiPoints(props.getAntiPoints() + getFormAntiPoints());
		player.world.playSound(player, player.getPosition(), ModSounds.drive.get(), SoundCategory.MASTER, 1.0f, 1.0f);
		PacketHandler.syncToAllAround(player, props);
	}

	public void updateDrive(PlayerEntity player) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		if (props.getFP() > 0) {
			props.setFP(props.getFP() - 1);
		} else {
			endDrive(player);
		}
		// Consume FP
		// Check if FP <= 0 then end
	}

	public void endDrive(PlayerEntity player) {
		IPlayerCapabilities props = ModCapabilities.get(player);
		props.setActiveDriveForm("");
		player.world.playSound(player, player.getPosition(), ModSounds.unsummon.get(), SoundCategory.MASTER, 1.0f, 1.0f);
		PacketHandler.syncToAllAround(player, props);
	}

	
	@SubscribeEvent
	public void getValorFormXP(LivingAttackEvent event) {
		if (!event.getEntity().world.isRemote) { //TODO Check the target is hostile
			if (event.getSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
				IPlayerCapabilities props = ModCapabilities.get(player);

				if (props.getActiveDriveForm().equals(Strings.Form_Valor)) {
					props.setDriveFormExp(props.getActiveDriveForm(), props.getDriveFormExp(props.getActiveDriveForm()) + 1);
					PacketHandler.sendTo(new PacketSyncCapability(props), (ServerPlayerEntity)player);
				}
			}
		}
	}
	@SubscribeEvent
	public void getFinalFormXP(LivingDeathEvent event) {
		if (!event.getEntity().world.isRemote && event.getEntity() instanceof EndermanEntity) {
			if (event.getSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
				IPlayerCapabilities props = ModCapabilities.get(player);

				if (props.getActiveDriveForm().equals(Strings.Form_Final)) {
					props.setDriveFormExp(props.getActiveDriveForm(), props.getDriveFormExp(props.getActiveDriveForm()) + 1);

					/*int[] costs = DriveFormRegistry.get(DRIVE.getActiveDriveName()).getExpCosts();
					int actualLevel = DRIVE.getDriveLevel(DRIVE.getActiveDriveName());
					int actualExp = DRIVE.getDriveExp(DRIVE.getActiveDriveName());

					if (costs.length == 7 && actualLevel < 7) {
						if (actualExp >= costs[actualLevel]) {
							DRIVE.setDriveLevel(DRIVE.getActiveDriveName(), actualLevel + 1);
							if (DRIVE.getDriveLevel(Strings.Form_Final) == 3)
								player.getCapability(ModCapabilities.ABILITIES, null).unlockAbility(ModAbilities.glide);
							DRIVE.displayLevelUpMessage(player, DRIVE.getActiveDriveName());

							if (actualLevel + 1 == 7) {
								DRIVE.setDriveGaugeLevel(DRIVE.getDriveGaugeLevel() + 1);
								DRIVE.setDP(DRIVE.getMaxDP());
							}
						}
					}
					PacketDispatcher.sendTo(new SyncDriveData(DRIVE), (EntityPlayerMP) player);
					PacketDispatcher.sendTo(new SyncUnlockedAbilities(player.getCapability(ModCapabilities.ABILITIES, null)), (EntityPlayerMP) player);*/
				}
			}
		}
	}

	

}