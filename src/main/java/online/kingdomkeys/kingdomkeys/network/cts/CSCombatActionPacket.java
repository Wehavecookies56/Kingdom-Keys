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
import online.kingdomkeys.kingdomkeys.util.CombatAbilities;

/**
 * The player pressed something during one of the short windows combat opens up. The client only asks; the
 * server still checks the window is really open before doing anything, so a spoofed packet buys nothing.
 */
public record CSCombatActionPacket(boolean counter) implements Packet {

	public static final Type<CSCombatActionPacket> TYPE = new Type<>(KingdomKeys.rl("cs_combat_action"));

	public static final StreamCodec<FriendlyByteBuf, CSCombatActionPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.BOOL,
			CSCombatActionPacket::counter,
			CSCombatActionPacket::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData data = PlayerData.get(player);

		if (data == null) {
			return;
		}

		if (counter) {
			CombatAbilities.counter(player, data);
		} else {
			CombatAbilities.recover(player, data);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
