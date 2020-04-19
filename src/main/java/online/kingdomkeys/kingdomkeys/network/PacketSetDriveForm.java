package online.kingdomkeys.kingdomkeys.network;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.ILevelCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;

public class PacketSetDriveForm {

	private String form;

	public PacketSetDriveForm() {
	}

	public PacketSetDriveForm(String form) {
		this.form = form;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.form.length());
		buffer.writeString(this.form);
	}

	public static PacketSetDriveForm decode(PacketBuffer buffer) {
		PacketSetDriveForm msg = new PacketSetDriveForm();
		int len = buffer.readInt();
		msg.form = buffer.readString(len);
		return msg;
	}

	public static void handle(PacketSetDriveForm message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			ILevelCapabilities props = ModCapabilities.get(player);
			props.setDriveForm(message.form);

			System.out.println(props.getDriveForm());
			PacketHandler.syncToAllAround(player, props);
		});
		ctx.get().setPacketHandled(true);
	}

}
