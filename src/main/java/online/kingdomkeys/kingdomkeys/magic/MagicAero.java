package online.kingdomkeys.kingdomkeys.magic;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.ability.Ability;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.reactioncommands.ReactionCommand;

public class MagicAero extends Magic {

	public MagicAero(String registryName, int maxLevel, boolean hasRC, String gmAbility, int order) {
		super(registryName, true, maxLevel, hasRC, gmAbility, order);
	}

	@Override
	protected void magicUse(Player player, Player caster, int level, float fullMPBlastMult) {
		IPlayerCapabilities playerData = ModCapabilities.getPlayer(player);
		int time = (int) (ModCapabilities.getPlayer(caster).getMaxMP() * (4F + level/2F) * getDamageMult(level));
		playerData.setAeroTicks(time, level);
		PacketHandler.syncToAllAround(player, playerData);
		caster.swing(InteractionHand.MAIN_HAND);
	}

}