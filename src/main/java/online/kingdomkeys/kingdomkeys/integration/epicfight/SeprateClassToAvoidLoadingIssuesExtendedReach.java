package online.kingdomkeys.kingdomkeys.integration.epicfight;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSSummonKeyblade;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class SeprateClassToAvoidLoadingIssuesExtendedReach {
    private SeprateClassToAvoidLoadingIssuesExtendedReach() {}

    public static boolean isBattleMode(Player player)
    {
        return KingdomKeys.efmLoaded && EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class).isBattleMode();
    }
    public static void SummonKeyblade(PlayerPatch<?> ep){
        if (ep.getOriginal().getLevel().isClientSide && (ep.isBattleMode()))
        {
            PacketHandler.sendToServer(new CSSummonKeyblade());
        }
    }
}
