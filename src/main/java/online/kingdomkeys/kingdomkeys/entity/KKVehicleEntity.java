package online.kingdomkeys.kingdomkeys.entity;

import com.google.common.collect.Lists;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public abstract class KKVehicleEntity extends VehicleEntity {
    private float invFriction;
    public float deltaRotation;
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    private double lerpXRot;
    public boolean inputLeft;
    public boolean inputRight;
    public boolean inputBackward;
    public boolean inputForward;
    public boolean inputUp;
    public boolean inputDown;
    public float cameraX;
    public float cameraY;

    public KKVehicleEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    protected Entity.MovementEmission getMovementEmission() {
        return MovementEmission.EVENTS;
    }

    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    @Override
    protected Item getDropItem() {
        return ModItems.gummiShip.get();
    }


    @Override
    public boolean canCollideWith(Entity entity) {
        return (entity.canBeCollidedWith() || entity.isPushable()) && !this.isPassengerOfSameVehicle(entity);
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.getControllingPassenger() == null || !(this.getControllingPassenger() instanceof Player);
    }
    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public Vec3 getRelativePortalPosition(Direction.Axis axis, BlockUtil.FoundRectangle portal) {
        return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(axis, portal));
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
        return (new Vec3(0.0, dimensions.height() / 3.0F, 0.2F)).yRot(-this.getYRot() * 0.017453292F);
    }

    @Override
    public void animateHurt(float yaw) {
        this.setHurtDir(-this.getHurtDir());
        this.setHurtTime(10);
        this.setDamage(this.getDamage() * 11.0F);
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
        this.lerpX = x;
        this.lerpY = y;
        this.lerpZ = z;
        this.lerpYRot = yRot;
        this.lerpXRot = xRot;
        this.lerpSteps = 10;
    }

    @Override
    public double lerpTargetX() {
        return this.lerpSteps > 0 ? this.lerpX : this.getX();
    }
    @Override
    public double lerpTargetY() {
        return this.lerpSteps > 0 ? this.lerpY : this.getY();
    }
    @Override
    public double lerpTargetZ() {
        return this.lerpSteps > 0 ? this.lerpZ : this.getZ();
    }
    @Override
    public float lerpTargetXRot() {
        return this.lerpSteps > 0 ? (float)this.lerpXRot : this.getXRot();
    }
    @Override
    public float lerpTargetYRot() {
        return this.lerpSteps > 0 ? (float)this.lerpYRot : this.getYRot();
    }
    @Override
    public Direction getMotionDirection() {
        return this.getDirection().getClockWise();
    }
    @Override
    public void tick() {
        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }

        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        super.baseTick();
        this.tickLerp();
        if (this.isControlledByLocalInstance()) {
            this.floatBoat();
            if (this.level().isClientSide) {
                this.controlBoat();
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
        } else {
            this.setDeltaMovement(Vec3.ZERO);
        }

        this.checkInsideBlocks();
        List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(0.20000000298023224, -0.009999999776482582, 0.20000000298023224), EntitySelector.pushableBy(this));
        if (!list.isEmpty()) {
            boolean flag = !this.level().isClientSide && !(this.getControllingPassenger() instanceof Player);
            Iterator<Entity> var10 = list.iterator();

            while(true) {
                while(true) {
                    Entity entity;
                    do {
                        if (!var10.hasNext()) {
                            return;
                        }

                        entity = (Entity)var10.next();
                    } while(entity.hasPassenger(this));

                    if (flag && this.getPassengers().size() < this.getMaxPassengers() && !entity.isPassenger() && this.hasEnoughSpaceFor(entity) && entity instanceof LivingEntity && !(entity instanceof WaterAnimal) && !(entity instanceof Player)) {
                        entity.startRiding(this);
                    } else {
                        this.push(entity);
                    }
                }
            }
        }
    }

    private void tickLerp() {
        if (this.isControlledByLocalInstance()) {
            this.lerpSteps = 0;
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }

        if (this.lerpSteps > 0) {
            this.lerpPositionAndRotationStep(this.lerpSteps, this.lerpX, this.lerpY, this.lerpZ, this.lerpYRot, this.lerpXRot);
            --this.lerpSteps;
        }
    }
    @Override
    protected double getDefaultGravity() {
        return 0;
    }

    protected void floatBoat() {
        this.invFriction = 0.5F;
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.x * (double)this.invFriction, 0, vec3.z * (double)this.invFriction);
        this.deltaRotation *= this.invFriction;
    }

    abstract void controlBoat();
    public boolean hasEnoughSpaceFor(Entity entity) {
        return entity.getBbWidth() < this.getBbWidth();
    }

    protected void positionRider(Entity passenger, Entity.MoveFunction callback) {
        super.positionRider(passenger, callback);
        if (!passenger.getType().is(EntityTypeTags.CAN_TURN_IN_BOATS)) {
            passenger.setYRot(passenger.getYRot() + this.deltaRotation);
            passenger.setYHeadRot(passenger.getYHeadRot() + this.deltaRotation);
            this.clampRotation(passenger);
            if (passenger instanceof Animal && this.getPassengers().size() == this.getMaxPassengers()) {
                int i = passenger.getId() % 2 == 0 ? 90 : 270;
                passenger.setYBodyRot(((Animal)passenger).yBodyRot + (float)i);
                passenger.setYHeadRot(passenger.getYHeadRot() + (float)i);
            }
        }
    }

    public Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
        Vec3 vec3 = getCollisionHorizontalEscapeVector((double)(this.getBbWidth() * Mth.SQRT_OF_TWO), (double)livingEntity.getBbWidth(), livingEntity.getYRot());
        double d0 = this.getX() + vec3.x;
        double d1 = this.getZ() + vec3.z;
        BlockPos blockpos = BlockPos.containing(d0, this.getBoundingBox().maxY, d1);
        BlockPos blockpos1 = blockpos.below();
        if (!this.level().isWaterAt(blockpos1)) {
            List<Vec3> list = Lists.newArrayList();
            double d2 = this.level().getBlockFloorHeight(blockpos);
            if (DismountHelper.isBlockFloorValid(d2)) {
                list.add(new Vec3(d0, (double)blockpos.getY() + d2, d1));
            }

            double d3 = this.level().getBlockFloorHeight(blockpos1);
            if (DismountHelper.isBlockFloorValid(d3)) {
                list.add(new Vec3(d0, (double)blockpos1.getY() + d3, d1));
            }

            for (Pose pose : livingEntity.getDismountPoses()) {
                for (Vec3 vec31 : list) {
                    if (DismountHelper.canDismountTo(this.level(), vec31, livingEntity, pose)) {
                        livingEntity.setPose(pose);
                        return vec31;
                    }
                }
            }
        }

        return super.getDismountLocationForPassenger(livingEntity);
    }

    protected void clampRotation(Entity entityToUpdate) {
        entityToUpdate.setYBodyRot(this.getYRot());
        float f = Mth.wrapDegrees(entityToUpdate.getYRot() - this.getYRot());
        float f1 = Mth.clamp(f, -105.0F, 105.0F);
        entityToUpdate.yRotO += f1 - f;
        entityToUpdate.setYRot(entityToUpdate.getYRot() + f1 - f);
        entityToUpdate.setYHeadRot(entityToUpdate.getYRot());
    }

    public void onPassengerTurned(Entity entityToUpdate) {
        this.clampRotation(entityToUpdate);
    }

    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    public InteractionResult interact(Player player, InteractionHand hand) {
        InteractionResult interactionresult = super.interact(player, hand);
        if (interactionresult != InteractionResult.PASS) {
            return interactionresult;
        } else if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else {
            return InteractionResult.PASS;
        }
    }

    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().size() < this.getMaxPassengers();
    }

    protected int getMaxPassengers() {
        return 0;
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity var2 = this.getFirstPassenger();
        LivingEntity var10000;
        if (var2 instanceof LivingEntity livingentity) {
            var10000 = livingentity;
        } else {
            var10000 = super.getControllingPassenger();
        }

        return var10000;
    }

    public void setInput(boolean inputLeft, boolean inputRight, boolean inputFW, boolean inputBW, boolean inputUp, boolean inputDown, float cameraX, float cameraY) {
        this.inputLeft = inputLeft;
        this.inputRight = inputRight;
        this.inputUp = inputUp;
        this.inputDown = inputDown;
        this.inputBackward = inputBW;
        this.inputForward = inputFW;
        this.cameraX = cameraX;
        this.cameraY = cameraY;
    }

    protected Component getTypeName() {
        return Component.translatable(this.getDropItem().getDescriptionId());
    }

    public ItemStack getPickResult() {
        return new ItemStack(this.getDropItem());
    }
}

