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
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeData;
import online.kingdomkeys.kingdomkeys.synthesis.keybladeforge.KeybladeDataDeserializer;

public class SCSyncKeybladeData {
	
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(KeybladeData.class, new KeybladeDataDeserializer()).setPrettyPrinting().create();

	public SCSyncKeybladeData() {
	}

	List<String> names = new LinkedList<String>();
	List<String> data = new LinkedList<String>();
	
	
	public SCSyncKeybladeData(List<String> names, List<String> data) { //TODO add the 2 lists thing
		this.names = names;
		this.data = data;
	}
	
	public void encode(PacketBuffer buffer) {
		buffer.writeInt(this.names.size());
		buffer.writeInt(this.data.size());
		
		for(int i = 0; i < this.names.size();i++) {
			String n = names.get(i);
			buffer.writeInt(n.length());
			buffer.writeString(n);
		}
		
		for(int i = 0; i < this.data.size();i++) {
			String d = data.get(i);
			buffer.writeInt(d.length());
			buffer.writeString(d);
		}
	}

	public static SCSyncKeybladeData decode(PacketBuffer buffer) {
		SCSyncKeybladeData msg = new SCSyncKeybladeData();
		int nLen = buffer.readInt();
		int dLen = buffer.readInt();
		
		for(int i=0;i<nLen;i++) {
			int l = buffer.readInt();
			msg.names.add(buffer.readString(l));
		}

		for(int i=0;i<dLen;i++) {
			int l = buffer.readInt();
			msg.data.add(buffer.readString(l));
		}
		
		return msg;	
	}

	public static void handle(final SCSyncKeybladeData message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			PlayerEntity player = KingdomKeys.proxy.getClientPlayer();
			
			for(int i = 0;i<message.names.size();i++) {
				KeybladeItem keyblade = (KeybladeItem) ForgeRegistries.ITEMS.getValue(new ResourceLocation(message.names.get(i)));
				String d = message.data.get(i);
				BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(d.getBytes())));
				
				KeybladeData result;
				try {
				    result = GSON_BUILDER.fromJson(br, KeybladeData.class);
				   
				} catch (JsonParseException e) {
				    KingdomKeys.LOGGER.error("Error parsing json file {}: {}", message.names.get(i), e);
				    continue;
				}
				 keyblade.setKeybladeData(result);
	                if(result.keychain != null)
	                	result.keychain.setKeyblade(keyblade);
				IOUtils.closeQuietly(br);
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
