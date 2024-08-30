package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.gui.synthesis.SynthesisMaterialScreen;
import online.kingdomkeys.kingdomkeys.network.Packet;

public record SCOpenMaterialsScreen(String inv, String name, int moogle) implements Packet {

    public static final Type<SCOpenMaterialsScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "sc_open_materials_screen"));

    public static final StreamCodec<FriendlyByteBuf, SCOpenMaterialsScreen> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SCOpenMaterialsScreen::inv,
            ByteBufCodecs.STRING_UTF8,
            SCOpenMaterialsScreen::name,
            ByteBufCodecs.INT,
            SCOpenMaterialsScreen::moogle,
            SCOpenMaterialsScreen::new
    );

    public SCOpenMaterialsScreen(int moogle) {
    	this("", "", moogle);
    }


    @Override
    public void handle(IPayloadContext context) {
        if (FMLEnvironment.dist.isClient()) {
            Minecraft.getInstance().setScreen(new SynthesisMaterialScreen(inv, name, moogle));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
