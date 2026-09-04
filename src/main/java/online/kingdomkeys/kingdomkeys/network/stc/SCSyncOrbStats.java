package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCSyncOrbStats(int munny, double mp, double dp, double fp, double focus, int lux) implements Packet {

	public static final Type<SCSyncOrbStats> TYPE = new Type<>(KingdomKeys.rl("sc_sync_orb_stats"));

	public static final StreamCodec<FriendlyByteBuf, SCSyncOrbStats> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.VAR_INT, SCSyncOrbStats::munny,
			ByteBufCodecs.DOUBLE, SCSyncOrbStats::mp,
			ByteBufCodecs.DOUBLE, SCSyncOrbStats::dp,
			ByteBufCodecs.DOUBLE, SCSyncOrbStats::fp,
			ByteBufCodecs.DOUBLE, SCSyncOrbStats::focus,
			ByteBufCodecs.VAR_INT, SCSyncOrbStats::lux,
			SCSyncOrbStats::new
	);

	public SCSyncOrbStats(PlayerData playerData) {
		this(playerData.getMunny(), playerData.getMP(), playerData.getDP(), playerData.getFP(), playerData.getFocus(), playerData.getLux());
	}

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			Player player = context.player();
			PlayerData playerData = PlayerData.get(player);
			if (playerData == null)
				return;

			playerData.setMunny(munny);
			playerData.setMP(mp);
			playerData.setDP(dp);
			playerData.setFP(fp);
			playerData.setFocus(focus);
			playerData.setLux(lux);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
