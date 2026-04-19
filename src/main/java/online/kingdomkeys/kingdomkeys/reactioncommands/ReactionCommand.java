package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;

public abstract class ReactionCommand {
	ResourceLocation name;
	int duration;
	int color;

	String translationKey;
	boolean constantCheck;

	public ReactionCommand(ResourceLocation registryName, boolean constantCheck, int duration, int color) {
		this.name = registryName;
		this.duration = duration;
		this.constantCheck = constantCheck;
		translationKey = "reactioncommand." + registryName.getNamespace() + "." + registryName.getPath() + ".name";
		this.color = color;
	}

	public ReactionCommand(ResourceLocation registryName, boolean constantCheck, int duration) {
		this(registryName, constantCheck, duration, 0x8800FF);
	}

	public ReactionCommand(String registryName, boolean constantCheck, int duration) {
		this(ResourceLocation.parse(registryName), constantCheck, duration);
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

	public int getDuration() {
		return duration;
	}

	public int getColor() {
		return color;
	}
	
    @OnlyIn(Dist.CLIENT)
	public String getTranslationKey() {
		return translationKey;
	}

	public abstract void onUse(Player player, LivingEntity target, LivingEntity lockedOnEntity);
	public abstract boolean conditionsToAppear(Player player, LivingEntity target);

	public void tick(Player player){
		PlayerData playerData = PlayerData.get(player);

		if(duration > -1){
			if(playerData.getReactionCommands().containsKey(getName())){
                playerData.getReactionCommands().compute(name.toString(), (k, duration) -> duration - 1);

				//Remove cuz it expired
				if(playerData.getReactionCommands().get(getName()) == 0){
					playerData.getReactionCommands().remove(getName());
				}
			}
		}

		if (!conditionsToAppear(player, player)) {
			playerData.getReactionCommands().remove(getName());
		}
	}
	
	public ResourceLocation getRegistryName() {
		return name;
	}

}