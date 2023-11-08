package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSummonKeyblade;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class SeparateClassToAvoidLoadingIssuesExtendedReach {
    private SeparateClassToAvoidLoadingIssuesExtendedReach() {}

    public static boolean isBattleMode(Player player)
    {
        return KingdomKeys.efmLoaded && EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class).isBattleMode();
    }
}
