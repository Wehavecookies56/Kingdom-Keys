package online.kingdomkeys.kingdomkeys.network.stc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.client.ClientUtils;
import online.kingdomkeys.kingdomkeys.magic.MagicData;
import online.kingdomkeys.kingdomkeys.magic.MagicDataDeserializer;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class SCSyncMagicData {

	public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(MagicData.class, new MagicDataDeserializer()).setPrettyPrinting().create();

	public SCSyncMagicData() {
	}

	public List<String> names = new LinkedList<String>();
	public List<String> data = new LinkedList<String>();

	public SCSyncMagicData(List<String> names, List<String> data) {
		this.names = names;
		this.data = data;
	}

	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.names.size());
		buffer.writeInt(this.data.size());

		for (int i = 0; i < this.names.size(); i++) {
			String n = names.get(i);
			buffer.writeInt(n.length());
			buffer.writeUtf(n);
		}

		for (int i = 0; i < this.data.size(); i++) {
			String d = data.get(i);
			buffer.writeInt(d.length());
			buffer.writeUtf(d);
		}
	}

	public static SCSyncMagicData decode(FriendlyByteBuf buffer) {
		SCSyncMagicData msg = new SCSyncMagicData();
		int nLen = buffer.readInt();
		int dLen = buffer.readInt();

		for (int i = 0; i < nLen; i++) {
			int l = buffer.readInt();
			msg.names.add(buffer.readUtf(l));
		}

		for (int i = 0; i < dLen; i++) {
			int l = buffer.readInt();
			msg.data.add(buffer.readUtf(l));
		}

		return msg;
	}

	public static void handle(final SCSyncMagicData message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientUtils.syncMagicData(message)));
		ctx.get().setPacketHandled(true);
	}
}