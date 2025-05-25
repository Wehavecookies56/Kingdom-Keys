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

public record SCOpenMaterialsScreen(CompoundTag playerData, String inv, String name, int moogle) implements Packet {

    public static final Type<SCOpenMaterialsScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_open_materials_screen"));

    public static final StreamCodec<FriendlyByteBuf, SCOpenMaterialsScreen> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            SCOpenMaterialsScreen::playerData,
            ByteBufCodecs.STRING_UTF8,
            SCOpenMaterialsScreen::inv,
            ByteBufCodecs.STRING_UTF8,
            SCOpenMaterialsScreen::name,
            ByteBufCodecs.INT,
            SCOpenMaterialsScreen::moogle,
            SCOpenMaterialsScreen::new
    );

    public SCOpenMaterialsScreen(PlayerData playerData, Player player, int moogle) {
    	this(playerData.serializeNBT(player.level().registryAccess()), "", "", moogle);
    }

    public SCOpenMaterialsScreen(PlayerData playerData, Player player, String inv, String name, int moogle) {
        this(playerData.serializeNBT(player.level().registryAccess()), inv, name, moogle);
    }


    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            ClientPacketHandler.openMaterialsScreen(this);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
