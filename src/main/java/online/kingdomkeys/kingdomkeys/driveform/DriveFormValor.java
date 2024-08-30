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
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.Strings;
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
			if (event.getSource().getEntity() instanceof Player) {
				Player player = (Player) event.getSource().getEntity();
				PlayerData playerData = PlayerData.get(player);
				
				if (playerData != null && playerData.getActiveDriveForm().equals(Strings.Form_Valor)) {
					double mult = Double.parseDouble(ModConfigs.driveFormXPMultiplier.get(0).split(",")[1]);
					playerData.setDriveFormExp(player, playerData.getActiveDriveForm(), (int) (playerData.getDriveFormExp(playerData.getActiveDriveForm()) + (1*mult)));
					PacketHandler.sendTo(new SCSyncPlayerData(playerData), (ServerPlayer)player);
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
			if (shouldHandleHighJump(player, playerData)) {
				handleHighJump(player, playerData);
			}
		}
	}

	private static boolean shouldHandleHighJump(Player player, PlayerData playerData) {
		if (playerData.getDriveFormMap() == null)
			return false;

		if (playerData.getActiveDriveForm().equals(Strings.Form_Valor) || playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) && (playerData.getDriveFormMap().containsKey(Strings.Form_Valor) && playerData.getDriveFormLevel(Strings.Form_Valor) >= 3 && playerData.getEquippedAbilityLevel(Strings.highJump) != null && playerData.getEquippedAbilityLevel(Strings.highJump)[1] > 0)) {
			return true;
		}
		DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(playerData.getActiveDriveForm()));
		if (form.getBaseGrowthAbilities() || playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) && (playerData.getDriveFormMap().containsKey(Strings.Form_Valor) && playerData.getDriveFormLevel(Strings.Form_Valor) >= 3 && playerData.getEquippedAbilityLevel(Strings.highJump) != null && playerData.getEquippedAbilityLevel(Strings.highJump)[1] > 0)) {
			return true;
		}
		return false;
	}

	private static void handleHighJump(Player player, PlayerData playerData) {
		boolean j = false;
		if (player.level().isClientSide) {
			j = Minecraft.getInstance().options.keyJump.isDown();
		}

		if (j) {
			if (player.getDeltaMovement().y > 0) {
				DriveForm form = ModDriveForms.registry.get(ResourceLocation.parse(playerData.getActiveDriveForm()));

				if (playerData.getActiveDriveForm().equals(Strings.Form_Valor)) {
					player.setDeltaMovement(player.getDeltaMovement().add(0, DriveForm.VALOR_JUMP_BOOST[playerData.getDriveFormLevel(Strings.Form_Valor)], 0));
				} else {
					if (playerData.getActiveDriveForm() != null) {
						int jumpLevel = playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) || form.getBaseGrowthAbilities() ? playerData.getDriveFormLevel(Strings.Form_Valor) - 2 : playerData.getDriveFormLevel(Strings.Form_Valor);// TODO eventually replace it with the skill
						if(jumpLevel > -1)
							player.setDeltaMovement(player.getDeltaMovement().add(0, DriveForm.VALOR_JUMP_BOOST[jumpLevel], 0));
					}
				}
			}
		}
	}
}