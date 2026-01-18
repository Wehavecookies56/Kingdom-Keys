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

public record SCOpenSellScreen(CompoundTag playerData, String inv, String name, int moogle) implements Packet {

    public static final Type<SCOpenSellScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_open_sell_screen"));

    public static final StreamCodec<FriendlyByteBuf, SCOpenSellScreen> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            SCOpenSellScreen::playerData,
            ByteBufCodecs.STRING_UTF8,
            SCOpenSellScreen::inv,
            ByteBufCodecs.STRING_UTF8,
            SCOpenSellScreen::name,
            ByteBufCodecs.INT,
            SCOpenSellScreen::moogle,
            SCOpenSellScreen::new
    );

    public SCOpenSellScreen(PlayerData playerData, Player player, int moogle) {
    	this(playerData.serializeNBT(player.level().registryAccess()), "", "", moogle);
    }

    public SCOpenSellScreen(PlayerData playerData, Player player, String inv, String name, int moogle) {
        this(playerData.serializeNBT(player.level().registryAccess()), inv, name, moogle);
    }


    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.openSellScreen(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
