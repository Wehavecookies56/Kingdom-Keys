package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.mob.IKHMob;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

@EventBusSubscriber(modid = KingdomKeys.MODID)
public class DriveFormWisdom extends DriveForm {

	public DriveFormWisdom(ResourceLocation registryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowth) {
		super(registryName, order, hasKeychain, baseGrowth);
		this.color = new float[] { 0F, 0F, 1F };
		this.skinRL = skinRL;
	}

	@SubscribeEvent
	public static void getWisdomFormXP(LivingDeathEvent event) { // Check if it's a heartless
		if (!event.getEntity().level().isClientSide && event.getEntity() instanceof IKHMob) {
			if (((IKHMob) event.getEntity()).getKHMobType() == MobType.HEARTLESS_EMBLEM || ((IKHMob) event.getEntity()).getKHMobType() == MobType.HEARTLESS_PUREBLOOD) {
				if (event.getSource().getEntity() instanceof Player) {
					Player player = (Player) event.getSource().getEntity();
					PlayerData playerData = PlayerData.get(player);
					if (playerData != null && playerData.getActiveDriveForm().equals(Strings.Form_Wisdom)) {
						double mult = Double.parseDouble(ModConfigs.driveFormXPMultiplier.get(1).split(",")[1]);
						playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), (int) (playerData.getDriveFormExp(playerData.getActiveDriveForm()) + (1*mult)));
						PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer) player);
					}
				}
			}
		}
	}

}