package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;

import java.util.UUID;
import java.util.function.Supplier;

public record SCDeleteSavePointScreenshot(String name, UUID uuid) {
    public SCDeleteSavePointScreenshot(FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readUUID());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(name);
        buf.writeUUID(uuid);
    }

    public static void handle(SCDeleteSavePointScreenshot message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.deleteScreenshot(message)));
        ctx.get().setPacketHandled(true);
    }
}
