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

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationData;
import online.kingdomkeys.kingdomkeys.item.organization.OrganizationDataDeserializer;

public class SCSyncOrganizationData {
	
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(OrganizationData.class, new OrganizationDataDeserializer()).setPrettyPrinting().create();

	public SCSyncOrganizationData() {
	}

	List<String> names = new LinkedList<String>();
	List<String> data = new LinkedList<String>();
	
	
	public SCSyncOrganizationData(List<String> names, List<String> data) {
		this.names = names;
		this.data = data;
	}
	
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeInt(this.names.size());
		buffer.writeInt(this.data.size());
		
		for(int i = 0; i < this.names.size();i++) {
			String n = names.get(i);
			buffer.writeInt(n.length());
			buffer.writeUtf(n);
		}
		
		for(int i = 0; i < this.data.size();i++) {
			String d = data.get(i);
			buffer.writeInt(d.length());
			buffer.writeUtf(d);
		}
	}

	public static SCSyncOrganizationData decode(FriendlyByteBuf buffer) {
		SCSyncOrganizationData msg = new SCSyncOrganizationData();
		int nLen = buffer.readInt();
		int dLen = buffer.readInt();
		
		for(int i=0;i<nLen;i++) {
			int l = buffer.readInt();
			msg.names.add(buffer.readUtf(l));
		}

		for(int i=0;i<dLen;i++) {
			int l = buffer.readInt();
			msg.data.add(buffer.readUtf(l));
		}
		
		return msg;	
	}

	public static void handle(final SCSyncOrganizationData message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			Player player = KingdomKeys.proxy.getClientPlayer();
			
			for(int i = 0;i<message.names.size();i++) {
	            IOrgWeapon weapon = (IOrgWeapon) ForgeRegistries.ITEMS.getValue(new ResourceLocation(message.names.get(i)));

				String d = message.data.get(i);
				BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(d.getBytes())));
				
				OrganizationData result;
				try {
				    result = GSON_BUILDER.fromJson(br, OrganizationData.class);
				   
				} catch (JsonParseException e) {
				    KingdomKeys.LOGGER.error("Error parsing json file {}: {}", message.names.get(i), e);
				    continue;
				}
				weapon.setOrganizationData(result);
				IOUtils.closeQuietly(br);
			}
		});
		ctx.get().setPacketHandled(true);
	}

}
