package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.limit.Limit;
import online.kingdomkeys.kingdomkeys.limit.ModLimits;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncPlayerData;

public record CSUseLimitPacket(ResourceLocation limit, int targetID) implements Packet {
	
	public static final Type<CSUseLimitPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_use_limit_packet"));

	public static final StreamCodec<FriendlyByteBuf, CSUseLimitPacket> STREAM_CODEC = StreamCodec.composite(
			ResourceLocation.STREAM_CODEC,
			CSUseLimitPacket::limit,
			ByteBufCodecs.INT,
			CSUseLimitPacket::targetID,
			CSUseLimitPacket::new
	);

	public CSUseLimitPacket(ResourceLocation limit, LivingEntity target) {
		this(limit, target.getId());
	}

	public CSUseLimitPacket(ResourceLocation limit) {
		this(limit, -1);
	}

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		PlayerData playerData = PlayerData.get(player);
		Limit limit = ModLimits.registry.get(this.limit);
		int cost = limit.getCost();
		if (playerData.getDP() >= cost) {
			playerData.remDP(cost);
			playerData.setLimitCooldownTicks(limit.getCooldown());
			PacketHandler.sendTo(new SCSyncPlayerData(player), (ServerPlayer)player);
			if(targetID > -1) {
				limit.onUse(player, (LivingEntity) player.level().getEntity(targetID));
			} else {
				limit.onUse(player, player);
			}

		}

		PacketHandler.syncToAllAround(player, playerData);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}