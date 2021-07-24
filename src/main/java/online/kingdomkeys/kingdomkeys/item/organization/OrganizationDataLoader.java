package online.kingdomkeys.kingdomkeys.item.organization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncOrganizationData;

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
        
        for (ResourceLocation file : manager.listResources(folder, n -> n.endsWith(extension))) { //Get all .json files
            ResourceLocation organizationDataID = new ResourceLocation(file.getNamespace(), file.getPath().substring(folder.length() + 1, file.getPath().length() - extension.length()));
            IOrgWeapon weapon = (IOrgWeapon) ForgeRegistries.ITEMS.getValue(organizationDataID);
            try {
            	BufferedReader br = new BufferedReader(new InputStreamReader(manager.getResource(file).getInputStream()));
            	BufferedReader br2 = new BufferedReader(new InputStreamReader(manager.getResource(file).getInputStream()));
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
                    KingdomKeys.LOGGER.error("Error parsing json file {}: {}", manager.getResource(file).getLocation().toString(), e);
                    continue;
                }
                weapon.setOrganizationData(result);
                IOUtils.closeQuietly(manager.getResource(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
