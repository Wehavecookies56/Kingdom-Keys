package online.kingdomkeys.kingdomkeys.network.stc;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.driveform.DriveForm;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormData;
import online.kingdomkeys.kingdomkeys.driveform.DriveFormDataDeserializer;
import online.kingdomkeys.kingdomkeys.driveform.ModDriveForms;

public class SCSyncDriveFormData {

	public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(DriveFormData.class, new DriveFormDataDeserializer()).setPrettyPrinting().create();

	public SCSyncDriveFormData() {
	}

	List<String> names = new LinkedList<String>();
	List<String> data = new LinkedList<String>();

	public SCSyncDriveFormData(List<String> names, List<String> data) {
		this.names = names;
		this.data = data;
	}

	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.names.size());
		buffer.writeInt(this.data.size());

		for (int i = 0; i < this.names.size(); i++) {
			String n = names.get(i);
			buffer.writeInt(n.length());
			buffer.writeString(n);
		}

		for (int i = 0; i < this.data.size(); i++) {
			String d = data.get(i);
			buffer.writeInt(d.length());
			buffer.writeString(d);
		}
	}

	public static SCSyncDriveFormData decode(PacketBuffer buffer) {
		SCSyncDriveFormData msg = new SCSyncDriveFormData();
		int nLen = buffer.readInt();
		int dLen = buffer.readInt();

		for (int i = 0; i < nLen; i++) {
			int l = buffer.readInt();
			msg.names.add(buffer.readString(l));
		}

		for (int i = 0; i < dLen; i++) {
			int l = buffer.readInt();
			msg.data.add(buffer.readString(l));
		}

		return msg;
	}

	public static void handle(final SCSyncDriveFormData message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = KingdomKeys.proxy.getClientPlayer();

			for (int i = 0; i < message.names.size(); i++) {
				DriveForm driveform = ModDriveForms.registry.getValue(new ResourceLocation(message.names.get(i)));
				String d = message.data.get(i);
				BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(d.getBytes())));

				DriveFormData result;
				try {
					result = GSON_BUILDER.fromJson(br, DriveFormData.class);

				} catch (JsonParseException e) {
					KingdomKeys.LOGGER.error("Error parsing json file {}: {}", message.names.get(i), e);
					continue;
				}
				driveform.setDriveFormData(result);
				IOUtils.closeQuietly(br);
			}
		});
		ctx.get().setPacketHandled(true);
	}
}