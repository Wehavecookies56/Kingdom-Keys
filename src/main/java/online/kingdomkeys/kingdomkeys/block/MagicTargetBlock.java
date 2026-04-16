package online.kingdomkeys.kingdomkeys.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.magic.IMagicProjectile;

public class MagicTargetBlock extends Block implements INoDataGen {

    public static final MapCodec<MagicTargetBlock> CODEC = simpleCodec(MagicTargetBlock::new);
    private static final IntegerProperty OUTPUT_POWER;

    public MapCodec<MagicTargetBlock> codec() {
        return CODEC;
    }

    public MagicTargetBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((this.stateDefinition.any()).setValue(OUTPUT_POWER, 0));
    }

    @Override
    protected void onProjectileHit(Level level, BlockState state, BlockHitResult hit, Projectile projectile) {
        int i = updateRedstoneOutput(level, state, hit, projectile);
        Entity owner = projectile.getOwner();
        if (owner instanceof ServerPlayer serverplayer) {
            serverplayer.awardStat(Stats.TARGET_HIT);
            CriteriaTriggers.TARGET_BLOCK_HIT.trigger(serverplayer, projectile, hit.getLocation(), i);
        }
    }

    private static int updateRedstoneOutput(LevelAccessor level, BlockState state, BlockHitResult hit, Entity projectile) {
        int strength = projectile instanceof IMagicProjectile p ? p.getMagicRedstoneStrength() : 0;
        int ticks = projectile instanceof IMagicProjectile p ? 20 : 0;
        if (!level.getBlockTicks().hasScheduledTick(hit.getBlockPos(), state.getBlock())) {
            setOutputPower(level, state, strength, hit.getBlockPos(), ticks);
        }

        return strength;
    }

    private static int getRedstoneStrength(BlockHitResult hit, Vec3 hitLocation) {
        Direction direction = hit.getDirection();
        double d0 = Math.abs(Mth.frac(hitLocation.x) - (double) 0.5F);
        double d1 = Math.abs(Mth.frac(hitLocation.y) - (double) 0.5F);
        double d2 = Math.abs(Mth.frac(hitLocation.z) - (double) 0.5F);
        Direction.Axis direction$axis = direction.getAxis();
        double d3;
        if (direction$axis == Direction.Axis.Y) {
            d3 = Math.max(d0, d2);
        } else if (direction$axis == Direction.Axis.Z) {
            d3 = Math.max(d0, d1);
        } else {
            d3 = Math.max(d1, d2);
        }

        return Math.max(1, Mth.ceil((double) 15.0F * Mth.clamp(((double) 0.5F - d3) / (double) 0.5F, (double) 0.0F, (double) 1.0F)));
    }

    private static void setOutputPower(LevelAccessor level, BlockState state, int power, BlockPos pos, int waitTime) {
        level.setBlock(pos, state.setValue(OUTPUT_POWER, power), 3);
        level.scheduleTick(pos, state.getBlock(), waitTime);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(OUTPUT_POWER) != 0) {
            level.setBlock(pos, state.setValue(OUTPUT_POWER, 0), 3);
        }

    }
    @Override
    protected int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return blockState.getValue(OUTPUT_POWER);
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OUTPUT_POWER);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide() && !state.is(oldState.getBlock()) && state.getValue(OUTPUT_POWER) > 0 && !level.getBlockTicks().hasScheduledTick(pos, this)) {
            level.setBlock(pos, state.setValue(OUTPUT_POWER, 0), 18);
        }

    }

    static {
        OUTPUT_POWER = BlockStateProperties.POWER;
    }
}