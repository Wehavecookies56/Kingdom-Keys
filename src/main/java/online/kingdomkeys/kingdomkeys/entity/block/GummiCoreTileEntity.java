package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class GummiCoreTileEntity extends BlockEntity {
    private float damage;
    private int fuel;

    public GummiCoreTileEntity(BlockPos pos, BlockState blockState) {
        super(ModEntities.TYPE_GUMMI_CORE_TE.get(), pos, blockState);
    }

    public void saveFromShip(GummiShipEntity ship){
        this.damage = ship.getDamage();
        this.fuel = ship.getFuel();

        setChanged();
        syncToClients(true);
    }

    public void loadToShip(GummiShipEntity ship){
        ship.setDamage(this.damage);
        ship.setFuel(Math.min(this.fuel, ship.getMaxFuel()));
    }

    public float getDamage() {
        return damage;
    }

    public int getFuel() {
        return fuel;
    }

    public void setDamage(float damage) {
        this.damage = Math.max(0F, damage);
        setChanged();
        syncToClients(this.damage <= 0F);
    }

    public void setFuel(int fuel) {
        this.fuel = Math.max(0, fuel);
        setChanged();
        syncToClients(false);
    }

    private static final int SYNC_INTERVAL = 10;
    private long lastSync = Long.MIN_VALUE;

    public void syncToClients(boolean now) {
        if (level == null || level.isClientSide) {
            return;
        }
        long time = level.getGameTime();
        if (now || time - lastSync >= SYNC_INTERVAL) {
            lastSync = time;
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putFloat("damage", damage);
        tag.putInt("fuel", fuel);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.damage = tag.getFloat("damage");
        this.fuel = tag.getInt("fuel");
    }
}
