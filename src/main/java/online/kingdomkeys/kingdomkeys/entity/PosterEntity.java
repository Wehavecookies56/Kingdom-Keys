package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import online.kingdomkeys.kingdomkeys.item.ModComponents;
import online.kingdomkeys.kingdomkeys.item.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PosterEntity extends HangingEntity {

    private static final EntityDataAccessor<Byte> DATA_DIRECTION = SynchedEntityData.defineId(PosterEntity.class, EntityDataSerializers.BYTE);

    @Nullable
    private GlobalPos target;

    public PosterEntity(EntityType<? extends PosterEntity> type, Level level) {
        super(type, level);
    }

    public PosterEntity(Level level, BlockPos pos, Direction direction) {
        super(ModEntities.TYPE_POSTER.get(), level, pos);
        setDirectionAndSync(pos, direction);
    }

    private void setDirectionAndSync(BlockPos pos, Direction direction) {
        this.setDirection(direction);
        this.setBoundingBox(this.calculateBoundingBox(pos, direction));
        this.entityData.set(DATA_DIRECTION, (byte) direction.get3DDataValue());
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (DATA_DIRECTION.equals(key)) {
            Direction direction = Direction.from3DDataValue(this.entityData.get(DATA_DIRECTION));
            this.setDirection(direction);
            this.setBoundingBox(this.calculateBoundingBox(this.pos, direction));
        }
    }

    public void setTarget(@Nullable GlobalPos target) {
        this.target = target;
    }

    @Nullable
    public GlobalPos getTarget() {
        return this.target;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_DIRECTION, (byte) Direction.SOUTH.get3DDataValue());
    }

    @Override
    protected AABB calculateBoundingBox(BlockPos blockPos, Direction direction) {
        double centerX = blockPos.getX() + 0.5;
        double centerY = blockPos.getY() + 0.5;
        double centerZ = blockPos.getZ() + 0.5;
        double inset = 0.46875; // matches vanilla item frame/painting's off-the-wall offset
        double x = centerX - direction.getStepX() * inset;
        double y = centerY - direction.getStepY() * inset;
        double z = centerZ - direction.getStepZ() * inset;

        double halfWidth = getWidth() / 32.0;
        double halfHeight = getHeight() / 32.0;
        double depth = 1.0 / 32.0;

        Direction.Axis axis = direction.getAxis();
        double sizeX = axis == Direction.Axis.X ? depth : halfWidth;
        double sizeY = axis == Direction.Axis.Y ? depth : halfHeight;
        double sizeZ = axis == Direction.Axis.Z ? depth : halfWidth;

        return new AABB(x - sizeX, y - sizeY, z - sizeZ, x + sizeX, y + sizeY, z + sizeZ);
    }

    public int getWidth() {
        return 16;
    }

    public int getHeight() {
        return 16;
    }

    @Override
    public void dropItem(@Nullable Entity entity) {
        if (!this.level().isClientSide) {
            this.playSound(SoundEvents.PAINTING_BREAK, 1.0F, 1.0F);
            if (entity instanceof Player player && player.hasInfiniteMaterials())
                return;
            ItemStack stack = new ItemStack(ModItems.struggle_poster.get());
            if (this.target != null) {
                stack.set(ModComponents.POSTER_TARGET.get(), this.target);
            }
            this.spawnAtLocation(stack);
        }
    }

    @Override
    public void playPlacementSound() {
        this.playSound(SoundEvents.PAINTING_PLACE, 1.0F, 1.0F);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(Items.COMPASS)) {
            if (this.target == null) {
                if (!this.level().isClientSide) {
                    player.displayClientMessage(Component.translatable("kingdomkeys.poster.no_target"), true);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            if (!this.level().isClientSide) {
                stack.set(DataComponents.LODESTONE_TRACKER, new LodestoneTracker(Optional.of(this.target), false));
                player.displayClientMessage(Component.translatable("kingdomkeys.poster.retuned"), true);
            }
            player.level().playSound(null, player.blockPosition(),SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.PLAYERS, 1.0F, 1.0F);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            player.displayClientMessage(Component.translatable("kingdomkeys.poster.use_compass"), true);
        }
        return super.interact(player, hand);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        if (this.direction != null) {
            tag.putByte("Facing", (byte) this.direction.get3DDataValue());
        }
        if (this.target != null) {
            GlobalPos.CODEC.encodeStart(NbtOps.INSTANCE, this.target).result().ifPresent(encoded -> tag.put("Target", encoded));
        }
        tag.putInt("TileX", this.pos.getX());
        tag.putInt("TileY", this.pos.getY());
        tag.putInt("TileZ", this.pos.getZ());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.pos = new BlockPos(tag.getInt("TileX"), tag.getInt("TileY"), tag.getInt("TileZ"));
        Direction direction = Direction.from3DDataValue(tag.getByte("Facing"));
        setDirectionAndSync(this.pos, direction);
        if (tag.contains("Target")) {
            GlobalPos.CODEC.parse(NbtOps.INSTANCE, tag.get("Target")).result().ifPresent(p -> this.target = p);
        }
    }
}