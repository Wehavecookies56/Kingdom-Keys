package online.kingdomkeys.kingdomkeys.item.organization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class OrganizationDataLoader {

    //GSON builder with custom deserializer for keyblade data
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(OrganizationData.class, new OrganizationDataDeserializer()).setPrettyPrinting().create();

    /**
     * Method searches the keyblades folder in the datapack for all json files inside it.
     * Loaded data is assigned to the keyblade with the same name as the json file
     * @param manager Resource manager from the server
     */
    public static void loadData(IResourceManager manager) {
        String folder = "organization";
        String extension = ".json";
        for (ResourceLocation file : manager.getAllResourceLocations(folder, n -> n.endsWith(extension))) {
            ResourceLocation organizationDataID = new ResourceLocation(file.getNamespace(), file.getPath().substring(folder.length() + 1, file.getPath().length() - extension.length()));
            KingdomKeys.LOGGER.info("Found organization data file {}, ID {}", file, organizationDataID);
            IOrgWeapon weapon = (IOrgWeapon) ForgeRegistries.ITEMS.getValue(organizationDataID);
            try {
                for (IResource resource : manager.getAllResources(file)) {
                    OrganizationData result;
                    try {
                        result = GSON_BUILDER.fromJson(new BufferedReader(new InputStreamReader(resource.getInputStream())), OrganizationData.class);
                    } catch (JsonParseException e) {
                        KingdomKeys.LOGGER.error("Error parsing json file {}: {}", resource.getLocation().toString(), e);
                        continue;
                    }
                    weapon.setOrganizationData(result);
                    IOUtils.closeQuietly(resource);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
