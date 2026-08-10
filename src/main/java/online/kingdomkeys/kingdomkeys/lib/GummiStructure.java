package online.kingdomkeys.kingdomkeys.lib;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.stream.IntStream;
import java.util.function.Function;
import net.minecraft.nbt.ListTag;
import com.mojang.datafixers.util.Either;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GummiStructure implements INBTSerializable<CompoundTag> {
    private UUID ownerID;
    private String shipName;
    private BlockState[][][] blocks;

    private CompoundTag[][][] blockEntities;
    private int width, height, depth;

    public String getName(){
        return shipName;
    }

    public void setName(String name){
        this.shipName = name;
    }

    public UUID getOwnerID(){
        return ownerID;
    }

    public String getOwnerIDString(){
        return ownerID.toString();
    }

    public BlockState[][][] getBlocks() {
        return blocks;
    }

    public void setBlocks(BlockState[][][] blocks) {
        this.blocks = blocks;
    }

    /** The saved contents of the block entity in a cell, or null where there is nothing to remember */
    public CompoundTag getBlockEntity(int x, int y, int z) {
        return blockEntities[x][y][z];
    }

    public void setBlockEntity(int x, int y, int z, CompoundTag data) {
        blockEntities[x][y][z] = data;
    }

    /**
     * The same ship with everything its blocks were holding left behind.
     *
     * <p>A blueprint is a design that gets stamped out again and again, so it must carry no contents: a
     * creative blueprint of a ship with a full chest would otherwise print those items every time it was
     * used. A ship picked up with the phone is the opposite case and keeps everything, because there is
     * only ever one of it.
     */
    public GummiStructure withoutBlockEntities() {
        GummiStructure copy = new GummiStructure(ownerID, shipName, width, height, depth);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                System.arraycopy(blocks[x][y], 0, copy.blocks[x][y], 0, depth);
            }
        }

        return copy;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    public void rotate(Rotation rotation){

    }

    /**
     * What a blueprint is written as. The old one listed a block state for every cell of the cube, air
     * included, which for a big hangar came to more than the two megabytes a chunk packet is allowed to
     * carry, so a client would be kicked the moment the hangar's chunk was sent. This names each kind of
     * piece once and gives every cell a number.
     */
    private static final Codec<GummiStructure> PALETTED = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("owner").forGetter(GummiStructure::getOwnerIDString),
            Codec.STRING.fieldOf("name").forGetter(GummiStructure::getName),
            Codec.INT.fieldOf("width").forGetter(GummiStructure::getWidth),
            Codec.INT.fieldOf("height").forGetter(GummiStructure::getHeight),
            Codec.INT.fieldOf("depth").forGetter(GummiStructure::getDepth),
            BlockState.CODEC.listOf().fieldOf("palette").forGetter(GummiStructure::palette),
            Codec.INT_STREAM.fieldOf("cells").forGetter(struct -> IntStream.of(struct.cells(struct.palette()))),
            // Keyed by cell number so only the handful of cells that have a block entity take up any room,
            // and optional so every blueprint written before this existed still reads
            Codec.unboundedMap(Codec.STRING, CompoundTag.CODEC).optionalFieldOf("block_entities", Map.of()).forGetter(GummiStructure::blockEntityMap)
    ).apply(instance, GummiStructure::new));

    private static final Codec<GummiStructure> LISTED = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("owner").forGetter(GummiStructure::getOwnerIDString),
            Codec.STRING.fieldOf("name").forGetter(GummiStructure::getName),
            Codec.INT.fieldOf("width").forGetter(GummiStructure::getWidth),
            Codec.INT.fieldOf("height").forGetter(GummiStructure::getHeight),
            Codec.INT.fieldOf("depth").forGetter(GummiStructure::getDepth),
            Codec.list(Codec.list(Codec.list(BlockState.CODEC))).fieldOf("blocks").forGetter(struct -> {
                List<List<List<BlockState>>> output = new ArrayList<>();
                for (int x = 0; x < struct.width; x++) {
                    output.add(new ArrayList<>());
                    for (int y = 0; y < struct.height; y++) {
                        output.get(x).add(new ArrayList<>());
                        for (int z = 0; z < struct.depth; z++) {
                            if (struct.blocks[x][y][z] == null) {
                                output.get(x).get(y).add(Blocks.AIR.defaultBlockState());
                            } else {
                                output.get(x).get(y).add(struct.blocks[x][y][z]);
                            }
                        }
                    }
                }
                return output;
            })
    ).apply(instance, GummiStructure::new)
    );

    /** Reads either shape and always writes the paletted one, so old blueprints keep working */
    public static final Codec<GummiStructure> CODEC = Codec.either(PALETTED, LISTED)
            .xmap(either -> either.map(Function.identity(), Function.identity()), Either::left);

    public static final StreamCodec<RegistryFriendlyByteBuf, GummiStructure> STREAM_CODEC = StreamCodec.of(
            (friendlyByteBuf, gummiStructure) ->
                friendlyByteBuf.writeNbt(gummiStructure.serializeNBT(friendlyByteBuf.registryAccess())),
            friendlyByteBuf ->
                new GummiStructure(friendlyByteBuf.registryAccess(), friendlyByteBuf.readNbt())
    );

    public boolean containsBlock (Block block){
        for (int z = 0; z < depth; ++z) {
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    BlockState state = getBlocks()[x][y][z];
                    if (state != null && !state.isAir()) {
                        if(state.getBlock() == block){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public int getBlockCount (Block block){
        int count = 0;
        for (int z = 0; z < depth; ++z) {
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    BlockState state = getBlocks()[x][y][z];
                    if (state != null && !state.isAir()) {
                        if(state.getBlock() == block){
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public GummiStructure(UUID ownerID, String name, int width, int height, int depth) {
        this.ownerID = ownerID;
        this.shipName = name;
        this.width = width;
        this.height = height;
        this.depth = depth;
        this.blocks = new BlockState[width][height][depth];
        this.blockEntities = new CompoundTag[width][height][depth];
    }

    private GummiStructure(UUID ownerID, String name, int width, int height, int depth, List<List<List<BlockState>>> blocks) {
        this(ownerID, name, width, height, depth);
        for (int x = 0; x < blocks.size(); x++) {
            for (int y = 0; y < blocks.get(x).size(); y++) {
                for (int z = 0; z < blocks.get(x).get(y).size(); z++) {
                    this.blocks[x][y][z] = blocks.get(x).get(y).get(z);
                }
            }
        }
    }

    private GummiStructure(String ownerID, String name, int width, int height, int depth, List<BlockState> palette, IntStream cells, Map<String, CompoundTag> blockEntities) {
        this(UUID.fromString(ownerID), name, width, height, depth);
        readCells(palette, cells.toArray());
        readBlockEntities(blockEntities);
    }

    public GummiStructure(HolderLookup.Provider provider, CompoundTag tag) {
        deserializeNBT(provider, tag);
    }

    public GummiStructure(UUID ownerID, String name, int width, int height, int depth, Level level, BlockPos pos) {
        this(ownerID, name, width, height, depth);
        BlockPos.MutableBlockPos mutableBlockPos = pos.mutable();
        for (int z = 0; z < depth; ++z) {
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    mutableBlockPos.move(x, y, z);
                    blocks[x][y][z] = level.getBlockState(mutableBlockPos);
                }
            }
        }
    }

    public GummiStructure(String ownerID, String name, int width, int height, int depth, List<List<List<BlockState>>> blocks) {
       this(UUID.fromString(ownerID),name,width,height,depth,blocks);
    }

    /**
     * The distinct states in the ship, in the order they are first met walking it. A ship is a few dozen
     * kinds of piece repeated hundreds of times, so naming each kind once and then pointing at it turns
     * what used to be one block state per cell into one number per cell.
     */
    private List<BlockState> palette() {
        List<BlockState> palette = new ArrayList<>();

        forEachCell((x, y, z) -> {
            BlockState state = blocks[x][y][z];

            if (state != null && !state.isAir() && !palette.contains(state)) {
                palette.add(state);
            }
        });

        return palette;
    }

    /** One entry per cell, indexing into {@link #palette()} one based, with nought for nothing there */
    private int[] cells(List<BlockState> palette) {
        int[] cells = new int[width * height * depth];
        int[] index = {0};

        forEachCell((x, y, z) -> {
            BlockState state = blocks[x][y][z];
            cells[index[0]++] = state == null || state.isAir() ? 0 : palette.indexOf(state) + 1;
        });

        return cells;
    }

    /** The saved block entities, keyed by the same cell number {@link #cells} uses, as text so it can be a map */
    private Map<String, CompoundTag> blockEntityMap() {
        Map<String, CompoundTag> saved = new LinkedHashMap<>();
        int[] index = {0};

        forEachCell((x, y, z) -> {
            CompoundTag data = blockEntities[x][y][z];

            if (data != null && !data.isEmpty()) {
                saved.put(Integer.toString(index[0]), data);
            }

            index[0]++;
        });

        return saved;
    }

    private void readBlockEntities(Map<String, CompoundTag> saved) {
        if (saved == null || saved.isEmpty()) {
            return;
        }

        int[] index = {0};

        forEachCell((x, y, z) -> {
            blockEntities[x][y][z] = saved.get(Integer.toString(index[0]));
            index[0]++;
        });
    }

    private void readCells(List<BlockState> palette, int[] cells) {
        int[] index = {0};

        forEachCell((x, y, z) -> {
            int id = index[0] < cells.length ? cells[index[0]] : 0;
            blocks[x][y][z] = id <= 0 || id > palette.size() ? null : palette.get(id - 1);
            index[0]++;
        });
    }

    /** Walked in one place so writing and reading can never disagree about the order */
    private void forEachCell(CellVisitor visitor) {
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < depth; ++z) {
                    visitor.visit(x, y, z);
                }
            }
        }
    }

    private interface CellVisitor {
        void visit(int x, int y, int z);
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("owner", ownerID);
        tag.putString("name", shipName);
        tag.putInt("width", width);
        tag.putInt("height", height);
        tag.putInt("depth", depth);

        List<BlockState> palette = palette();
        ListTag paletteTag = new ListTag();

        for (BlockState state : palette) {
            paletteTag.add(NbtUtils.writeBlockState(state));
        }

        tag.put("palette", paletteTag);
        tag.putIntArray("cells", cells(palette));

        Map<String, CompoundTag> saved = blockEntityMap();

        if (!saved.isEmpty()) {
            ListTag blockEntitiesTag = new ListTag();

            for (Map.Entry<String, CompoundTag> entry : saved.entrySet()) {
                CompoundTag cell = new CompoundTag();
                cell.putInt("cell", Integer.parseInt(entry.getKey()));
                cell.put("data", entry.getValue());
                blockEntitiesTag.add(cell);
            }

            tag.put("block_entities", blockEntitiesTag);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        ownerID = tag.getUUID("owner");
        shipName = tag.getString("name");
        width = tag.getInt("width");
        height = tag.getInt("height");
        depth = tag.getInt("depth");
        blocks = new BlockState[width][height][depth];
        blockEntities = new CompoundTag[width][height][depth];

        if (tag.contains("block_entities", Tag.TAG_LIST)) {
            Map<String, CompoundTag> saved = new LinkedHashMap<>();

            for (Tag entry : tag.getList("block_entities", Tag.TAG_COMPOUND)) {
                CompoundTag cell = (CompoundTag) entry;
                saved.put(Integer.toString(cell.getInt("cell")), cell.getCompound("data"));
            }

            readBlockEntities(saved);
        }

        if (tag.contains("cells", Tag.TAG_INT_ARRAY)) {
            List<BlockState> palette = new ArrayList<>();

            for (Tag entry : tag.getList("palette", Tag.TAG_COMPOUND)) {
                palette.add(NbtUtils.readBlockState(provider.lookupOrThrow(Registries.BLOCK), (CompoundTag) entry));
            }

            readCells(palette, tag.getIntArray("cells"));
            return;
        }

        // Ships written before the palette: one compound per cell, keyed by its index
        int index = 0;
        for (int z = 0; z < depth; ++z) {
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    String key = "block_" + index;
                    if (tag.contains(key, Tag.TAG_COMPOUND)) {
                        blocks[x][y][z] = NbtUtils.readBlockState(provider.lookupOrThrow(Registries.BLOCK), tag.getCompound(key));
                    } else {
                        blocks[x][y][z] = null;
                    }
                    index++;
                }
            }
        }
    }

    @Override
    public int hashCode() {
        return 31 * getOwnerID().hashCode() + getName().hashCode() + Integer.hashCode(width) + Integer.hashCode(height) + Integer.hashCode(depth) + Arrays.deepHashCode(blocks);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GummiStructure gummiStructure) {
            if (gummiStructure.getName().equals(this.shipName) && gummiStructure.getOwnerID().equals(this.ownerID) && gummiStructure.width == this.width && gummiStructure.height == this.height && gummiStructure.depth == this.depth) {
                for (int z = 0; z < depth; ++z) {
                    for (int y = 0; y < height; ++y) {
                        for (int x = 0; x < width; ++x) {
                            if (blocks[x][y][z] != null && gummiStructure.blocks[x][y][z] != null) {
                                if (!blocks[x][y][z].equals(gummiStructure.blocks[x][y][z])) {
                                    return false;
                                }
                            }
                            else if (blocks[x][y][z] == null && gummiStructure.blocks[x][y][z] != null) {
                                return false;
                            }
                            else if (blocks[x][y][z] != null && gummiStructure.blocks[x][y][z] == null) {
                                return false;
                            }
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


}