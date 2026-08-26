package online.kingdomkeys.kingdomkeys.world.dimension.castle_oblivion.system.registry;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtException;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCSyncJsonRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class JsonRegistry<T extends JsonRegistryObject> extends SimpleJsonResourceReloadListener {

    private final ResourceLocation registryName;
    private Map<ResourceLocation, T> registry;
    private final T emptyValue;
    private final Codec<T> codec;

    private HolderLookup.Provider registries;

    public JsonRegistry(ResourceLocation registryName, String folder, Codec<T> codec, T emptyValue) {
        super(new GsonBuilder().setPrettyPrinting().create(), folder);
        this.registryName = registryName;
        this.registry = new HashMap<>();
        this.emptyValue = emptyValue;
        this.codec = codec;
    }

    public void setRegistries(HolderLookup.Provider registries) {
        if (this.registries == null) {
            this.registries = registries;
        }
    }

    public JsonRegistry(ResourceLocation registryName, String folder, Codec<T> codec) {
        this(registryName, folder, codec, null);
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
            T result = codec.parse(RegistryOps.create(JsonOps.INSTANCE, registries), jsonElement).getPartialOrThrow(JsonParseException::new);
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

    public CompoundTag serializeNBT(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        registry.forEach((key, value) -> {
            codec.encodeStart(RegistryOps.create(NbtOps.INSTANCE, registries), value).resultOrPartial(KingdomKeys.LOGGER::error).ifPresent(encoded -> {
                tag.put(key.toString(), encoded);
            });
        });
        return tag;
    }

    public void deserializeNBT(CompoundTag tag, HolderLookup.Provider registries) {
        tag.getAllKeys().forEach(key -> {
            ResourceLocation rl = KingdomKeys.rl(key);
            T value = codec.parse(RegistryOps.create(NbtOps.INSTANCE, registries), tag.getCompound(key)).getPartialOrThrow(NbtException::new);
            value.registryName = rl;
            registry.put(rl, value);
        });
    }
}
