package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import com.google.gson.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.server.ServerLifecycleHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncJsonRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonRegistry<T extends JsonRegistryObject> extends SimpleJsonResourceReloadListener {

    private final ResourceLocation registryName;
    private Map<ResourceLocation, T> registry;
    private final String folder;
    private final T emptyValue;
    private final JsonDeserializer<T> deserializer;
    private final Class<T> clazz;

    public JsonRegistry(ResourceLocation registryName, String folder, JsonDeserializer<T> deserializer, Class<T> clazz, T emptyValue) {
        super(new GsonBuilder().registerTypeAdapter(clazz, deserializer).setPrettyPrinting().create(), folder);
        this.registryName = registryName;
        this.registry = new HashMap<>();
        this.folder = folder;
        this.emptyValue = emptyValue;
        this.deserializer = deserializer;
        this.clazz = clazz;
    }

    public JsonRegistry(ResourceLocation registryName, String folder, JsonDeserializer<T> deserializer, Class<T> clazz) {
        this(registryName, folder, deserializer, clazz, null);
    }

    private void register(ResourceLocation key, T value) {
        if (registry.containsKey(key)) {
            KingdomKeys.LOGGER.error("Tried to register existing object {} for json registry {}", key, registryName);
        } else {
            KingdomKeys.LOGGER.debug("{}: registered {}", registryName, key.toString());
            registry.put(key, value);
        }
    }

    public T getValue(ResourceLocation key) {
        if (containsKey(key)) {
            return registry.get(key);
        } else {
            return emptyValue;
        }
    }

    public Collection<T> getValues() {
        return registry.values();
    }

    public boolean containsKey(ResourceLocation key) {
        return registry.containsKey(key);
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        registry = new HashMap<>();
        AtomicInteger count = new AtomicInteger();
        pObject.forEach((resourceLocation, jsonElement) -> {
            T result = new GsonBuilder().registerTypeAdapter(clazz, deserializer).setPrettyPrinting().create().fromJson(jsonElement, clazz);
            result.registryName = resourceLocation;
            register(resourceLocation, result);
            count.incrementAndGet();
        });
        KingdomKeys.LOGGER.info("Loaded {} {} data", count.get(), registryName);
        if (ServerLifecycleHooks.getCurrentServer() != null) {
            for (ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
                PacketHandler.sendTo(new SCSyncJsonRegistry<>(this), player);
            }
        }
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        registry.forEach((key, value) -> {
            tag.put(key.toString(), value.serializeNBT());
        });
        return tag;
    }

    public void deserializeNBT(CompoundTag tag) {
        registry = new HashMap<>();
        tag.getAllKeys().forEach(key -> {
            ResourceLocation rl = new ResourceLocation(key);
            try {
                T value = clazz.getDeclaredConstructor(CompoundTag.class).newInstance(tag.getCompound(key));
                value.registryName = rl;
                registry.put(rl, value);
            } catch (NoSuchMethodException e) {
                KingdomKeys.LOGGER.error("Deserialization constructor is missing for type {}", clazz.toString());
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
