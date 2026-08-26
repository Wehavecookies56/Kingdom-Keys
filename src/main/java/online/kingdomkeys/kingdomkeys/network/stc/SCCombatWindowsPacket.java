package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;

/**
 * Syncs all the combat variables
 */
public record SCCombatWindowsPacket(int entityID, int guardTicks, int counterTicks, int recoveryTicks, int spinTicks, int ringTicks, int flashTicks) implements Packet {
	public static final Type<SCCombatWindowsPacket> TYPE = new Type<>(KingdomKeys.rl("sc_combat_windows"));

	// Written out by hand rather than composed: composite only goes up to six parts and this carries seven
	public static final StreamCodec<FriendlyByteBuf, SCCombatWindowsPacket> STREAM_CODEC = StreamCodec.of(
			(buffer, packet) -> {
				buffer.writeVarInt(packet.entityID());
				buffer.writeVarInt(packet.guardTicks());
				buffer.writeVarInt(packet.counterTicks());
				buffer.writeVarInt(packet.recoveryTicks());
				buffer.writeVarInt(packet.spinTicks());
				buffer.writeVarInt(packet.ringTicks());
				buffer.writeVarInt(packet.flashTicks());
			},
			buffer -> new SCCombatWindowsPacket(buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt(), buffer.readVarInt())
	);

	@Override
	public void handle(IPayloadContext context) {
		if (!FMLEnvironment.dist.isClient()) {
			return;
		}

		Entity entity = context.player().level().getEntity(entityID);
		if (!(entity instanceof Player player)) {
			return;
		}

		PlayerData data = PlayerData.get(player);

		if (data != null) {
			data.setGuardTicks(guardTicks);
			data.setCounterTicks(counterTicks);
			data.setRecoveryTicks(recoveryTicks);
			data.setCounterSpinTicks(spinTicks);
			data.setCounterRingTicks(ringTicks);
			data.setRecoveryFlashTicks(flashTicks);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
