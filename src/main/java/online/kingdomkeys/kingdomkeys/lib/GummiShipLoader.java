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
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class GummiShipLoader extends SimplePreparableReloadListener<Map<ResourceLocation, CompoundTag>> {
	private static final String FOLDER = "gummi_ships";
	private static final String EXTENSION = ".nbt";

	private static final long SIZE_LIMIT = 8 * 1024 * 1024;

	private static volatile Map<ResourceLocation, CompoundTag> SHIPS = Map.of();

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
		SHIPS = Collections.unmodifiableMap(found);
		KingdomKeys.LOGGER.info("Loaded {} gummi ships", found.size());
	}

	// Strips the folder and the extension, so gummi_ships/highwind.nbt --> kingdomkeys:highwind
	private static ResourceLocation nameOf(ResourceLocation path) {
		String name = path.getPath().substring(FOLDER.length() + 1, path.getPath().length() - EXTENSION.length());
		return ResourceLocation.fromNamespaceAndPath(path.getNamespace(), name);
	}

	@Nullable
	public static GummiStructure get(ResourceLocation id, HolderLookup.Provider registries) {
		CompoundTag tag = all().get(id);
		return tag == null ? null : new GummiStructure(registries, tag.copy());
	}

	public static Set<ResourceLocation> names() {
		return all().keySet();
	}

	// Datapacks are run server sided, therefore a client doesn't knwo the files throught he loaded values
	// So it scans the files inside the JAR file directly
	private static Map<ResourceLocation, CompoundTag> all() {
		if (!SHIPS.isEmpty()) {
			return SHIPS;
		}

		if (bundled == null) {
			Map<ResourceLocation, CompoundTag> found = new LinkedHashMap<>();
			String inside = "/data/" + KingdomKeys.MODID + "/" + FOLDER;

			try {
				URL url = GummiShipLoader.class.getResource(inside);

				if (url == null) {
					return found;
				}

				URI uri = url.toURI();
				Path dir;

				try {
					dir = Paths.get(uri);
				} catch (FileSystemNotFoundException notOpen) {
					FileSystems.newFileSystem(uri, Map.of());
					dir = Paths.get(uri);
				}

				try (Stream<Path> files = Files.list(dir)) {
					for (Path file : files.filter(path -> path.getFileName().toString().endsWith(EXTENSION)).toList()) {
						String name = file.getFileName().toString();
						name = name.substring(0, name.length() - EXTENSION.length());

						try (InputStream in = Files.newInputStream(file)) {
							found.put(KingdomKeys.rl(name), NbtIo.readCompressed(in, NbtAccounter.create(SIZE_LIMIT)));
						}
					}
				}
			} catch (Exception failed) {
				KingdomKeys.LOGGER.error("Could not read the gummi ships bundled at " + inside, failed);
			}

			bundled = found;
		}

		return bundled;
	}

	private static Map<ResourceLocation, CompoundTag> bundled;
}
