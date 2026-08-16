package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class GummiShipLoader extends SimplePreparableReloadListener<Map<ResourceLocation, CompoundTag>> {
	private static final String FOLDER = "gummi_ships";
	private static final String EXTENSION = ".nbt";

	private static final long SIZE_LIMIT = 8 * 1024 * 1024;

	private static final Map<ResourceLocation, CompoundTag> SHIPS = new LinkedHashMap<>();

	@Override
	protected Map<ResourceLocation, CompoundTag> prepare(ResourceManager manager, ProfilerFiller profiler) {
		Map<ResourceLocation, CompoundTag> found = new LinkedHashMap<>();

		manager.listResources(FOLDER, path -> path.getPath().endsWith(EXTENSION)).forEach((path, resource) -> {
			ResourceLocation id = nameOf(path);

			try (InputStream in = resource.open()) {
				found.put(id, NbtIo.readCompressed(in, NbtAccounter.create(SIZE_LIMIT)));
			} catch (Exception failed) {
				KingdomKeys.LOGGER.error("Could not read the gummi ship at {}", path, failed);
			}
		});

		return found;
	}

	@Override
	protected void apply(Map<ResourceLocation, CompoundTag> found, ResourceManager manager, ProfilerFiller profiler) {
		SHIPS.clear();
		SHIPS.putAll(found);
		KingdomKeys.LOGGER.info("Loaded {} gummi ships", SHIPS.size());
	}

	// Strips the folder and the extension, so gummi_ships/highwind.nbt --> kingdomkeys:highwind
	private static ResourceLocation nameOf(ResourceLocation path) {
		String name = path.getPath().substring(FOLDER.length() + 1, path.getPath().length() - EXTENSION.length());
		return ResourceLocation.fromNamespaceAndPath(path.getNamespace(), name);
	}

	@Nullable
	public static GummiStructure get(ResourceLocation id, HolderLookup.Provider registries) {
		CompoundTag tag = SHIPS.get(id);
		return tag == null ? null : new GummiStructure(registries, tag.copy());
	}

	public static Set<ResourceLocation> names() {
		return SHIPS.keySet();
	}
}
