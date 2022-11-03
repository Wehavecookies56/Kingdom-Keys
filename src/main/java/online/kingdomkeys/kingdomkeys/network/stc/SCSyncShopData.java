package online.kingdomkeys.kingdomkeys.network.stc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.synthesis.shop.ShopList;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class SCSyncShopData {

	public SCSyncShopData() {
	}

	public List<ShopList> list = new LinkedList<>();
	
	public SCSyncShopData(List<ShopList> recipes) {
		this.list = recipes;
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(list.size());
		CompoundTag compoundNBT = new CompoundTag();
		for(int i = 0; i < list.size(); i++) {
			compoundNBT.put("shop"+i, list.get(i).serializeNBT());
		}
		buffer.writeNbt(compoundNBT);
	}

	public static SCSyncShopData decode(FriendlyByteBuf buffer) {
		SCSyncShopData msg = new SCSyncShopData();
		int size = buffer.readInt();
		CompoundTag compoundNBT = buffer.readNbt();
		for (int i = 0; i < size; i++) {
			ShopList r = new ShopList();
			r.deserializeNBT((CompoundTag) compoundNBT.get("shop"+i));
			msg.list.add(r);
		}
		return msg;	
	}

	public static void handle(final SCSyncShopData message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.syncShopData(message)));
		ctx.get().setPacketHandled(true);
	}

}
