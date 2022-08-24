package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncCapabilityPacket;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID)
public class DriveFormLimit extends DriveForm {

	public DriveFormLimit(String registryName, int order, ResourceLocation skinRL, boolean hasKeychain) {
		super(registryName, order, hasKeychain);
		this.color = new float[] { 0.6F, 0.3F, 1F };
		this.skinRL = skinRL;
	}
	
	@SubscribeEvent
	public static void getLimitFormXP(LivingAttackEvent event) {
		if (!event.getEntity().level.isClientSide && event.getEntityLiving() instanceof Monster) {
			if (event.getSource().getEntity() instanceof Player) {
				Player player = (Player) event.getSource().getEntity();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				if (playerData != null && playerData.getActiveDriveForm().equals(Strings.Form_Limit) && playerData.hasShotMaxShotlock()) {
					double mult = Double.parseDouble(ModConfigs.driveFormXPMultiplier.get(2).split(",")[1]);
					playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), (int) (playerData.getDriveFormExp(playerData.getActiveDriveForm()) + (1*mult)));
					playerData.setHasShotMaxShotlock(false);
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)player);
				}
			}
		}
	}	
}