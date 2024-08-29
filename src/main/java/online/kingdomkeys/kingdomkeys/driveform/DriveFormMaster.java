package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetAerialDodgeTicksPacket;

@EventBusSubscriber(modid = KingdomKeys.MODID)
public class DriveFormMaster extends DriveForm {

	public DriveFormMaster(ResourceLocation registryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowth) {
		super(registryName, order, hasKeychain, baseGrowth);
		this.color = new float[] { 1F, 0.7F, 0.1F };
		this.skinRL = skinRL;
	}

	//Hehe you won't find it here, it's in DriveOrbEntity#onPickup
	
	@SubscribeEvent
	public static void onLivingUpdate(PlayerTickEvent event) {
		Player player = (Player) event.getEntity();
		IPlayerData playerData = ModData.getPlayer(player);

		if (playerData != null) {
			// Drive Form abilities
			DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(playerData.getActiveDriveForm()));
			if (playerData.getActiveDriveForm().equals(Strings.Form_Master) || (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) || form.getBaseGrowthAbilities()) && (playerData.getDriveFormMap().containsKey(Strings.Form_Master) && playerData.getDriveFormLevel(Strings.Form_Master) >= 3 && playerData.getEquippedAbilityLevel(Strings.aerialDodge) != null && playerData.getEquippedAbilityLevel(Strings.aerialDodge)[1] > 0)) {
				handleAerialDodge(player, playerData);
			}
		}
	}

	private static void handleAerialDodge(Player player, IPlayerData playerData) {
		if (playerData.getAerialDodgeTicks() <= 0) {
			if (player.onGround()) {
				playerData.setHasJumpedAerialDodge(false);
				playerData.setAerialDodgeTicks(0);
			} else {
				if (player.level().isClientSide) {
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