package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.cts.CSOpenMagicCustomize;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SCOpenMagicCustomize {

    LinkedHashMap<String, int[]> knownMagic = new LinkedHashMap<>();

    public SCOpenMagicCustomize() {}
    public SCOpenMagicCustomize(LinkedHashMap<String, int[]> knownMagic) {
        this.knownMagic = knownMagic;
    }

    public void encode(FriendlyByteBuf buffer) {
        CompoundTag magic = new CompoundTag();
        Iterator<Map.Entry<String, int[]>> magicsIt = knownMagic.entrySet().iterator();
        while (magicsIt.hasNext()) {
            Map.Entry<String, int[]> pair = magicsIt.next();
            magic.putIntArray(pair.getKey().toString(), pair.getValue());
        }
        buffer.writeNbt(magic);
    }

    public static SCOpenMagicCustomize decode(FriendlyByteBuf buffer) {
        SCOpenMagicCustomize msg = new SCOpenMagicCustomize();
        CompoundTag tag = buffer.readNbt();
        Iterator<String> iterator = tag.getAllKeys().iterator();
        while (iterator.hasNext()) {
            String magicName = iterator.next();
            msg.knownMagic.put(magicName, tag.getIntArray(magicName));
        }
        return msg;
    }

    public static void handle(SCOpenMagicCustomize message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.openMagicCustomize(message.knownMagic)));
        ctx.get().setPacketHandled(true);
    }
}
