package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class KKSupplier<T extends KKRegistryObject> implements Supplier<T> {

    final Supplier<T> supplier;
    final ResourceLocation location;

    public KKSupplier(ResourceLocation location, Supplier<T> supplier) {
        this.location = location;
        this.supplier = supplier;
    }

    @Override
    public T get() {
        return supplier.get();
    }

    public ResourceLocation location() {
        return location;
    }
}
