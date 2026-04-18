package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.entity.block.MagicTargetBlockEntity;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.Collections;

public class MagicTargetEntity extends LivingEntity {
    private static final EntityDataAccessor<BlockPos> DATA_POS = SynchedEntityData.defineId(MagicTargetEntity.class, EntityDataSerializers.BLOCK_POS);

    public MagicTargetEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    public MagicTargetEntity(Level level) {
        this(ModEntities.TYPE_MAGIC_TARGET.get(), level);
    }

    public void setLinkedBlock(BlockPos pos) {
        this.entityData.set(DATA_POS,pos);
    }

    public BlockPos getLinkedBlock() {
        return this.entityData.get(DATA_POS);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!level().isClientSide && getLinkedBlock() != null) {
            if (level() instanceof ServerLevel server) {
                if (server.getBlockEntity(getLinkedBlock()) instanceof MagicTargetBlockEntity target) {
                    int power = Utils.getRedstoneFromMagic(source.getMsgId());
                    if (power > 0) {
                        target.onMagicHit(power);
                    }
                }
            }
        }

        return false;
    }

    // Removes the entity if the block is missing / broken
    @Override
    public void tick() {
        super.tick();
        setDeltaMovement(0, 0, 0);

        if (!level().isClientSide) {
            if (getLinkedBlock() == null || !(level().getBlockEntity(getLinkedBlock()) instanceof MagicTargetBlockEntity)) {
                discard();
            }
        }
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DATA_POS, BlockPos.ZERO);
        super.defineSynchedData(builder);
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public Iterable<ItemStack> getHandSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {}

    @Override
    public ItemStack getMainHandItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getOffhandItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public void knockback(double strength, double x, double z) {}

    @Override
    public void move(MoverType type, Vec3 pos) {}

    @Override
    public void push(Entity entity) {}

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putLong("LinkedPos", getLinkedBlock().asLong());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("LinkedPos")) {
            setLinkedBlock(BlockPos.of(tag.getLong("LinkedPos")));
        }
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                ;
    }

}