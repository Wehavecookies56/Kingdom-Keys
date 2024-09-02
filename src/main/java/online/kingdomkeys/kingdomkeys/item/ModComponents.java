package online.kingdomkeys.kingdomkeys.item;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class ModComponents {

    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(KingdomKeys.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SynthesisBagItem.BagLevel>> SYNTH_BAG_LEVEL = COMPONENTS.registerComponentType("synth_bag_level", builder -> builder.persistent(SynthesisBagItem.BagLevel.CODEC).networkSynchronized(SynthesisBagItem.BagLevel.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> INVENTORY = COMPONENTS.registerComponentType("inventory", builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<KeybladeItem.KeybladeID>> KEYBLADE_ID = COMPONENTS.registerComponentType("keyblade_id", builder -> builder.persistent(KeybladeItem.KeybladeID.CODEC).networkSynchronized(KeybladeItem.KeybladeID.STREAM_CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<KeybladeItem.KeybladeLevel>> KEYBLADE_LEVEL = COMPONENTS.registerComponentType("keyblade_level", builder -> builder.persistent(KeybladeItem.KeybladeLevel.CODEC).networkSynchronized(KeybladeItem.KeybladeLevel.STREAM_CODEC).cacheEncoding());
}
