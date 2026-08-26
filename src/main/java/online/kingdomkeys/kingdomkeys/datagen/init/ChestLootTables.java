package online.kingdomkeys.kingdomkeys.datagen.init;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import online.kingdomkeys.kingdomkeys.lib.ModTags;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ChestLootTables implements LootTableSubProvider {

    private HolderLookup.Provider lookupProvider;

    Map<ResourceLocation, LootTable.Builder> lootTables;

    public ChestLootTables(HolderLookup.Provider lookupProvider) {
        this.lookupProvider = lookupProvider;
        lootTables = new HashMap<>();
    }

    public void build() {
        add(KingdomKeys.rl("castle_oblivion/standard_treasure"),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(TagEntry.expandTag(ModTags.MAGICS))));

        // Reward for getting 3 charades right in a row on a White Mushroom
        add(KingdomKeys.rl("entities/white_mushroom_reward"),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(ModItems.orichalcum.get()).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                                .add(LootItem.lootTableItem(ModItems.orichalcumplus.get()).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                                .add(LootItem.lootTableItem(ModItems.evanescent_crystal.get()).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                                .add(LootItem.lootTableItem(ModItems.illusory_crystal.get()).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                                .add(LootItem.lootTableItem(ModItems.manifest_illusion.get()).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3))))
                                .add(LootItem.lootTableItem(ModItems.lost_illusion.get()).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))));
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        build();
        lootTables.forEach((location, builder) -> consumer.accept(ResourceKey.create(Registries.LOOT_TABLE, location), builder));
    }

    public void add(ResourceLocation location, LootTable.Builder builder) {
        lootTables.put(location, builder);
    }
}
