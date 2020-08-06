package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

public class DriveFormMaster extends DriveForm {

	public DriveFormMaster(String registryName, int order) {
		super(registryName, order);
		this.driveCost = 400;
		this.ap = 1;
		this.levelUpCosts = new int[] {0, 60, 240, 456, 726, 1050, 1500};
	}
	
	@Override
	public String getBaseAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "";
		case 2:
			return Strings.autoMaster;
		case 3:
			return Strings.aerialDodge;
		case 4:
			return "";
		case 5:
			return Strings.aerialDodge;
		case 6:
			return "";
		case 7:
			return Strings.aerialDodge;
		}
		return null;	
	}

	@Override
	public String getDFAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return Strings.aerialDodge;
		case 2:
			return "";
		case 3:
			return Strings.aerialDodge;
		case 4:
			return "";
		case 5:
			return Strings.aerialDodge;
		case 6:
			return "";
		case 7:
			return Strings.aerialDodge;
		}
		return null;
	}
	
	//Hehe you won't find it here, it's in DriveOrbEntity#onPickup

}