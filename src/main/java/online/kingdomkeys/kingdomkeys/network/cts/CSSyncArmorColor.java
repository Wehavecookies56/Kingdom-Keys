package online.kingdomkeys.kingdomkeys.network.cts;


import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;

public record CSSyncArmorColor(int color, boolean glint) implements Packet {

    public static final Type<CSSyncArmorColor> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_sync_armor_color"));

    public static final StreamCodec<FriendlyByteBuf, CSSyncArmorColor> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            CSSyncArmorColor::color,
            ByteBufCodecs.BOOL,
            CSSyncArmorColor::glint,
            CSSyncArmorColor::new
    );

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        PlayerData playerData = PlayerData.get(player);
        playerData.setArmorColor(color);
        playerData.setArmorGlint(glint);
        PacketHandler.syncToAllAround(player, playerData);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
