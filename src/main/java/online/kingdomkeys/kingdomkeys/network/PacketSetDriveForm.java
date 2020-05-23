package online.kingdomkeys.kingdomkeys.network;

import java.util.function.Supplier;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.capability.IPlayerCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;

public class PacketSetDriveForm {

	String form;

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
		int length = buffer.readInt();
		msg.form = buffer.readString(length);
		return msg;
	}

	public static void handle(PacketSetDriveForm message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = ctx.get().getSender();
			IPlayerCapabilities props = ModCapabilities.get(player);
			System.out.println("Actual form: "+props.getDriveForm()+", Going to get form: "+message.form);
			if (!props.getDriveForm().equals("") && message.form.equals("")) { // If is in a drive form and the target is "" (player)
				DriveForm form = ModDriveForms.registry.getValue(new ResourceLocation(props.getDriveForm()));
				form.endDrive(player);
			} else if (!message.form.equals("")) { // If is not in a form and wants to drive
				DriveForm form = ModDriveForms.registry.getValue(new ResourceLocation(message.form));
				form.initDrive(player);
			}

		});
		ctx.get().setPacketHandled(true);
	}

}
