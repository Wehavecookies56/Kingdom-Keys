package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class ReactionMagic extends ReactionCommand {
	ResourceLocation magic;

	public ReactionMagic(ResourceLocation registryName) {
		super(registryName, false, 20*20, 0x8800FF);
		this.magic = registryName;		
	}
	
	public String getMagicName() {
		return magic.toString();
	}

	public void setMagic(ResourceLocation magic) {
		this.magic = magic;
	}
	
    @OnlyIn(Dist.CLIENT)
	@Override
	public String getTranslationKey() {
		PlayerData playerData = PlayerData.get(Minecraft.getInstance().player);
	    Magic mag = ModMagic.registry.get(magic);
	    if(mag.getGMAbility() != null && playerData.getNumberOfAbilitiesEquipped(mag.getGMAbility().getRegistryName().toString()) > 0) { //Get if the player has the -za
		    Magic current = mag;
		    Magic next = current.getNextTierMagic();

		    while (next != current) {
			    current = next;
			    next = current.getNextTierMagic();
		    }

		    mag = current;
	    } else {
		    mag = mag.getNextTierMagic();
	    }
		
        return mag.getTranslationKey();
	}

	
	@Override
	public void onUse(Player player, LivingEntity target, LivingEntity lockedOnEntity) {
		Magic mag = ModMagic.registry.get(magic);
		PlayerData playerData = PlayerData.get(player);

		//Get if player has za, if so get the next magic till it returns null and return the last non null value
		//If doesn't have za get the immediately next magic
		if(mag.getGMAbility() != null && playerData.getNumberOfAbilitiesEquipped(mag.getGMAbility().getRegistryName().toString()) > 0) { //Get if the player has the -za
			Magic current = mag;
			Magic next = current.getNextTierMagic();

			while (next != current) {
				current = next;
				next = current.getNextTierMagic();
			}

			mag = current;
		} else {
			mag = mag.getNextTierMagic();
		}
		//int level = Utils.getMagicHighestLevel(playerData.getEquippedMagics(),magic.toString());
		/*if(level == mag.getMaxLevel()) { //If magic level is the same as the max keep it max
			level = mag.getMaxLevel();
		} else { //If magic level is not max increment it one level
			level++;
		}
		level++;
		if(mag.getGMAbility() != null && playerData.getNumberOfAbilitiesEquipped(mag.getGMAbility().getRegistryName().toString()) > 0) { //Get if the player has the -za
			level = mag.getMaxLevel()+1;
		}
		*/
		mag.onUse(player, player, lockedOnEntity, true);
		playerData.removeReactionCommand(getRegistryName().toString());
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity target) {
		return true;
	}
}