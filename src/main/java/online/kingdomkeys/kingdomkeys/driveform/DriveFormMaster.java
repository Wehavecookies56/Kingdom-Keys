package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetAerialDodgeTicksPacket;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetGlidingPacket;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID)
public class DriveFormMaster extends DriveForm {

	public DriveFormMaster(String registryName, int order, ResourceLocation skinRL, boolean hasKeychain) {
		super(registryName, order, hasKeychain);
		this.driveCost = 400;
		this.ap = 1;
		this.levelUpCosts = new int[] {0, 60, 240, 456, 726, 1050, 1500};
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
		if(event.getEntityLiving() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) event.getEntityLiving();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
	
			if (playerData != null) {
				//Drive form speed
				if(playerData.getActiveDriveForm().equals(Strings.Form_Master)) {
					if(player.isOnGround()) {
						player.setMotion(player.getMotion().mul(new Vector3d(1.5, 1, 1.5)));
					}
				}
				
				// Drive Form abilities								
				if (playerData.getActiveDriveForm().equals(Strings.Form_Master) || playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) && (playerData.getDriveFormMap().containsKey(Strings.Form_Master) && playerData.getDriveFormLevel(Strings.Form_Master) >= 3 && playerData.getEquippedAbilityLevel(Strings.aerialDodge) != null && playerData.getEquippedAbilityLevel(Strings.aerialDodge)[1] > 0)) {
					handleAerialDodge(player, playerData);
				}
			}
		}
	}

	private static void handleAerialDodge(PlayerEntity player, IPlayerCapabilities playerData) {
		if (playerData.getAerialDodgeTicks() <= 0) {
			if (player.isOnGround()) {
				playerData.setHasJumpedAerialDodge(false);
				playerData.setAerialDodgeTicks(0);
			} else {
				if (player.world.isRemote) {
					if (player.getMotion().y < 0 && Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown() && !player.isSneaking()) {
						if (!playerData.hasJumpedAerialDodge()) {
							playerData.setHasJumpedAerialDodge(true);
							player.jump();
							int jumpLevel = playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) ? playerData.getDriveFormLevel(Strings.Form_Master) - 2 : playerData.getDriveFormLevel(Strings.Form_Master);// TODO eventually replace it with the skill
							float boost = DriveForm.MASTER_AERIAL_DODGE_BOOST[jumpLevel];
							player.setMotion(player.getMotion().mul(new Vector3d(boost, boost, boost)));
							PacketHandler.sendToServer(new CSSetAerialDodgeTicksPacket(true, 10));
						}
					}
				}
			}
		}
	}

}