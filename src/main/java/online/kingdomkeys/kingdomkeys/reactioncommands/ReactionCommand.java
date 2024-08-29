package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;

public abstract class ReactionCommand {
	ResourceLocation name;
	//int order;
	//int cooldown;
	//int max;

	String translationKey;
	boolean constantCheck;


	public ReactionCommand(ResourceLocation registryName, boolean constantCheck) {
		this.name = registryName;
		//this.max = max;
		this.constantCheck = constantCheck;
		translationKey = "reactioncommand." + registryName.getPath() + ".name";
	}

	public ReactionCommand(String registryName, boolean constantCheck) {
		this(ResourceLocation.parse(registryName), constantCheck);
	}

	public SoundEvent getUseSound(Player player, LivingEntity target) {
		return ModSounds.menu_in.get();
	}

	public String getName() {
		return name.toString();
	}

	public boolean needsConstantCheck() {
		return constantCheck;
	}
	
    @OnlyIn(Dist.CLIENT)
	public String getTranslationKey() {
		return translationKey;
	}

	public abstract void onUse(Player player, LivingEntity target, LivingEntity lockedOnEntity);
	public abstract boolean conditionsToAppear(Player player, LivingEntity target);
	
	public ResourceLocation getRegistryName() {
		return name;
	}

}