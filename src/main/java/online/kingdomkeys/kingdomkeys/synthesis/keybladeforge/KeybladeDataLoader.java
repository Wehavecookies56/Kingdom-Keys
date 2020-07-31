package online.kingdomkeys.kingdomkeys.synthesis.keybladeforge;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;

public class KeybladeDataLoader {

    //GSON builder with custom deserializer for keyblade data
    public static final Gson GSON_BUILDER = new GsonBuilder().registerTypeAdapter(KeybladeData.class, new KeybladeDataDeserializer()).setPrettyPrinting().create();
    /**
     * Method searches the keyblades folder in the datapack for all json files inside it.
     * Loaded data is assigned to the keyblade with the same name as the json file
     * @param manager Resource manager from the server
     */
    
    public static LinkedList<String> names = new LinkedList<String>();
    public static LinkedList<String> jsonData = new LinkedList<String>();
    
    public static void loadData(IResourceManager manager) {
        String folder = "keyblades";
        String extension = ".json";
        for (ResourceLocation file : manager.getAllResourceLocations(folder, n -> n.endsWith(extension))) {
        	
        	 BufferedReader isr;
			try {
				isr = new BufferedReader(new InputStreamReader(new FileInputStream(file.toString())));
				 String stuff = "";
	             
	             String line;
					while((line = isr.readLine()) != null) {
	             	//String line = isr.readLine();
	             	stuff += line;
	             }
	             jsonData.add(stuff);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        	
            ResourceLocation keybladeDataID = new ResourceLocation(file.getNamespace(), file.getPath().substring(folder.length() + 1, file.getPath().length() - extension.length()));
          //  KingdomKeys.LOGGER.info("Found keyblade data file {}, ID {}", file, keybladeDataID);
            KeybladeItem keyblade = (KeybladeItem) ForgeRegistries.ITEMS.getValue(keybladeDataID);
            try {
                for (IResource resource : manager.getAllResources(file)) {
                    KeybladeData result;
                    try {
                        result = GSON_BUILDER.fromJson(new BufferedReader(new InputStreamReader(resource.getInputStream())), KeybladeData.class);
                        names.add(keybladeDataID.getPath());
                       
                    } catch (JsonParseException e) {
                        KingdomKeys.LOGGER.error("Error parsing json file {}: {}", resource.getLocation().toString(), e);
                        continue;
                    }
                    keyblade.setKeybladeData(result);
                    IOUtils.closeQuietly(resource);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
