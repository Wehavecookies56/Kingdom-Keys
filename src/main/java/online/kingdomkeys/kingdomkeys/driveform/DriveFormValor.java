package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

@EventBusSubscriber(modid = KingdomKeys.MODID)
public class DriveFormValor extends DriveForm {

	public DriveFormValor(ResourceLocation registryName, int order, ResourceLocation skinRL, boolean hasKeychain, boolean baseGrowth) {
		super(registryName, order, hasKeychain, baseGrowth);
		this.color = new float[] { 1F, 0F, 0F };
		this.skinRL = skinRL;
	}

	@SubscribeEvent
	public static void getValorFormXP(LivingDamageEvent.Post event) {
		if (!event.getEntity().level().isClientSide && (event.getEntity() instanceof Monster || event.getEntity() instanceof EnderDragon)) {
			if (event.getSource().getEntity() instanceof Player player) {
                PlayerData playerData = PlayerData.get(player);
				
				if (playerData != null && playerData.isFormActive(ModDriveForms.VALOR)) {
					double mult = Double.parseDouble(ModConfigs.SERVER.driveFormXPMultiplier.get().get(0).split(",")[1]);
					playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), (int) (playerData.getDriveFormExp(playerData.getActiveDriveForm()) + (1*mult)));
					PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer)player);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onLivingUpdate(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		PlayerData playerData = PlayerData.get(player);

		if (playerData != null) {
			// Drive Form abilities
			if (shouldHandleHighJump(player, playerData)) {
				handleHighJump(player, playerData);
			}
		}
	}

	private static boolean shouldHandleHighJump(Player player, PlayerData playerData) {
		return playerData.isFormActive(ModDriveForms.VALOR) || playerData.isAbilityEquipped(ModAbilities.HIGH_JUMP);
	}

	private static void handleHighJump(Player player, PlayerData playerData) {
		boolean j = false;
		if (player.level().isClientSide) {
			j = Minecraft.getInstance().options.keyJump.isDown();
		}

		if (j) {
			if (player.getDeltaMovement().y > 0) {
				int jumpLevel = DriveForm.growthLevel(playerData, ModAbilities.HIGH_JUMP.location(), ModDriveForms.VALOR, DriveForm.VALOR_JUMP_BOOST.length);

				if (jumpLevel > 0) {
					player.setDeltaMovement(player.getDeltaMovement().add(0, DriveForm.VALOR_JUMP_BOOST[jumpLevel], 0));
				}
			}
		}
	}
}