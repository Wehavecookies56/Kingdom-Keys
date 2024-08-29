package online.kingdomkeys.kingdomkeys.driveform;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.neoforged.neoforge.server.ServerLifecycleHooks;
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
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncDriveFormData;

public class DriveFormDataLoader extends SimpleJsonResourceReloadListener {

    //GSON builder with custom deserializer for keyblade data
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(DriveFormData.class, new DriveFormDataDeserializer()).setPrettyPrinting().create();

    
    public DriveFormDataLoader() {
        super(GSON_BUILDER, "driveforms");
    }

    public static List<String> names = new LinkedList<>();
    public static List<String> dataList = new LinkedList<>();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, ResourceManager resourceManagerIn, ProfilerFiller profilerIn) {
        KingdomKeys.LOGGER.info("Loading driveforms data");
        loadData(resourceManagerIn);
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                PacketHandler.sendTo(new SCSyncDriveFormData(names,dataList), player);
            }
        }
    }

    /**
     * Method searches the keyblades folder in the datapack for all json files inside it.
     * Loaded data is assigned to the keyblade with the same name as the json file
     * @param manager Resource manager from the server
     */
    public void loadData(ResourceManager manager) {
        String folder = "driveforms";
        String extension = ".json";

        names.clear();
        dataList.clear();

        for (ResourceLocation file : manager.listResources(folder, n -> n.toString().endsWith(extension)).keySet()) { //Get all .json files
            ResourceLocation driveFormName = ResourceLocation.fromNamespaceAndPath(file.getNamespace(), file.getPath().substring(folder.length() + 1, file.getPath().length() - extension.length()));
			DriveForm driveform = ModDriveForms.registry.get(driveFormName);
            try {
            	BufferedReader br = manager.getResource(file).get().openAsReader();
            	BufferedReader br2 = manager.getResource(file).get().openAsReader();
            	String data = "";
            	while(br.ready()) {
            		data += br.readLine();
            	}
            	dataList.add(data);
            	DriveFormData result;
                try {
                    result = GSON_BUILDER.fromJson(br2, DriveFormData.class);
                    names.add(driveFormName.toString());
                } catch (JsonParseException e) {
                    KingdomKeys.LOGGER.error("Error parsing driveform json file {}: {}", manager.getResource(file).get().sourcePackId(), e);
                    continue;
                }
                driveform.setDriveFormData(result);
                IOUtils.closeQuietly(br);
                IOUtils.closeQuietly(br2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

