package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.CommonConfig;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.mob.IKHMob;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID)
public class DriveFormFinal extends DriveForm {

	public DriveFormFinal(String registryName, int order, ResourceLocation skinRL, boolean hasKeychain) {
		super(registryName, order, hasKeychain);
		this.driveCost = 500;
		this.ap = -10;
		this.levelUpCosts = new int[] { 0, 20, 80, 152, 242, 350, 500 };
		this.color = new float[] { 0.9F, 0.9F, 0.9F };
		this.skinRL = skinRL;
	}

	@Override
	public String getBaseAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "";
		case 2:
			return Strings.autoFinal;
		case 3:
			return Strings.glide;
		case 4:
			return "";
		case 5:
			return Strings.glide;
		case 6:
			return "";
		case 7:
			return Strings.glide;
		}
		return null;
	}

	@Override
	public String getDFAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return Strings.glide;
		case 2:
			return "";
		case 3:
			return Strings.glide;
		case 4:
			return "";
		case 5:
			return Strings.glide;
		case 6:
			return "";
		case 7:
			return Strings.glide;
		}
		return null;
	}

	@SubscribeEvent
	public static void getFinalFormXP(LivingDeathEvent event) {
		if (!event.getEntity().world.isRemote && (event.getEntity() instanceof EndermanEntity) || event.getEntity() instanceof IKHMob && ((IKHMob)event.getEntity()).getMobType() == MobType.NOBODY) {
			if (event.getSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);

				if (playerData != null && playerData.getActiveDriveForm().equals(Strings.Form_Final)) {
					playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), (int) (playerData.getDriveFormExp(playerData.getActiveDriveForm()) + (1*CommonConfig.finalFormXPMultiplier.get())));
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayerEntity) player);
				}
			}
		}
	}
}