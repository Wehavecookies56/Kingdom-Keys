package online.kingdomkeys.kingdomkeys.network.stc;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.capability.ExtendedWorldData;

public class SCSyncExtendedWorld {
	
	private CompoundNBT data;

	public SCSyncExtendedWorld() {
	}
	
	public SCSyncExtendedWorld(ExtendedWorldData worldData) {
		this.data = new CompoundNBT();
		this.data = worldData.write(this.data);
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeCompoundTag(this.data);
	}

	public static SCSyncExtendedWorld decode(PacketBuffer buffer) {
		SCSyncExtendedWorld msg = new SCSyncExtendedWorld();
		msg.data = buffer.readCompoundTag();
		return msg;	
	}

	public static void handle(final SCSyncExtendedWorld message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			World world = KingdomKeys.proxy.getClientWorld();
			ExtendedWorldData worldData = ExtendedWorldData.get(world);
			worldData.read(message.data);
		});
		ctx.get().setPacketHandled(true);
	}

}
