package online.kingdomkeys.kingdomkeys.network.cts;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.entity.mob.MoogleEntity;

import java.util.UUID;
import java.util.function.Supplier;

public class CSCloseMoogleGUI {

    public CSCloseMoogleGUI() {}

    int moogle;

    public CSCloseMoogleGUI(int moogle) {
        this.moogle = moogle;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(moogle);
    }

    public static CSCloseMoogleGUI decode(FriendlyByteBuf buffer) {
        CSCloseMoogleGUI msg = new CSCloseMoogleGUI();
        msg.moogle = buffer.readInt();
        return msg;
    }

    public static void handle(CSCloseMoogleGUI message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = ctx.get().getSender().level();
            MoogleEntity entity = (MoogleEntity) level.getEntity(message.moogle);
            if (entity != null && !entity.isDeadOrDying()) {
                entity.stopInteracting();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
