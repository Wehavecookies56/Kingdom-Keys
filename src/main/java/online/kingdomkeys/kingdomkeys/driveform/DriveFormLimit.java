package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID)
public class DriveFormLimit extends DriveForm {

	public DriveFormLimit(String registryName, int order) {
		super(registryName, order);
		this.driveCost = 400;
		this.ap = 1;
		this.levelUpCosts = new int[] {0, 3, 9, 21, 40, 63, 90};
	}
	
	@Override
	public String getBaseAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "";
		case 2:
			return "Auto Limit";
		case 3:
			return "Dodge Roll LV 1";
		case 4:
			return "";
		case 5:
			return "Dodge Roll LV 2";
		case 6:
			return "";
		case 7:
			return "Dodge Roll LV 3";
		}
		return null;	
	}

	@Override
	public String getDFAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "Dodge Roll LV 1";
		case 2:
			return "";
		case 3:
			return "Dodge Roll LV 2";
		case 4:
			return "";
		case 5:
			return "Dodge Roll LV 3";
		case 6:
			return "";
		case 7:
			return "Dodge Roll LV MAX";
		}
		return null;
	}
	
	@SubscribeEvent
	public static void getValorFormXP(LivingAttackEvent event) {
		if (!event.getEntity().world.isRemote) { //TODO Check the target is hostile
			if (event.getSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
				IPlayerCapabilities props = ModCapabilities.get(player);

				if (props.getActiveDriveForm().equals(Strings.Form_Limit)) {
					props.setDriveFormExp(player, props.getActiveDriveForm(), props.getDriveFormExp(props.getActiveDriveForm()) + 1);
					PacketHandler.sendTo(new SCSyncCapabilityPacket(props), (ServerPlayerEntity)player);
				}
			}
		}
	}

}