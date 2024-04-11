package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopList;
import online.kingdomkeys.kingdomkeys.synthesis.shop.names.NamesListRegistry;

import java.util.*;
import java.util.function.Supplier;

public class SCSyncMoogleNames {

    public Map<ResourceLocation, List<String>> names;

    public SCSyncMoogleNames() {}

    public SCSyncMoogleNames(NamesListRegistry names) {
        this.names = names.getRegistry();
    }

    public void encode(FriendlyByteBuf buffer) {
        List<ResourceLocation> keys = names.keySet().stream().toList();
        List<List<String>> values = names.values().stream().toList();
        int size = names.size();
        buffer.writeInt(size);
        for(int i = 0; i < size; i++) {
            int valueSize = values.get(i).size();
            buffer.writeInt(valueSize);
            buffer.writeUtf(keys.get(i).toString());
            for (int j = 0; j < valueSize; j++) {
                buffer.writeUtf(values.get(i).get(j));
            }
        }
    }

    public static SCSyncMoogleNames decode(FriendlyByteBuf buffer) {
        SCSyncMoogleNames msg = new SCSyncMoogleNames();
        Map<ResourceLocation, List<String>> registry = new HashMap<>();
        int size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            int valueSize = buffer.readInt();
            ResourceLocation location = new ResourceLocation(buffer.readUtf());
            List<String> list = new ArrayList<>();
            for (int j = 0; j < valueSize; j++) {
                list.add(buffer.readUtf());
            }
            registry.put(location, list);
        }
        msg.names = registry;
        return msg;
    }

    public static void handle(final SCSyncMoogleNames message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.syncMoogleNames(message)));
        ctx.get().setPacketHandled(true);
    }
}
