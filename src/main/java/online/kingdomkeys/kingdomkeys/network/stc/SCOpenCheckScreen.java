package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.core.UUIDUtil;
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

import java.util.UUID;

public record SCOpenCheckScreen(CompoundTag playerData, UUID uuid, String name) implements Packet {

    public static final Type<SCOpenCheckScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_open_check_screen"));

    public static final StreamCodec<FriendlyByteBuf, SCOpenCheckScreen> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG, SCOpenCheckScreen::playerData,
            UUIDUtil.STREAM_CODEC, SCOpenCheckScreen::uuid,
            ByteBufCodecs.STRING_UTF8, SCOpenCheckScreen::name,
            SCOpenCheckScreen::new
    );

    public SCOpenCheckScreen(PlayerData playerData, Player player) {
        this(playerData.serializeNBT(player.level().registryAccess()), player.getUUID(), player.getGameProfile().getName());
    }

    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.openCheckScreen(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
