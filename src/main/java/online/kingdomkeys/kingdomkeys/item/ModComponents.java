package online.kingdomkeys.kingdomkeys.item;

import com.mojang.serialization.Codec;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModComponents {

    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(KingdomKeys.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SYNTH_BAG_LEVEL = COMPONENTS.registerComponentType("synth_bag_level", builder -> builder.persistent(ExtraCodecs.intRange(0, 3)).networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> INVENTORY = COMPONENTS.registerComponentType("inventory", builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> KEYBLADE_ID = COMPONENTS.registerComponentType("keyblade_id", builder -> builder.persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> ARMOR_ID = COMPONENTS.registerComponentType("armor_id", builder -> builder.persistent(UUIDUtil.CODEC).networkSynchronized(UUIDUtil.STREAM_CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> KEYBLADE_LEVEL = COMPONENTS.registerComponentType("keyblade_level", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> HAS_FORTUNE_BONUS = COMPONENTS.registerComponentType("has_fortune_bonus", builder -> builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CARD_VALUE = COMPONENTS.registerComponentType("card_value", builder -> builder.persistent(ExtraCodecs.intRange(0, 9)).networkSynchronized(ByteBufCodecs.VAR_INT).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ARROWGUN_AMMO = COMPONENTS.registerComponentType("arrowgun_ammo", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<PauldronItem.PauldronEnchantments>> PAULDRON_ENCHANTMENTS = COMPONENTS.registerComponentType("pauldron_enchantments", builder -> builder.persistent(PauldronItem.PauldronEnchantments.CODEC).networkSynchronized(PauldronItem.PauldronEnchantments.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<RecipeItem.Recipes>> RECIPES = COMPONENTS.registerComponentType("recipes", builder -> builder.persistent(RecipeItem.Recipes.CODEC).networkSynchronized(RecipeItem.Recipes.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> WAYFINDER_COLOR = COMPONENTS.registerComponentType("wayfinder_color", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<WayfinderItem.WayfinderOwner>> WAYFINDER_OWNER = COMPONENTS.registerComponentType("wayfinder_owner", builder -> builder.persistent(WayfinderItem.WayfinderOwner.CODEC).networkSynchronized(WayfinderItem.WayfinderOwner.STREAM_CODEC).cacheEncoding());
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> SAVE_POINT_TIER = COMPONENTS.registerComponentType("save_point_tier", builder -> builder.persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8));
}
