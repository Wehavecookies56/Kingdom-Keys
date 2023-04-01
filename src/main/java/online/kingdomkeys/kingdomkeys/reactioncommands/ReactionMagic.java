package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;

@Mod.EventBusSubscriber(modid = KingdomKeys.MODID)
public class ReactionMagic extends ReactionCommand {
	String magic;

	public ReactionMagic(String registryName) {
		super(registryName, false);
		this.magic = registryName;
	}
	
	public String getMagicName() {
		return magic;
	}
	
    @OnlyIn(Dist.CLIENT)
	@Override
	public String getTranslationKey() {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(Minecraft.getInstance().player);
		int level = playerData.getMagicLevel(magic);
		Magic mag = ModMagic.registry.get().getValue(new ResourceLocation(getMagicName()));
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
		
		
        return "magic." + magic.replace(KingdomKeys.MODID+":", "") + level+".name";
	}

	
	@Override
	public void onUse(Player player, LivingEntity target) {
		Magic mag = ModMagic.registry.get().getValue(new ResourceLocation(getMagicName()));
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		int level = playerData.getMagicLevel(getMagicName());
		/*if(level == mag.getMaxLevel()) { //If magic level is the same as the max keep it max
			level = mag.getMaxLevel();
		} else { //If magic level is not max increment it one level
			level++;
		}*/
		level++;
		if(mag.getGMAbility() != null && playerData.getNumberOfAbilitiesEquipped(mag.getGMAbility().getRegistryName().toString()) > 0) { //Get if the player has the -za
			level = mag.getMaxLevel()+1;
		}
		
		mag.onUse(player, player, level);
		playerData.removeReactionCommand(getRegistryName().toString());
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity target) {
		return true;
	}
	
}