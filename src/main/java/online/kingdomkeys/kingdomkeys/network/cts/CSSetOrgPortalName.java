package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.UUID;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.block.OrgPortalTileEntity;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncWorldData;

public record CSSetOrgPortalName(BlockPos pos, String name) implements Packet {

    public static final Type<CSSetOrgPortalName> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_set_org_portal_name"));

    public static final StreamCodec<FriendlyByteBuf, CSSetOrgPortalName> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            CSSetOrgPortalName::pos,
            ByteBufCodecs.STRING_UTF8,
            CSSetOrgPortalName::name,
            CSSetOrgPortalName::new
    );

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        if(player.level().getBlockEntity(pos) != null && player.level().getBlockEntity(pos) instanceof OrgPortalTileEntity) {
            OrgPortalTileEntity te = (OrgPortalTileEntity) player.level().getBlockEntity(pos);
            UUID portalUUID = te.getUUID();
            WorldData.get(player.getServer()).getPortalFromUUID(portalUUID).setName(name);
            PacketHandler.sendTo(new SCSyncWorldData(player.getServer()), (ServerPlayer) player);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}