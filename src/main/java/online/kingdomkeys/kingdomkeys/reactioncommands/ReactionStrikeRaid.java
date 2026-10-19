package online.kingdomkeys.kingdomkeys.reactioncommands;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.magic.MagicStrikeRaid;

public class ReactionStrikeRaid extends ReactionCommand {
	private final boolean judgment;

	public ReactionStrikeRaid(ResourceLocation registryName, boolean judgment) {
		super(registryName, false, MagicStrikeRaid.WINDOW);
		this.judgment = judgment;
	}

	@Override
	public void onUse(Player player, LivingEntity target, LivingEntity lockedOnEntity) {
		PlayerData.get(player).removeReactionCommand(getRegistryName());
		MagicStrikeRaid.followUp(player, judgment);
	}

	@Override
	public boolean conditionsToAppear(Player player, LivingEntity target) {
		return true;
	}
}
