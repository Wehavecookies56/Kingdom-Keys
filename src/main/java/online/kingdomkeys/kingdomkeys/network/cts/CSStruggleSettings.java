package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.lib.Struggle;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldData;
import online.kingdomkeys.kingdomkeys.util.Utils;

public record CSStruggleSettings(Struggle struggle) implements Packet {

	public static final Type<CSStruggleSettings> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_struggle_settings"));

	public static final StreamCodec<FriendlyByteBuf, CSStruggleSettings> STREAM_CODEC = StreamCodec.composite(
			Struggle.STREAM_CODEC,
			CSStruggleSettings::struggle,
			CSStruggleSettings::new
	);

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		WorldData worldData = WorldData.get(player.getServer());
		Struggle p = worldData.getStruggleFromBlockPos(struggle.blockPos);

		p.setSize(struggle.getSize());
		p.setDamageMult(struggle.getDamageMult());
		p.setName(struggle.getName());
		p.setC1(struggle.c1);
		p.setC2(struggle.c2);

		PacketHandler.sendToAll(new SCSyncWorldData(player.getServer()));
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
