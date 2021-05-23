package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
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
		super(registryName);
		this.magic = registryName;
	}
	
	public String getMagicName() {
		return magic;
	}
	
    @OnlyIn(Dist.CLIENT)
	@Override
	public String getTranslationKey() {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(Minecraft.getInstance().player);
		int level = playerData.getMagicLevel(magic) +1;
        return "magic." + magic.replace(KingdomKeys.MODID+":", "") + level+".name";
	}

	
	@Override
	public void onUse(PlayerEntity player, LivingEntity target) {
		Magic magic = ModMagic.registry.getValue(new ResourceLocation(getMagicName()));
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		int level = playerData.getMagicLevel(getMagicName());
		magic.onUse(player, player, level+1);
		playerData.removeReactionCommand(getRegistryName().toString());
	}

	@Override
	public boolean conditionsToAppear(PlayerEntity player, LivingEntity target) {
		return true;
	}
	
}