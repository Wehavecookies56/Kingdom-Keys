package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.integration.epicfight.EpicFightEvents;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

import java.util.Optional;

public class DriveFormAnti extends DriveForm {

	public DriveFormAnti(ResourceLocation registryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowth) {
		super(registryName, order, hasKeychain, baseGrowth);
		this.color = new float[] { 0F, 0F, 0F };
		this.skinRL = skinRL;
	}

	@Override
	public Optional<ResourceLocation> getBaseAbilityForLevel(int driveFormLevel) {
		return Optional.empty();
	}

	@Override
	public Optional<ResourceLocation> getDFAbilityForLevel(int driveFormLevel) {
		return Optional.empty();
	}

	@Override
	public boolean displayInCommandMenu(Player player) {
		return PlayerData.get(player).isAbilityEquipped(ModAbilities.DARK_DOMINATION);
	}

	@Override
	public void initDrive(Player player) {
		if (!getRegistryName().equals(NONE)) {
			PlayerData playerData = PlayerData.get(player);
			playerData.setActiveDriveForm(getRegistryName());
			int cost = 1000;
			if(playerData.isAbilityEquipped(ModAbilities.DARK_DOMINATION)){
				cost = this.getDriveCost();
			}
			playerData.remDP(cost);
			playerData.setFP(1000);
			playerData.setAntiPoints(playerData.getAntiPoints() + getFormAntiPoints());
			player.heal(ModConfigs.driveHeal * player.getMaxHealth() / 100);
			playerData.setMP(playerData.getMaxMP());

			if(getDriveSound() != null)
				player.level().playSound(null, player.blockPosition(), getDriveSound(), SoundSource.MASTER, 1.0f, 1.0f);
			pushEntities(player);
			if (KingdomKeys.efmLoaded) {
				EpicFightEvents.refreshLivingMotions(player);
			}
			PacketHandler.syncToAllAround(player, playerData);
		}
	}

	@Override
	public void updateDrive(Player player) {
		PlayerData playerData = PlayerData.get(player);

		if (playerData.getFP() > 0) {
			playerData.setFP(playerData.getFP() - 0.3);
		} else {
			endDrive(player);
		}
	}

	@Override
	public SoundEvent getDriveSound() {
		return ModSounds.antidrive.get();
	}
}