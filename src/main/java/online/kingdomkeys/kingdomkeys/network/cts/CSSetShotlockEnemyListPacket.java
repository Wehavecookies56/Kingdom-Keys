package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;

public record CSSetShotlockEnemyListPacket(List<Utils.ShotlockPosition> shotlockEnemies) implements Packet {

	public static final Type<CSSetShotlockEnemyListPacket> TYPE = new Type<>(KingdomKeys.rl("cs_set_shotlock_enemy_list"));

    public static final StreamCodec<FriendlyByteBuf, CSSetShotlockEnemyListPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, Utils.ShotlockPosition.STREAM_CODEC),
            CSSetShotlockEnemyListPacket::shotlockEnemies,
            CSSetShotlockEnemyListPacket::new
    );

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);
		playerData.setShotlockEnemies(shotlockEnemies);
		PacketHandler.syncToAllAround(player, playerData);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
