package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.entity.GummiShipEntity;
import online.kingdomkeys.kingdomkeys.entity.GummiShotEntity;
import online.kingdomkeys.kingdomkeys.entity.block.GummiHangarTileEntity;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.lib.LineDisplay;
import online.kingdomkeys.kingdomkeys.util.Utils;

import javax.annotation.Nullable;

public class GummiWeaponBlock extends GummiBlockEdge {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    int firepower, fuelPerShot;
    SHOT_TYPE shotType;

    public enum SHOT_TYPE {
        FIRE, FIRA, FIRAGA, BLIZZARD, BLIZZARA, BLIZZAGA, GRAVITY, GRAVIRA, GRAVIGA;

        SHOT_TYPE getTextureName(){
            return switch (this) {
                case FIRE, FIRA, FIRAGA -> FIRE;
                case BLIZZARD, BLIZZARA, BLIZZAGA -> BLIZZARD;
                case GRAVITY, GRAVIRA, GRAVIGA -> GRAVITY;
            };
        }
    }

    public GummiWeaponBlock(Properties properties, SHOT_TYPE shotType, int weight, int armour, int firepower, int fuelPerShot) {
        super(properties, weight, armour, null, null);
        this.firepower = firepower;
        this.shotType = shotType;
        this.fuelPerShot = fuelPerShot;
    }

    public int getFirepower() {
        return firepower;
    }

    public int getFuelPerShot(){
        return fuelPerShot;
    }

    public void castShot(Player player, Level level, GummiShipEntity ship, float dmg, Vec3 pos, float xOff, float yOff, float speed) {
        if (player == null){
            castShot(level, xOff, yOff, getFirepower(), pos, speed/2F);
        } else {
            GummiShotEntity shot = new GummiShotEntity(level, player, shotType.getTextureName().name().toLowerCase(), dmg);
            level.addFreshEntity(shot);
            shot.setPos(pos);
            shot.shootFromRotation(ship, player.getXRot() + xOff, player.getYRot() + yOff, 0, speed, 0);
        }
    }

    public void castShot(Level level, float xRot, float yRot, float dmg, Vec3 pos, float speed){
        BlockState state = level.getBlockState(new BlockPos((int) pos.x(), (int) pos.y(), (int) pos.z()));
        Direction rotation = state.getValue(FACING);
        //Get yRot based on direction
        int dir = switch (rotation){
            case DOWN, UP, SOUTH -> 180;
            case NORTH -> 0;
            case WEST -> 270;
            case EAST -> 90;
        };

        GummiShotEntity shot = new GummiShotEntity(level, shotType.getTextureName().name().toLowerCase(), dmg);
        level.addFreshEntity(shot);
        shot.setPos(pos.add(0.5F,0.5F,0.5F));
        this.shootFromRotation(shot, xRot, yRot+dir, 0, speed, 0);
    }

    public void shootFromRotation(GummiShotEntity shot, float x, float y, float z, float velocity, float inaccuracy) {
        float f = -Mth.sin(y * ((float)Math.PI / 180F)) * Mth.cos(x * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((x + z) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(y * ((float)Math.PI / 180F)) * Mth.cos(x * ((float)Math.PI / 180F));
        shot.shoot(f, f1, f2, velocity, inaccuracy);
    }

    public void shoot(Player player, Level level, GummiShipEntity ship, Vec3 finalPos){
        float speed = 4F;
        switch(shotType){
            case FIRE, FIRA, FIRAGA-> {
                castShot(player, level, ship, getFirepower(), finalPos,0,0, speed);
                level.playSound(null, new BlockPos((int) finalPos.x(), (int) finalPos.y(), (int) finalPos.z()), ModSounds.laser.get(), SoundSource.PLAYERS, 4F, 1F);
            }
            case BLIZZARD -> {
                castShot(player, level,ship, getFirepower(), finalPos,0,-3, speed);
                castShot(player, level,ship, getFirepower(), finalPos,0,+3, speed);
                level.playSound(null, new BlockPos((int) finalPos.x(), (int) finalPos.y(), (int) finalPos.z()), ModSounds.laser.get(), SoundSource.PLAYERS, 4F, 1F);
            }
            case BLIZZARA -> {
                castShot(player, level,ship, getFirepower(), finalPos,-3,0, speed);
                castShot(player, level,ship, getFirepower(), finalPos,3,3, speed);
                castShot(player, level,ship, getFirepower(), finalPos,3,-3, speed);
                level.playSound(null, new BlockPos((int) finalPos.x(), (int) finalPos.y(), (int) finalPos.z()), ModSounds.laser.get(), SoundSource.PLAYERS, 4F, 1F);
            }
            case BLIZZAGA -> {
                castShot(player, level,ship, getFirepower(), finalPos,-6,0, speed);
                castShot(player, level,ship, getFirepower(), finalPos,6,0, speed);
                castShot(player, level,ship, getFirepower(), finalPos,0,-6, speed);
                castShot(player, level,ship, getFirepower(), finalPos,0,6, speed);
                level.playSound(null, new BlockPos((int) finalPos.x(), (int) finalPos.y(), (int) finalPos.z()), ModSounds.laser.get(), SoundSource.PLAYERS, 4F, 1F);
            }
            case GRAVITY, GRAVIRA, GRAVIGA-> {
                castShot(player, level, ship, getFirepower(), finalPos,0,0, 0.75F);
                level.playSound(null, new BlockPos((int) finalPos.x(), (int) finalPos.y(), (int) finalPos.z()), ModSounds.laser.get(), SoundSource.PLAYERS, 4F, 1F);
            }

        }
        if(ship != null)
            ship.remFuel(getFuelPerShot());
    }

    /*
    * Overrinding this so it doesn't conflict with the dye method from super
    * */
    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection()).setValue(ACTIVE,false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE);
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn,pos,state,placer,stack);

        if (!worldIn.isClientSide){
           // if(worldIn.getBlockEntity(pos) != null) {
                worldIn.setBlockAndUpdate(pos, state.setValue(ACTIVE, worldIn.hasNeighborSignal(pos)));

            //}
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean b) {
        super.neighborChanged(state,worldIn,pos,blockIn,fromPos,b);
        boolean powered = worldIn.hasNeighborSignal(pos);
        boolean oldPowered = state.getValue(ACTIVE);
        worldIn.setBlockAndUpdate(pos, state.setValue(ACTIVE, powered));
        if(!worldIn.isClientSide() && !oldPowered && powered){
            System.out.println(powered);
            shoot(null, worldIn, null, new Vec3(pos.getX(), pos.getY(), pos.getZ()));
        }
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean b) {
        super.onPlace(state,worldIn,pos,oldState,b);

        if (oldState.getBlock() != state.getBlock()) {
            worldIn.setBlockAndUpdate(pos, state.setValue(ACTIVE, worldIn.hasNeighborSignal(pos)));
        }
    }

    @Override
    public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
        return true;
    }

}
