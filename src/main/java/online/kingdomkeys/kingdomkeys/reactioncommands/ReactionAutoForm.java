package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.ability.ModAbilities;
import online.kingdomkeys.kingdomkeys.api.event.AbilityEvent;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;
import online.kingdomkeys.kingdomkeys.util.Utils;
import online.kingdomkeys.kingdomkeys.util.Utils.OrgMember;

import java.util.ArrayList;
import java.util.List;

public class ReactionAutoForm extends ReactionCommand {
	ResourceLocation form, abilityName;

	public ReactionAutoForm(ResourceLocation registryName, ResourceLocation abilityName, ResourceLocation form) {
		super(registryName, true, -1);
		this.form = form;
		this.abilityName = abilityName;
	}
	
	public ResourceLocation getFormName() {
		return form;
	}
	
	public ResourceLocation getAbilityName() {
		return abilityName;
	}

	public DriveForm getForm() {
		return ModDriveForms.registry.get(form);
	}
	
	public boolean isAutoForm() {
		return form != null;
	}
	
	@Override
	public void onUse(Player player, LivingEntity target, LivingEntity lockOnEntity) {
		if(conditionsToAppear(player,target)) {
			player.level().playSound(null, player.position().x(),player.position().y(),player.position().z(), ModSounds.drive.get(), SoundSource.PLAYERS, 1F, 1F);
			PlayerData playerData = PlayerData.get(player);
			
			if (!playerData.noFormActive() && form.equals(DriveForm.NONE)) { // If is in a drive form and the target is "" (player)
				DriveForm forma = ModDriveForms.registry.get(playerData.getActiveDriveForm());
				forma.endDrive(player);
				if (!forma.getBaseGrowthAbilities()) {
					NeoForge.EVENT_BUS.post(new AbilityEvent.Unequip(ModAbilities.registry.get(forma.getDFAbilityForLevel(playerData.getDriveFormLevel(forma.getRegistryName())).get()), playerData.getDriveFormLevel(forma.getRegistryName()), player, false));
				}
				for (ResourceLocation abilityLoc : forma.getDriveFormData().getAbilities()) {
					Ability ability = ModAbilities.registry.get(abilityLoc);
					NeoForge.EVENT_BUS.post(new AbilityEvent.Equip(ability, 0, player, false));
				}
			} else if (!form.equals(DriveForm.NONE)) { // If is not in a form and wants to drive
				DriveForm forma = ModDriveForms.registry.get(form);
				forma.initDrive(player);
				if (!forma.getBaseGrowthAbilities()) {
					NeoForge.EVENT_BUS.post(new AbilityEvent.Equip(ModAbilities.registry.get(forma.getDFAbilityForLevel(playerData.getDriveFormLevel(forma.getRegistryName())).get()), playerData.getDriveFormLevel(forma.getRegistryName()), player, false));
				}
				for (ResourceLocation abilityLoc : forma.getDriveFormData().getAbilities()) {
					Ability ability = ModAbilities.registry.get(abilityLoc);
					NeoForge.EVENT_BUS.post(new AbilityEvent.Equip(ability, 0, player, false));
				}
			}
			
			playerData.removeReactionCommand(getRegistryName());
			List<ReactionCommand> list = new ArrayList<>();
			for(ResourceLocation name : playerData.getReactionCommands().keySet()) {
				ReactionCommand rc = ModReactionCommands.registry.get(name);
				if(rc instanceof ReactionAutoForm) {
					list.add(rc);
				}
			}
			
			for(ReactionCommand rc : list) {
				if(rc instanceof ReactionAutoForm) {
					playerData.removeReactionCommand(rc.getRegistryName());
				}
			}
		}
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity target) {
		PlayerData playerData = PlayerData.get(player);
		if(playerData != null) {
			if(Utils.isPlayerLowHP(player)) {
				if(playerData.getAlignment() == OrgMember.NONE) {
					if(playerData.noFormActive()) {
						if(playerData.getDP() >= ModDriveForms.registry.get(form).getDriveCost()) {
                            return playerData.getEquippedAbilityLevel(abilityName)[1] > 0;
						}
					}
				}
			}
		}
		return false;
	}
	
}