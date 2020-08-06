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
public class DriveFormValor extends DriveForm {

	public DriveFormValor(String registryName, int order) {
		super(registryName, order);
		this.driveCost = 300;
		this.ap = 1;
		this.levelUpCosts = new int[] { 0, 80, 240, 520, 968, 1528, 2200 };
	}
	
	@Override
	public String getBaseAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return "";
		case 2:
			return Strings.autoValor;
		case 3:
			return Strings.highJump;
		case 4:
			return "";
		case 5:
			return Strings.highJump;
		case 6:
			return "";
		case 7:
			return Strings.highJump;
		}
		return null;	
	}

	@Override
	public String getDFAbilityForLevel(int driveFormLevel) {
		switch (driveFormLevel) {
		case 1:
			return Strings.highJump;
		case 2:
			return "";
		case 3:
			return Strings.highJump;
		case 4:
			return "";
		case 5:
			return Strings.highJump;
		case 6:
			return "";
		case 7:
			return Strings.highJump;
		}
		return null;
	}
	
	@SubscribeEvent
	public static void getValorFormXP(LivingAttackEvent event) {
		if (!event.getEntity().world.isRemote) { //TODO Check the target is hostile
			if (event.getSource().getTrueSource() instanceof PlayerEntity) {
				PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
				IPlayerCapabilities props = ModCapabilities.get(player);
				
				if (props != null && props.getActiveDriveForm().equals(Strings.Form_Valor)) {
					props.setDriveFormExp(player, props.getActiveDriveForm(), props.getDriveFormExp(props.getActiveDriveForm()) + 1);
					//props.setDriveFormExp(player, props.getActiveDriveForm(), 239);
					PacketHandler.sendTo(new SCSyncCapabilityPacket(props), (ServerPlayerEntity)player);
				}
			}
		}
	}
}