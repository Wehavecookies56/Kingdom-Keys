package online.kingdomkeys.kingdomkeys.lib;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class KKSupplier<T extends KKRegistryObject> implements Supplier<T> {

    Supplier<T> supplier;

    public KKSupplier(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        return supplier.get();
    }

    public ResourceLocation location() {
        return get().getRegistryName();
    }
}
