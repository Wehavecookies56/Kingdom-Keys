package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class SCSyncWorldCapability {
	
	private CompoundNBT data;

	public SCSyncWorldCapability() {
	}
	
	public SCSyncWorldCapability(IWorldCapabilities worldData) {
		this.data = new CompoundNBT();
		this.data = worldData.write(this.data);
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeCompoundTag(this.data);
	}

	public static SCSyncWorldCapability decode(PacketBuffer buffer) {
		SCSyncWorldCapability msg = new SCSyncWorldCapability();
		msg.data = buffer.readCompoundTag();
		return msg;	
	}

	public static void handle(final SCSyncWorldCapability message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			World world = KingdomKeys.proxy.getClientWorld();
			IWorldCapabilities worldData = ModCapabilities.getWorld(world);
			worldData.read(message.data);
		});
		ctx.get().setPacketHandled(true);
	}

}
