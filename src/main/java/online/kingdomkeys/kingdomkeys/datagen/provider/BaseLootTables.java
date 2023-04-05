package online.kingdomkeys.kingdomkeys.datagen.provider;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public abstract class BaseLootTables extends LootTableProvider {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    protected final Map<Block, LootTable.Builder> lootTables = new HashMap<>();
    private final DataGenerator generator;

    public BaseLootTables(PackOutput pOutput, Set<ResourceLocation> pRequiredTables, List<SubProviderEntry> pSubProviders, DataGenerator generator) {
        super(pOutput, pRequiredTables, pSubProviders);
        this.generator = generator;
    }

    protected abstract void addTables();

    protected LootTable.Builder createStandardTable(String name, Block block) {
        LootPool.Builder builder = LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block));
        return LootTable.lootTable().withPool(builder);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        addTables();

        Map<ResourceLocation, LootTable> tables = new HashMap<>();
        for (Map.Entry<Block, LootTable.Builder> entry : lootTables.entrySet()) {
            tables.put(entry.getKey().getLootTable(), entry.getValue().setParamSet(LootContextParamSets.BLOCK).build());
        }
        return writeTables(cache, tables);
    }

    private CompletableFuture<?> writeTables(CachedOutput cache, Map<ResourceLocation, LootTable> tables) {
        PackOutput.PathProvider pathProvider = this.generator.getPackOutput().createPathProvider(PackOutput.Target.DATA_PACK, "loot_tables");
        List<CompletableFuture<?>> list = new ArrayList<>();
        tables.forEach((key, lootTable) -> {
            Path path = pathProvider.json(key);
            list.add(DataProvider.saveStable(cache, LootTables.serialize(lootTable), path));
        });
        return CompletableFuture.allOf(list.toArray(CompletableFuture[]::new));
    }
}