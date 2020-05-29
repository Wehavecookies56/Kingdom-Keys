package online.kingdomkeys.kingdomkeys.network.packet;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;

public class PacketAntiPoints {

	int points;

	public PacketAntiPoints() {
	}

	public PacketAntiPoints(int points) {
		this.points = points;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.points);
	}

	public static PacketAntiPoints decode(PacketBuffer buffer) {
		PacketAntiPoints msg = new PacketAntiPoints();
		msg.points = buffer.readInt();
		return msg;
	}

	public static void handle(PacketAntiPoints message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities props = ModCapabilities.get(player);
			props.setAntiPoints(props.getAntiPoints() + message.points);
		});
		ctx.get().setPacketHandled(true);
	}

}
