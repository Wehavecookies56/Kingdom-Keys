package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class SCSyncWorldCapability {
	
	private CompoundTag data;

	public SCSyncWorldCapability() {
	}
	
	public SCSyncWorldCapability(IWorldCapabilities worldData) {
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
		ctx.get().enqueueWork(() -> {
			Level world = KingdomKeys.proxy.getClientWorld();
			IWorldCapabilities worldData = ModCapabilities.getWorld(world);
			worldData.read(message.data);
		});
		ctx.get().setPacketHandled(true);
	}

}
