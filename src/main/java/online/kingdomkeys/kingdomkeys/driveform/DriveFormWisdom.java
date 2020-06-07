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
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.mob.IKHMob;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID)
public class DriveFormWisdom extends DriveForm {

	public DriveFormWisdom(String registryName, int order) {
		super(registryName, order);
		this.driveCost = 300;
		this.ap = 1;
		this.levelUpCosts = new int[] { 0, 20, 80, 152, 242, 350, 500 };
	}

	@Override
	public String getBaseAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "";
		case 2:
			return "Auto Wisdom";
		case 3:
			return "Quick Run LV 1";
		case 4:
			return "";
		case 5:
			return "Quick Run LV 2";
		case 6:
			return "";
		case 7:
			return "Quick Run LV 3";
		}
		return null;
	}

	@Override
	public String getDFAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "Quick Run LV 1";
		case 2:
			return "";
		case 3:
			return "Quick Run LV 2";
		case 4:
			return "";
		case 5:
			return "Quick Run LV 3";
		case 6:
			return "";
		case 7:
			return "Quick Run MAX";
		}
		return null;
	}

	@SubscribeEvent
	public static void getWisdomFormXP(LivingDeathEvent event) { // Check if it's a heartless
		if (!event.getEntity().world.isRemote && event.getEntity() instanceof IKHMob) {
			if (((IKHMob) event.getEntity()).getMobType() == MobType.HEARTLESS_EMBLEM || ((IKHMob) event.getEntity()).getMobType() == MobType.HEARTLESS_PUREBLOOD) {
				if (event.getSource().getTrueSource() instanceof PlayerEntity) {
					PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
					IPlayerCapabilities props = ModCapabilities.get(player);
					if (props.getActiveDriveForm().equals(Strings.Form_Wisdom)) {
						props.setDriveFormExp(player, props.getActiveDriveForm(), props.getDriveFormExp(props.getActiveDriveForm()) + 1);
						PacketHandler.sendTo(new SCSyncCapabilityPacket(props), (ServerPlayerEntity) player);
					}
				}
			}
		}
	}

}