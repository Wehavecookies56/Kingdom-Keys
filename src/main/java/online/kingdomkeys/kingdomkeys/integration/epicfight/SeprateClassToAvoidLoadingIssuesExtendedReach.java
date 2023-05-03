package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class SeprateClassToAvoidLoadingIssuesExtendedReach {
    private SeprateClassToAvoidLoadingIssuesExtendedReach() {}

    public static boolean isBattleMode(Player player)
    {
        return KingdomKeys.efmLoaded && EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class).isBattleMode();
    }
}
