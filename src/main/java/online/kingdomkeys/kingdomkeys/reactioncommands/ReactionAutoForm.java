package online.kingdomkeys.kingdomkeys.reactioncommands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.event.AbilityEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
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
		return ModDriveForms.registry.get().getValue(new ResourceLocation(form));
	}
	
	public boolean isAutoForm() {
		return form != null;
	}
	
	@Override
	public void onUse(Player player, LivingEntity target, LivingEntity lockOnEntity) {
		if(conditionsToAppear(player,target)) {
			player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.drive.get(), SoundSource.PLAYERS, 1F, 1F);
	    	//PacketHandler.sendToServer(new CSSetDriveFormPacket(form));
			IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
			
			
			if (!playerData.getActiveDriveForm().equals(DriveForm.NONE.toString()) && form.equals(DriveForm.NONE.toString())) { // If is in a drive form and the target is "" (player)
				DriveForm forma = ModDriveForms.registry.get().getValue(new ResourceLocation(playerData.getActiveDriveForm()));
				forma.endDrive(player);
				if (!forma.getBaseGrowthAbilities()) {
					MinecraftForge.EVENT_BUS.post(new AbilityEvent.Unequip(ModAbilities.registry.get().getValue(new ResourceLocation(forma.getDFAbilityForLevel(playerData.getDriveFormLevel(forma.getName())))), playerData.getDriveFormLevel(forma.getName()), player, false));
				}
				for (String abilityLoc : forma.getDriveFormData().getAbilities()) {
					Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(abilityLoc));
					MinecraftForge.EVENT_BUS.post(new AbilityEvent.Equip(ability, 0, player, false));
				}
			} else if (!form.equals(DriveForm.NONE.toString())) { // If is not in a form and wants to drive
				DriveForm forma = ModDriveForms.registry.get().getValue(new ResourceLocation(form));
				forma.initDrive(player);
				if (!forma.getBaseGrowthAbilities()) {
					MinecraftForge.EVENT_BUS.post(new AbilityEvent.Equip(ModAbilities.registry.get().getValue(new ResourceLocation(forma.getDFAbilityForLevel(playerData.getDriveFormLevel(forma.getName())))), playerData.getDriveFormLevel(forma.getName()), player, false));
				}
				for (String abilityLoc : forma.getDriveFormData().getAbilities()) {
					Ability ability = ModAbilities.registry.get().getValue(new ResourceLocation(abilityLoc));
					MinecraftForge.EVENT_BUS.post(new AbilityEvent.Equip(ability, 0, player, false));
				}
			}
			
			playerData.removeReactionCommand(getRegistryName().toString());
			List<ReactionCommand> list = new ArrayList<ReactionCommand>();
			for(String name : playerData.getReactionCommands()) {
				ReactionCommand rc = ModReactionCommands.registry.get().getValue(new ResourceLocation(name));
				if(rc instanceof ReactionAutoForm) {
					list.add(rc);
				}
			}
			
			for(ReactionCommand rc : list) {
				if(rc instanceof ReactionAutoForm) {
					playerData.removeReactionCommand(rc.getName());
					
				}
			}
		}
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity target) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		if(playerData != null) {
			if(Utils.isPlayerLowHP(player)) {
				if(playerData.getAlignment() == OrgMember.NONE) {
					if(playerData.getActiveDriveForm().equals(DriveForm.NONE.toString())) {
						if(playerData.getDP() >= ModDriveForms.registry.get().getValue(new ResourceLocation(form)).getDriveCost()) {
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