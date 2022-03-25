package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetAerialDodgeTicksPacket;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID)
public class DriveFormMaster extends DriveForm {

	public DriveFormMaster(String registryName, int order, ResourceLocation skinRL, boolean hasKeychain) {
		super(registryName, order, hasKeychain);
		this.color = new float[] { 1F, 0.7F, 0.1F };
		this.skinRL = skinRL;
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
	
	@SubscribeEvent
	public static void onLivingUpdate(LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof Player) {
			Player player = (Player) event.getEntityLiving();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
	
			if (playerData != null) {
				// Drive Form abilities								
				if (playerData.getActiveDriveForm().equals(Strings.Form_Master) || playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) && (playerData.getDriveFormMap().containsKey(Strings.Form_Master) && playerData.getDriveFormLevel(Strings.Form_Master) >= 3 && playerData.getEquippedAbilityLevel(Strings.aerialDodge) != null && playerData.getEquippedAbilityLevel(Strings.aerialDodge)[1] > 0)) {
					handleAerialDodge(player, playerData);
				}
			}
		}
	}

	private static void handleAerialDodge(Player player, IPlayerCapabilities playerData) {
		if (playerData.getAerialDodgeTicks() <= 0) {
			if (player.isOnGround()) {
				playerData.setHasJumpedAerialDodge(false);
				playerData.setAerialDodgeTicks(0);
			} else {
				if (player.level.isClientSide) {
					if (player.getDeltaMovement().y < 0 && Minecraft.getInstance().options.keyJump.isDown() && !player.isShiftKeyDown()) {
						if (!playerData.hasJumpedAerialDodge()) {
							playerData.setHasJumpedAerialDodge(true);
							player.jumpFromGround();
							int jumpLevel = playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) ? playerData.getDriveFormLevel(Strings.Form_Master) - 2 : playerData.getDriveFormLevel(Strings.Form_Master);// TODO eventually replace it with the skill
							float boost = DriveForm.MASTER_AERIAL_DODGE_BOOST[jumpLevel];
							player.setDeltaMovement(player.getDeltaMovement().multiply(new Vec3(boost, boost, boost)));
							PacketHandler.sendToServer(new CSSetAerialDodgeTicksPacket(true, 10));
						}
					}
				}
			}
		}
	}

}