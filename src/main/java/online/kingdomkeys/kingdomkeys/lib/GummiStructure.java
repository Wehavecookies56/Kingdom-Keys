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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GummiStructure implements INBTSerializable<CompoundTag> {
    private UUID ownerID;
    private String shipName;
    private BlockState[][][] blocks;
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

    public static final Codec<GummiStructure> CODEC = RecordCodecBuilder.create(instance -> instance.group(
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

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("owner", ownerID);
        tag.putString("name", shipName);
        tag.putInt("width", width);
        tag.putInt("height", height);
        tag.putInt("depth", depth);

        int index = 0;
        for (int z = 0; z < depth; ++z) {
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    BlockState state = blocks[x][y][z];
                    // Skips air to avoid huge amount of useless data being stored
                    if (state != null && !state.isAir()) {
                        tag.put("block_" + index, NbtUtils.writeBlockState(state));
                    }
                    index++;
                }
            }
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