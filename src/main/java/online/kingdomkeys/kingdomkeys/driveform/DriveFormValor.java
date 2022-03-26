package online.kingdomkeys.kingdomkeys.driveform;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
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
public class DriveFormValor extends DriveForm {

	public DriveFormValor(String registryName, int order, ResourceLocation skinRL, boolean hasKeychain) {
		super(registryName, order, hasKeychain);
		this.color = new float[] { 1F, 0F, 0F };
		this.skinRL = skinRL;
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
		if (!event.getEntity().level.isClientSide && event.getEntityLiving() instanceof Monster) {
			if (event.getSource().getEntity() instanceof Player) {
				Player player = (Player) event.getSource().getEntity();
				IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
				
				if (playerData != null && playerData.getActiveDriveForm().equals(Strings.Form_Valor)) {
					double mult = Double.parseDouble(ModConfigs.driveFormXPMultiplier.get(0).split(",")[1]);
					playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), (int) (playerData.getDriveFormExp(playerData.getActiveDriveForm()) + (1*mult)));
					PacketHandler.sendTo(new SCSyncCapabilityPacket(playerData), (ServerPlayer)player);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onLivingUpdate(LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof Player) {
			Player player = (Player) event.getEntityLiving();
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
	
			if (playerData != null) {
				// Drive Form abilities
				if (shouldHandleHighJump(player, playerData)) {
					handleHighJump(player, playerData);
				}
			}
		}
	}

	private static boolean shouldHandleHighJump(Player player, IPlayerCapabilities playerData) {
		if (playerData.getDriveFormMap() == null)
			return false;

		if (playerData.getActiveDriveForm().equals(Strings.Form_Valor) || playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) && (playerData.getDriveFormMap().containsKey(Strings.Form_Valor) && playerData.getDriveFormLevel(Strings.Form_Valor) >= 3 && playerData.getEquippedAbilityLevel(Strings.highJump) != null && playerData.getEquippedAbilityLevel(Strings.highJump)[1] > 0)) {
			return true;
		}
		return false;
	}

	private static void handleHighJump(Player player, IPlayerCapabilities playerData) {
		boolean j = false;
		if (player.level.isClientSide) {
			j = Minecraft.getInstance().options.keyJump.isDown();
		}

		if (j) {
			if (player.getDeltaMovement().y > 0) {
				if (playerData.getActiveDriveForm().equals(Strings.Form_Valor)) {
					player.setDeltaMovement(player.getDeltaMovement().add(0, DriveForm.VALOR_JUMP_BOOST[playerData.getDriveFormLevel(Strings.Form_Valor)], 0));
				} else {
					if (playerData.getActiveDriveForm() != null) {
						int jumpLevel = playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) ? playerData.getDriveFormLevel(Strings.Form_Valor) - 2 : playerData.getDriveFormLevel(Strings.Form_Valor);// TODO eventually replace it with the skill
						player.setDeltaMovement(player.getDeltaMovement().add(0, DriveForm.VALOR_JUMP_BOOST[jumpLevel], 0));
					}
				}
			}
		}
	}
}