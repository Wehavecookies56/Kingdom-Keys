package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;
import online.kingdomkeys.kingdomkeys.entity.mob.IKHMob;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetAerialDodgeTicksPacket;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetGlidingPacket;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

@EventBusSubscriber(modid = KingdomKeys.MODID)
public class DriveFormFinal extends DriveForm {

	public DriveFormFinal(ResourceLocation registryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowth) {
		super(registryName, order, hasKeychain, baseGrowth);
		this.color = new float[] { 0.9F, 0.9F, 0.9F };
		this.skinRL = skinRL;
	}

	@SubscribeEvent
	public static void getFinalFormXP(LivingDeathEvent event) {
		if (!event.getEntity().level().isClientSide && (event.getEntity() instanceof EnderMan) || event.getEntity() instanceof IKHMob && ((IKHMob)event.getEntity()).getKHMobType() == MobType.NOBODY) {
			if (event.getSource().getEntity() instanceof Player) {
				Player player = (Player) event.getSource().getEntity();
				PlayerData playerData = PlayerData.get(player);

				if (playerData != null && playerData.getActiveDriveForm().equals(Strings.Form_Final)) {
					double mult = Double.parseDouble(ModConfigs.driveFormXPMultiplier.get(4).split(",")[1]);
					playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), (int) (playerData.getDriveFormExp(playerData.getActiveDriveForm()) + (1*mult)));
					PacketHandler.sendTo(new SCSyncPlayerData(playerData), (ServerPlayer) player);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onLivingUpdate(PlayerTickEvent event) {
		Player player = (Player) event.getEntity();
		PlayerData playerData = PlayerData.get(player);

		if (playerData != null) {
			// Drive Form abilities
			if (playerData.getDriveFormMap() != null && playerData.getActiveDriveForm().equals(Strings.Form_Final)) {
				handleHighJump(player, playerData);
			}

			DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(playerData.getActiveDriveForm()));
			if (playerData.getActiveDriveForm().equals(Strings.Form_Final) || (playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) || form.getBaseGrowthAbilities()) && (playerData.getDriveFormMap().containsKey(Strings.Form_Final) && playerData.getDriveFormLevel(Strings.Form_Final) >= 3 && playerData.getEquippedAbilityLevel(Strings.glide) != null && playerData.getEquippedAbilityLevel(Strings.glide)[1] > 0)) {
				handleGlide(player, playerData);
			}


			//Check if the player has the ability to cancel the variable
			if(playerData.getIsGliding()) {
				if(playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) && playerData.getEquippedAbilityLevel(Strings.glide)[1] == 0) {
					playerData.setIsGliding(false);
					//if(player.world.isRemote)
						//PacketHandler.sendToServer(new CSSetGlidingPacket(false));
				}
			}
		}
	}

	private static void handleHighJump(Player player, PlayerData playerData) {
		boolean j = false;
		if (player.level().isClientSide) {
			j = Minecraft.getInstance().options.keyJump.isDown();
		}

		if (j) {
			if (player.getDeltaMovement().y > 0) {
				if (playerData.getActiveDriveForm().equals(Strings.Form_Final)) {
					player.setDeltaMovement(player.getDeltaMovement().add(0, DriveForm.FINAL_JUMP_BOOST[playerData.getDriveFormLevel(Strings.Form_Final)], 0));
				}
			}
		}
	}
	
	private static void handleGlide(Player player, PlayerData playerData) {
		if (player.isInWater() || player.isInLava())
			return;
		if (player.level().isClientSide) {// Need to check if it's clientside for the keyboard key detection
			Minecraft mc = Minecraft.getInstance();
			if (mc.player == player) { // Only the local player will send the packets
				if (!player.onGround() && player.fallDistance > 0) { // Glide only when falling
					if (mc.options.keyJump.isDown()) {
						if (!playerData.getIsGliding() && !(player.level().getBlockState(player.blockPosition()).getBlock() instanceof LiquidBlock) && !(player.level().getBlockState(player.blockPosition().below()).getBlock() instanceof LiquidBlock)) {
							playerData.setIsGliding(true);// Set playerData clientside
							playerData.setAerialDodgeTicks(0);
							PacketHandler.sendToServer(new CSSetGlidingPacket(true)); // Set playerData serverside
							PacketHandler.sendToServer(new CSSetAerialDodgeTicksPacket(true, 0)); // In case the player is still rotating stop it
						}
					} else { // If is no longer pressing space
						if (playerData.getIsGliding()) {
							playerData.setIsGliding(false);
							PacketHandler.sendToServer(new CSSetGlidingPacket(false));
						}
					}
				} else { // If touches the ground
					if (playerData.getIsGliding()) {
						playerData.setIsGliding(false);
						PacketHandler.sendToServer(new CSSetGlidingPacket(false));
						PacketHandler.sendToServer(new CSSetAerialDodgeTicksPacket(false, 0)); // In case the player is still rotating stop it
					}
				}
			}
		}

		if (playerData.getIsGliding()) {
			int glideLevel = playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) ? playerData.getDriveFormLevel(Strings.Form_Final) - 2 : playerData.getDriveFormLevel(Strings.Form_Final);// TODO eventually replace it with the skill
			float glide = DriveForm.FINAL_GLIDE[glideLevel];
			float limit = DriveForm.FINAL_GLIDE_SPEED[glideLevel];
			
			Vec3 motion = player.getDeltaMovement();

			if (Math.abs(motion.x()) < limit && Math.abs(motion.z()) < limit)
				player.setDeltaMovement(motion.x() * 1.1, glide, motion.z() * 1.1);

			if (player.getForcedPose() != Pose.SWIMMING) {
				player.setForcedPose(Pose.SWIMMING);
			}
		}
	}
}