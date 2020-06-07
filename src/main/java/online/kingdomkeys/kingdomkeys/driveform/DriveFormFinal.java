package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.packet.SCSyncCapabilityPacket;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID)
public class DriveFormFinal extends DriveForm {

	public DriveFormFinal(String registryName, int order) {
		super(registryName, order);
		this.driveCost = 500;
		this.ap = -10;
		this.levelUpCosts = new int[] {0, 20, 80, 152, 242, 350, 500};
	}

	@Override
	public String getBaseAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "";
		case 2:
			return "Auto Final";
		case 3:
			return "Glide LV 1";
		case 4:
			return "";
		case 5:
			return "Glide LV 2";
		case 6:
			return "";
		case 7:
			return "Glide LV 3";
		}
		return null;	
	}
	
	@Override
	public String getDFAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "Glide LV 1";
		case 2:
			return "";
		case 3:
			return "Glide LV 2";
		case 4:
			return "";
		case 5:
			return "Glide LV 3";
		case 6:
			return "";
		case 7:
			return "Glide MAX";
		}
		return null;
	}
	
	@SubscribeEvent
	public static void getFinalFormXP(LivingDeathEvent event) {
		 if (!event.getEntity().world.isRemote && event.getEntity() instanceof EndermanEntity) { 
			if (event.getSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
				IPlayerCapabilities props = ModCapabilities.get(player);

				if (props.getActiveDriveForm().equals(Strings.Form_Final)) {
					props.setDriveFormExp(player, props.getActiveDriveForm(), props.getDriveFormExp(props.getActiveDriveForm()) + 1);
					PacketHandler.sendTo(new SCSyncCapabilityPacket(props), (ServerPlayerEntity)player);
				}
			}
		}
	}
}