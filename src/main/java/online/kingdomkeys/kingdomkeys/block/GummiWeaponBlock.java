package online.kingdomkeys.kingdomkeys.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import online.kingdomkeys.kingdomkeys.lib.Quarter;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class GummiWeaponBlock extends GummiBlockEdge {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    int firepower, fuelPerShot;
    public ShotType shotType;
    public enum ShotType {
        FIRE, FIRA, FIRAGA, BLIZZARD, BLIZZARA, BLIZZAGA, GRAVITY, GRAVIRA, GRAVIGA, WATER, WATERA, WATERGA;

        public ShotType getRootType(){
            return switch (this) {
                case FIRE, FIRA, FIRAGA -> FIRE;
                case BLIZZARD, BLIZZARA, BLIZZAGA -> BLIZZARD;
                case GRAVITY, GRAVIRA, GRAVIGA -> GRAVITY;
                case WATER, WATERA, WATERGA -> WATERGA;
            };
        }
    }

    public GummiWeaponBlock(Properties properties, ShotType shotType, int weight, int armour, int firepower, int fuelPerShot) {
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

    public void castShot(Player player, Level level, float dmg, Vec3 pos, float xOff, float yOff, float speed, Vec3 direction) {
        if (player == null){
            castShotFromRedstone(level, xOff, yOff, getFirepower(), pos, speed/2F);
        } else {
            GummiShotEntity shot = new GummiShotEntity(level, player, shotType.getRootType().name().toLowerCase(), dmg);
            level.addFreshEntity(shot);
            shot.setPos(pos);
            shot.setDeltaMovement(direction.scale(speed));
        }
    }

    public void castShotFromRedstone(Level level, float xRot, float yRot, float dmg, Vec3 pos, float speed){
        BlockState state = level.getBlockState(new BlockPos((int) pos.x(), (int) pos.y(), (int) pos.z()));
        Quarter quarter = state.getValue(QUARTER);
        Direction rotation = state.getValue(FACING);

        float xOff = 0, zOff = 0;
        if(this instanceof GummiWeaponMultiBlock mb){
            xOff = mb.getOffsetToCannon()[0];
            zOff = mb.getOffsetToCannon()[2];
        }

        //Get yRot based on direction
        int dir = switch (rotation){
            case DOWN, UP, SOUTH -> {
                xOff *= -1;
                zOff *= -1;
                yield 180;
            }
            case NORTH -> 0;
            case WEST -> {
                xOff *= -1;
                zOff *= -1;
                yield 270;
            }
            case EAST -> 90;
        };

        if(quarter == Quarter.TOP){
            xOff *= -1;
            zOff *= -1;
        }

        GummiShotEntity shot = new GummiShotEntity(level, shotType.name().toLowerCase(), dmg);
        level.addFreshEntity(shot);

        shot.setPos(pos.add(0.5F+xOff,0.5F,0.5F+zOff));
        this.shootFromRotation(shot, xRot, yRot+dir, 0, speed, 0);
    }

    public void shootFromRotation(GummiShotEntity shot, float x, float y, float z, float velocity, float inaccuracy) {
        float f = -Mth.sin(y * ((float)Math.PI / 180F)) * Mth.cos(x * ((float)Math.PI / 180F));
        float f1 = -Mth.sin((x + z) * ((float)Math.PI / 180F));
        float f2 = Mth.cos(y * ((float)Math.PI / 180F)) * Mth.cos(x * ((float)Math.PI / 180F));
        shot.shoot(f, f1, f2, velocity, inaccuracy);
    }

    public void shoot(Player player, Level level, GummiShipEntity ship, Vec3 finalPos, Vec3 dir) {
        float speed = 4F;
        //Dir calculation if they are blocks so it is not null
        if (dir == null && finalPos != null && level != null) {
            BlockPos pos = BlockPos.containing(finalPos);
            BlockState state = level.getBlockState(pos);
            if (state.hasProperty(FACING)) {
                Direction facing = state.getValue(FACING);
                dir = Vec3.atLowerCornerOf(facing.getNormal()).normalize();
            } else {
                dir = new Vec3(0, 1, 0);
            }
        }

        switch (shotType) {
            case FIRE, FIRA, FIRAGA -> {
                castShot(player, level, getFirepower(), finalPos, 0, 0, speed, dir);
                level.playSound(null, BlockPos.containing(finalPos), ModSounds.wisdom_shot.get(), SoundSource.PLAYERS, 2.5F, 1F);
            }
            case BLIZZARD -> {
                castShot(player, level, getFirepower(), finalPos, 0, -3, speed, rotateDirection(dir, -3, 0));
                castShot(player, level, getFirepower(), finalPos, 0, +3, speed, rotateDirection(dir, +3, 0));
                level.playSound(null, BlockPos.containing(finalPos), ModSounds.wisdom_shot.get(), SoundSource.PLAYERS, 2.5F, 1F);
            }
            case BLIZZARA -> {
                castShot(player, level, getFirepower(), finalPos, -8, 0, speed, rotateDirection(dir, 0, +8));
                castShot(player, level, getFirepower(), finalPos, 0, 12, speed, rotateDirection(dir, -12, 0));
                castShot(player, level, getFirepower(), finalPos, 0, -12, speed, rotateDirection(dir, +12, 0));
                level.playSound(null, BlockPos.containing(finalPos), ModSounds.wisdom_shot.get(), SoundSource.PLAYERS, 2.5F, 1F);
            }
            case BLIZZAGA -> {
                castShot(player, level, getFirepower(), finalPos, 0, 8, speed, rotateDirection(dir, -8, 0));
                castShot(player, level, getFirepower(), finalPos, 0, -8, speed, rotateDirection(dir, +8, 0));
                castShot(player, level, getFirepower(), finalPos, 8, 0, speed, rotateDirection(dir, 0, -8));
                castShot(player, level, getFirepower(), finalPos, -8, 0, speed, rotateDirection(dir, 0, +8));
                level.playSound(null, BlockPos.containing(finalPos), ModSounds.wisdom_shot.get(), SoundSource.PLAYERS, 2.5F, 1F);
            }

            case GRAVITY, GRAVIRA, GRAVIGA -> {
                castShot(player, level, getFirepower(), finalPos, 0, 0, 0.75F, dir);
                level.playSound(null, new BlockPos((int) finalPos.x(), (int) finalPos.y(), (int) finalPos.z()), ModSounds.wisdom_shot.get(), SoundSource.PLAYERS, 2.5F, 0.9F);
            }
        }

        if (ship != null)
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
        return super.getStateForPlacement(context).setValue(ACTIVE,false);
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
            worldIn.setBlockAndUpdate(pos, state.setValue(ACTIVE, worldIn.hasNeighborSignal(pos)));
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block blockIn, BlockPos fromPos, boolean b) {
        super.neighborChanged(state,level,pos,blockIn,fromPos,b);
        boolean powered = level.hasNeighborSignal(pos);
        boolean oldPowered = state.getValue(ACTIVE);
        level.setBlockAndUpdate(pos, state.setValue(ACTIVE, powered));
        if(!level.isClientSide() && !oldPowered && powered){
            shoot(null, level, null, new Vec3(pos.getX(), pos.getY(), pos.getZ()), null);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean b) {
        super.onPlace(state,level,pos,oldState,b);

        if (oldState.getBlock() != state.getBlock()) {
            level.setBlockAndUpdate(pos, state.setValue(ACTIVE, level.hasNeighborSignal(pos)));
        }
    }

    @Override
    public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
        return true;
    }

    private Vec3 rotateDirection(Vec3 dir, double yawDeg, double pitchDeg) {
        if (dir == null)
            return Vec3.ZERO;

        Vector3f forward = new Vector3f((float)dir.x, (float)dir.y, (float)dir.z).normalize();
        Vector3f up = new Vector3f(0, 1, 0);
        Vector3f right = forward.cross(up, new Vector3f()).normalize();

        Quaternionf q = new Quaternionf();
        q.rotateAxis((float)Math.toRadians(yawDeg), up);
        q.rotateAxis((float)Math.toRadians(pitchDeg), right);
        q.transform(forward);
        return new Vec3(forward.x(), forward.y(), forward.z());
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }
}
