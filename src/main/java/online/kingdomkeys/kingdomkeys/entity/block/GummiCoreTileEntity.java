package online.kingdomkeys.kingdomkeys.entity.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
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
    }

    public void loadToShip(GummiShipEntity ship){
        ship.setDamage(this.damage);
        ship.setFuel(this.fuel);
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
