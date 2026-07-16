package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TreasureChestTileEntity extends BlockEntity {

    EntityType<?> trapEntity;
    List<ItemStack> treasure = new ArrayList<>();

    public TreasureChestTileEntity(BlockPos pos, BlockState blockState) {
        super(ModEntities.TYPE_TREASURE_CHEST.get(), pos, blockState);
    }

    public boolean isTrapped() {
        return trapEntity != null;
    }

    public EntityType<?> getTrapEntity() {
        return trapEntity;
    }

    public List<ItemStack> getTreasure() {
        return treasure;
    }

    public void setTrapEntity(EntityType<?> trapEntity) {
        this.trapEntity = trapEntity;
    }

    public void setTreasure(List<ItemStack> treasure) {
        this.treasure = treasure;
    }

    @Override
    public void loadAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.loadAdditional(pTag, provider);

        this.treasure = ItemStack.CODEC.listOf().parse(NbtOps.INSTANCE, pTag.get("treasure")).getPartialOrThrow();
        if (pTag.contains("trap_entity")) {
            this.trapEntity = BuiltInRegistries.ENTITY_TYPE.byNameCodec().parse(NbtOps.INSTANCE, pTag.get("trap_entity")).getPartialOrThrow();
        }

    }

    @Override
    protected void saveAdditional(CompoundTag pTag, HolderLookup.Provider provider) {
        super.saveAdditional(pTag, provider);
        pTag.put("treasure", ItemStack.CODEC.listOf().encodeStart(NbtOps.INSTANCE, treasure).getPartialOrThrow());
        if (isTrapped()) {
            pTag.put("trap_entity", BuiltInRegistries.ENTITY_TYPE.byNameCodec().encodeStart(NbtOps.INSTANCE, trapEntity).getPartialOrThrow());
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider pRegistries) {
        return saveCustomOnly(pRegistries);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        this.loadAdditional(tag, lookupProvider);
    }

}
