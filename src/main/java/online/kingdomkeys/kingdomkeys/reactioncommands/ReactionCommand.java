package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class ReactionCommand extends ForgeRegistryEntry<ReactionCommand> {
	String name;
	//int order;
	//int cooldown;
	//int max;

	String translationKey;
	boolean constantCheck;


	public ReactionCommand(ResourceLocation registryName, boolean constantCheck) {
		this.name = registryName.toString();
		//this.max = max;
		setRegistryName(registryName);
		this.constantCheck = constantCheck;
		translationKey = "reactioncommand." + getRegistryName().getPath() + ".name";
	}

	public ReactionCommand(String registryName, boolean constantCheck) {
		this(new ResourceLocation(registryName), constantCheck);
	}

	public String getName() {
		return name;
	}
	
	public boolean needsConstantCheck() {
		return constantCheck;
	}
	
    @OnlyIn(Dist.CLIENT)
	public String getTranslationKey() {
		return translationKey;
	}

	public abstract void onUse(PlayerEntity player, LivingEntity target);
	public abstract boolean conditionsToAppear(PlayerEntity player, LivingEntity target);
	
	

}