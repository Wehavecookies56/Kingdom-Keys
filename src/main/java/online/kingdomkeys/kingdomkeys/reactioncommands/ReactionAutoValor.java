package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetDriveFormPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID)
public class ReactionAutoValor extends ReactionCommand {

	public ReactionAutoValor(String registryName) {
		super(registryName);
	}

	@Override
	public void onUse(PlayerEntity player, LivingEntity target) {
		if(conditionsToAppear(player,target)) {
			player.world.playSound(null, player.getPosition(), ModSounds.drive.get(), SoundCategory.PLAYERS, 1F, 1F);
	    	PacketHandler.sendToServer(new CSSetDriveFormPacket(Strings.Form_Valor));
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			playerData.removeReactionCommand(getRegistryName().toString());
			
		}
	}

	@Override
	public boolean conditionsToAppear(PlayerEntity player, LivingEntity target) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		if(playerData != null) {
			if(Utils.isPlayerLowHP(player)) {
				if(playerData.getDP() >= ModDriveForms.registry.getValue(new ResourceLocation(Strings.Form_Valor)).getDriveCost()) {
					if(playerData.getEquippedAbilityLevel(Strings.autoValor)[1] > 0) {
						System.out.println("ability autovalor equipped");
						return true;
					}
				}
			}
		}
		return false;
	}
	
}