package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.mob.IKHMob;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID)
public class DriveFormWisdom extends DriveForm {

	public DriveFormWisdom(String registryName, int order, ResourceLocation skinRL, boolean hasKeychain) {
		super(registryName, order, hasKeychain);
		this.color = new float[] { 0F, 0F, 1F };
		this.skinRL = skinRL;
	}

	@SubscribeEvent
	public static void getWisdomFormXP(LivingDeathEvent event) { // Check if it's a heartless
		if (!event.getEntity().level.isClientSide && event.getEntity() instanceof IKHMob) {
			if (((IKHMob) event.getEntity()).getKHMobType() == MobType.HEARTLESS_EMBLEM || ((IKHMob) event.getEntity()).getKHMobType() == MobType.HEARTLESS_PUREBLOOD) {
				if (event.getSource().getEntity() instanceof Player) {
					Player player = (Player) event.getSource().getEntity();
					IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
					if (playerData != null && playerData.getActiveDriveForm().equals(Strings.Form_Wisdom)) {
						double mult = Double.parseDouble(ModConfigs.driveFormXPMultiplier.get(1).split(",")[1]);
						playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), (int) (playerData.getDriveFormExp(playerData.getActiveDriveForm()) + (1*mult)));
						PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer) player);
					}
				}
			}
		}
	}

}