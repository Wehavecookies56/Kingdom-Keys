package online.kingdomkeys.kingdomkeys.reactioncommands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSetDriveFormPacket;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID)
public class ReactionAutoForm extends ReactionCommand {
	String form, abilityName;

	public ReactionAutoForm(String registryName, String abilityName, String form) {
		super(registryName, true);
		this.form = form;
		this.abilityName = abilityName;
	}
	
	public String getFormName() {
		return form;
	}
	
	public String getAbilityName() {
		return abilityName;
	}

	public DriveForm getForm() {
		return ModDriveForms.registry.getValue(new ResourceLocation(form));
	}
	
	public boolean isAutoForm() {
		return form != null;
	}
	
	@Override
	public void onUse(PlayerEntity player, LivingEntity target) {
		if(conditionsToAppear(player,target)) {
			player.world.playSound(null, player.getPosition(), ModSounds.drive.get(), SoundCategory.PLAYERS, 1F, 1F);
	    	//PacketHandler.sendToServer(new CSSetDriveFormPacket(form));
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			//System.out.println("Actual form: " + playerData.getActiveDriveForm() + ", Going to get form: " + message.form);
			
			if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) && form.equals(DriveForm.NONE.toString())) { // If is in a drive form and the target is "" (player)
				DriveForm forma = ModDriveForms.registry.getValue(new ResourceLocation(playerData.getActiveDriveForm()));
				forma.endDrive(player);
			} else if (!form.equals(DriveForm.NONE.toString())) { // If is not in a form and wants to drive
				DriveForm forma = ModDriveForms.registry.getValue(new ResourceLocation(form));
				forma.initDrive(player);
			}
			
			playerData.removeReactionCommand(getRegistryName().toString());
			List<ReactionCommand> list = new ArrayList<ReactionCommand>();
			for(String name : playerData.getReactionCommands()) {
				ReactionCommand rc = ModReactionCommands.registry.getValue(new ResourceLocation(name));
				if(rc instanceof ReactionAutoForm) {
					list.add(rc);
				}
			}
			
			for(ReactionCommand rc : list) {
				if(rc instanceof ReactionAutoForm) {
					playerData.removeReactionCommand(rc.getName());
					//System.out.println("removed "+rc.getName());
				}
			}
		}
	}

	@Override
	public boolean conditionsToAppear(PlayerEntity player, LivingEntity target) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		if(playerData != null) {
			if(Utils.isPlayerLowHP(player)) {
				if(playerData.getAlignment() == OrgMember.NONE) {
					if(playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
						if(playerData.getDP() >= ModDriveForms.registry.getValue(new ResourceLocation(form)).getDriveCost()) {
							if(playerData.getEquippedAbilityLevel(abilityName)[1] > 0) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
}