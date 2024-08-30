package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.ClientPacketHandler;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCSyncPlayerData(int player, CompoundTag data) implements Packet {

	public static final Type<SCSyncPlayerData> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_sync_player_data"));

	public static final StreamCodec<FriendlyByteBuf, SCSyncPlayerData> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			SCSyncPlayerData::player,
			ByteBufCodecs.COMPOUND_TAG,
			SCSyncPlayerData::data,
			SCSyncPlayerData::new
	);

	public SCSyncPlayerData(Player player, PlayerData playerData) {
		this(player.getId(), playerData.serializeNBT(player.level().registryAccess()));
	}

	@Override
	public void handle(IPayloadContext context) {
		if (FMLEnvironment.dist.isClient()) {
			ClientPacketHandler.syncCapability(this);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
