package online.kingdomkeys.kingdomkeys.item.organization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncOrganizationData;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OrganizationDataLoader extends SimpleJsonResourceReloadListener {

    //GSON builder with custom deserializer for keyblade data
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(OrganizationData.class, new OrganizationDataDeserializer()).setPrettyPrinting().create();

    /**
     * Method searches the keyblades folder in the datapack for all json files inside it.
     * Loaded data is assigned to the keyblade with the same name as the json file
     * @param manager Resource manager from the server
     */
    
    public static List<String> names = new LinkedList<>();
    public static List<String> dataList = new LinkedList<>();

    public OrganizationDataLoader() {
        super(GSON_BUILDER, "organization");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        KingdomKeys.LOGGER.info("Loading organization data");
        loadData(resourceManagerIn);
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                PacketHandler.sendTo(new SCSyncOrganizationData(OrganizationDataLoader.names, OrganizationDataLoader.dataList), player);
            }
        }
    }

    public void loadData(ResourceManager manager) {
        String folder = "organization";
        String extension = ".json";
        
        names.clear();
        dataList.clear();
        
        System.out.print("Loading Organization weapons: ");
        for (ResourceLocation file : manager.listResources(folder, n -> n.toString().endsWith(extension)).keySet()) { //Get all .json files
        	System.out.print(file.getNamespace()+":"+file.getPath()+" ");
            ResourceLocation organizationDataID = new ResourceLocation(file.getNamespace(), file.getPath().substring(folder.length() + 1, file.getPath().length() - extension.length()));
            try {
                IOrgWeapon weapon = (IOrgWeapon) ForgeRegistries.ITEMS.getValue(organizationDataID);
                BufferedReader br = manager.getResource(file).get().openAsReader();
            	BufferedReader br2 = manager.getResource(file).get().openAsReader();
            	String data = "";
            	while(br.ready()) {
            		data += br.readLine();
            	}
            	dataList.add(data);
            	OrganizationData result;
                try {
                    result = GSON_BUILDER.fromJson(br2, OrganizationData.class);
                    names.add(organizationDataID.toString());
                   
                } catch (JsonParseException e) {
                    KingdomKeys.LOGGER.error("Error parsing json file {}: {}", manager.getResource(file).get().sourcePackId().toString(), e);
                    continue;
                }
                weapon.setOrganizationData(result);
                IOUtils.closeQuietly(br);
                IOUtils.closeQuietly(br2);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                KingdomKeys.LOGGER.warn("Found Organization weapon data: {} for item that does not exist, ignoring.");
            }
        }
        System.out.println("");

    }
}
