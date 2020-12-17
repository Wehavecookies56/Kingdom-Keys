package online.kingdomkeys.kingdomkeys.driveform;

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

	public DriveFormWisdom(String registryName, int order, boolean hasKeychain) {
		super(registryName, order, hasKeychain);
		this.driveCost = 300;
		this.ap = 1;
		this.levelUpCosts = new int[] { 0, 20, 80, 152, 242, 350, 500 };
		this.color = new float[] { 0F, 0F, 1F };
	}

	@Override
	public String getBaseAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "";
		case 2:
			return Strings.autoWisdom;
		case 3:
			return Strings.quickRun;
		case 4:
			return "";
		case 5:
			return Strings.quickRun;
		case 6:
			return "";
		case 7:
			return Strings.quickRun;
		}
		return null;	
	}

	@Override
	public String getDFAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return Strings.quickRun;
		case 2:
			return "";
		case 3:
			return Strings.quickRun;
		case 4:
			return "";
		case 5:
			return Strings.quickRun;
		case 6:
			return "";
		case 7:
			return Strings.quickRun;
		}
		return null;
	}

	@SubscribeEvent
	public static void getWisdomFormXP(LivingDeathEvent event) { // Check if it's a heartless
		if (!event.getEntity().world.isRemote && event.getEntity() instanceof IKHMob) {
			if (((IKHMob) event.getEntity()).getMobType() == MobType.HEARTLESS_EMBLEM || ((IKHMob) event.getEntity()).getMobType() == MobType.HEARTLESS_PUREBLOOD) {
				if (event.getSource().getTrueSource() instanceof PlayerEntity) {
					PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
					if (playerData != null && playerData.getActiveDriveForm().equals(Strings.Form_Wisdom)) {
						playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), playerData.getDriveFormExp(playerData.getActiveDriveForm()) + 1);
						PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
					}
				}
			}
		}
	}

}