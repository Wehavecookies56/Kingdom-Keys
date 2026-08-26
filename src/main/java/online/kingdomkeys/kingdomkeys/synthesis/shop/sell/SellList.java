package online.kingdomkeys.kingdomkeys.synthesis.shop.sell;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.LinkedList;
import java.util.List;

public class SellList {
    List<SellItem> list = new LinkedList<>();

    ResourceLocation registryName;

    public SellList() {}

    public SellList(CompoundTag tag) {
        deserializeNBT(tag);
    }

    public SellList(ResourceLocation rl, List<SellItem> list) {
        this.registryName = rl;
        this.list = list;
    }

    public List<SellItem> getList() {
        return list;
    }

    public void setList(List<SellItem> list) {
        this.list = list;
    }

    public void addToList(SellItem shopItem) {
        this.list.add(shopItem);
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();

        nbt.putString("regname", getRegistryName().toString());
        nbt.putInt("len", list.size());
        for(int i=0;i<list.size();i++) {
            SellItem shopItem = list.get(i);
            nbt.put("shop_item_"+i, shopItem.serializeNBT());
        }

        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.list.clear();
        for(int i=0;i<nbt.getInt("len");i++) {
            SellItem shopItem = new SellItem();
            shopItem.deserializeNBT(nbt.getCompound("shop_item_"+i));
            this.list.add(shopItem);
        }
        this.setRegistryName(nbt.getString("regname"));

    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }
    public void setRegistryName(String registryName) {
        this.registryName = KingdomKeys.rl(registryName);
    }

    public void setRegistryName(ResourceLocation registryName) {
        this.registryName = registryName;
    }

    public static final StreamCodec<FriendlyByteBuf, SellList> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.COMPOUND_TAG,
            SellList::serializeNBT,
            SellList::new
    );
}
