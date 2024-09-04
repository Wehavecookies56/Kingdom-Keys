package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.common.EventBusSubscriber;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;

public class ReactionMagic extends ReactionCommand {
	ResourceLocation magic;

	public ReactionMagic(ResourceLocation registryName) {
		super(registryName, false);
		this.magic = registryName;		
	}
	
	public String getMagicName() {
		return magic.toString();
	}
	
    @OnlyIn(Dist.CLIENT)
	@Override
	public String getTranslationKey() {
		PlayerData playerData = PlayerData.get(Minecraft.getInstance().player);
		int level = playerData.getMagicLevel(magic);
		Magic mag = ModMagic.registry.get(magic);
		//Maybe this will have to be re-enabled if we give access to -za magic to players without reaction commands
		/*if(level == mag.getMaxLevel()) { //If magic level is the same as the max keep it max
			level = mag.getMaxLevel();
		} else { //If magic level is not max increment it one level
			level++;
		}*/
		level++;
		if(mag.getGMAbility() != null && playerData.getNumberOfAbilitiesEquipped(mag.getGMAbility().getRegistryName().toString()) > 0) { //Get if the player has the -za
			level = mag.getMaxLevel()+1;
		}
		
        return "magic." + magic.getPath() + level+".name";
	}

	
	@Override
	public void onUse(Player player, LivingEntity target, LivingEntity lockedOnEntity) {
		Magic mag = ModMagic.registry.get(magic);
		PlayerData playerData = PlayerData.get(player);
		int level = playerData.getMagicLevel(magic);
		/*if(level == mag.getMaxLevel()) { //If magic level is the same as the max keep it max
			level = mag.getMaxLevel();
		} else { //If magic level is not max increment it one level
			level++;
		}*/
		level++;
		if(mag.getGMAbility() != null && playerData.getNumberOfAbilitiesEquipped(mag.getGMAbility().getRegistryName().toString()) > 0) { //Get if the player has the -za
			level = mag.getMaxLevel()+1;
		}
		
		mag.onUse(player, player, level, lockedOnEntity);
		playerData.removeReactionCommand(getRegistryName().toString());
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity target) {
		return true;
	}
	
}