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

public record SCSendPlayerDataToClient(CompoundTag playerData) implements Packet {

    public static final Type<SCSendPlayerDataToClient> TYPE = new Type<>(KingdomKeys.rl("sc_send_player_data_to_client"));

    public static final StreamCodec<FriendlyByteBuf, SCSendPlayerDataToClient> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            SCSendPlayerDataToClient::playerData,
            SCSendPlayerDataToClient::new
    );

    public SCSendPlayerDataToClient(Player player) {
        this(PlayerData.get(player).serializeNBT(player.level().registryAccess()));
    }

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.sendPlayerDataToClient(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
