package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.data.WorldData;

public class SCSyncWorldCapability {
	
	public CompoundTag data;

	public SCSyncWorldCapability() {
	}
	
	public SCSyncWorldCapability(WorldData worldData) {
		this.data = new CompoundTag();
		this.data = worldData.write(this.data);
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeNbt(this.data);
	}

	public static SCSyncWorldCapability decode(FriendlyByteBuf buffer) {
		SCSyncWorldCapability msg = new SCSyncWorldCapability();
		msg.data = buffer.readNbt();
		return msg;	
	}

	public static void handle(final SCSyncWorldCapability message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.syncWorldCapability(message)));
		ctx.get().setPacketHandled(true);
	}

}
