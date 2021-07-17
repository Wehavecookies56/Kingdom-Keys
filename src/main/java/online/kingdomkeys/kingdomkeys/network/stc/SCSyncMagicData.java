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
import online.kingdomkeys.kingdomkeys.magic.Magic;
import online.kingdomkeys.kingdomkeys.magic.MagicData;
import online.kingdomkeys.kingdomkeys.magic.MagicDataDeserializer;
import online.kingdomkeys.kingdomkeys.magic.ModMagic;

public class SCSyncMagicData {

	public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(MagicData.class, new MagicDataDeserializer()).setPrettyPrinting().create();

	public SCSyncMagicData() {
	}

	List<String> names = new LinkedList<String>();
	List<String> data = new LinkedList<String>();

	public SCSyncMagicData(List<String> names, List<String> data) {
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

	public static SCSyncMagicData decode(PacketBuffer buffer) {
		SCSyncMagicData msg = new SCSyncMagicData();
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

	public static void handle(final SCSyncMagicData message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = KingdomKeys.proxy.getClientPlayer();

			for (int i = 0; i < message.names.size(); i++) {
				Magic magic = ModMagic.registry.getValue(new ResourceLocation(message.names.get(i)));
				String d = message.data.get(i);
				BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(d.getBytes())));

				MagicData result;
				try {
					result = GSON_BUILDER.fromJson(br, MagicData.class);

				} catch (JsonParseException e) {
					KingdomKeys.LOGGER.error("Error parsing json file {}: {}", message.names.get(i), e);
					continue;
				}
				magic.setMagicData(result);
				IOUtils.closeQuietly(br);
			}
		});
		ctx.get().setPacketHandled(true);
	}
}