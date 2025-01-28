package online.kingdomkeys.kingdomkeys.client.gui;

import net.minecraft.world.entity.player.Player;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSendPlayerDataToClient;

public interface IPlayerDataRequester {

    void updatePlayerData(PlayerData playerData);

    default void requestData(Player player) {
    }
}
