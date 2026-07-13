package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.lib.KKRegistryObject;

public abstract class ReactionCommand implements KKRegistryObject {
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

	/**
	 * If needsConstantCheck() is true this should not be just true, since it will get added each tick.
	 * @param player
	 * @param target
	 * @return
	 */
	public abstract boolean conditionsToAppear(Player player, LivingEntity target);

	public void tick(Player player){
		PlayerData playerData = PlayerData.get(player);

		if(duration > -1){
			if(playerData.getReactionCommands().containsKey(getRegistryName())){
                playerData.getReactionCommands().compute(name, (k, duration) -> duration - 1);

				//Remove cuz it expired
				if(playerData.getReactionCommands().get(getRegistryName()) == 0){
					playerData.getReactionCommands().remove(getRegistryName());
				}
			}
		}

		if (!conditionsToAppear(player, player)) {
			playerData.getReactionCommands().remove(getRegistryName());
		}
	}

	@Override
	public ResourceLocation getRegistryName() {
		return name;
	}

}