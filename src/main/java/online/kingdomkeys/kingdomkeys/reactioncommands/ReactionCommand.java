package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;

public abstract class ReactionCommand extends ForgeRegistryEntry<ReactionCommand> {
	String name;
	//int order;
	//int cooldown;
	//int max;

	String translationKey;


	public ReactionCommand(ResourceLocation registryName) {
		this.name = registryName.toString();
		//this.max = max;
		setRegistryName(registryName);
		//this.order = order;
		translationKey = "reactioncommand." + getRegistryName().getPath() + ".name";
	}

	public ReactionCommand(String registryName) {
		this(new ResourceLocation(registryName));
	}

	public String getName() {
		return name;
	}
	
	public String getTranslationKey() {
		return translationKey;
	}

	public abstract void onUse(PlayerEntity player, LivingEntity target);
	public abstract boolean conditionsToAppear(PlayerEntity player, LivingEntity target);
	
	

}