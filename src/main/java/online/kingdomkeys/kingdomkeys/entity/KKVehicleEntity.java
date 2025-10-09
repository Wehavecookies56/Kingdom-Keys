package online.kingdomkeys.kingdomkeys.entity;

import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
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
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public class KKVehicleEntity extends VehicleEntity implements Leashable {
        private float invFriction;
        private float deltaRotation;
        private int lerpSteps;
        private double lerpX;
        private double lerpY;
        private double lerpZ;
        private double lerpYRot;
        private double lerpXRot;
        private boolean inputLeft;
        private boolean inputRight;
        private boolean inputBackward;
        private boolean inputForward;
        private boolean inputUp;
        private boolean inputDown;
        @Nullable
        private Leashable.LeashData leashData;

        public KKVehicleEntity(Level level, double x, double y, double z) {
            this(EntityType.BOAT, level);
            this.setPos(x, y, z);
            this.xo = x;
            this.yo = y;
            this.zo = z;
        }

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

    public boolean canCollideWith(Entity entity) {
            return canVehicleCollide(this, entity);
        }

        public static boolean canVehicleCollide(Entity vehicle, Entity entity) {
            return (entity.canBeCollidedWith() || entity.isPushable()) && !vehicle.isPassengerOfSameVehicle(entity);
        }

        public boolean canBeCollidedWith() {
            return true;
        }

        public boolean isPushable() {
            return true;
        }

        public Vec3 getRelativePortalPosition(Direction.Axis axis, BlockUtil.FoundRectangle portal) {
            return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(axis, portal));
        }

        protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
            float f = this.getSinglePassengerXOffset();
            if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(entity);
                if (i == 0) {
                    f = 0.2F;
                } else {
                    f = -0.6F;
                }

                if (entity instanceof Animal) {
                    f += 0.2F;
                }
            }

            return (new Vec3(0.0, (double)(dimensions.height() / 3.0F), (double)f)).yRot(-this.getYRot() * 0.017453292F);
        }



        /*public void push(Entity entity) {
            if (entity instanceof net.minecraft.world.entity.vehicle.Boat) {
                if (entity.getBoundingBox().minY < this.getBoundingBox().maxY) {
                    super.push(entity);
                }
            } else if (entity.getBoundingBox().minY <= this.getBoundingBox().minY) {
                super.push(entity);
            }

        }*/



        public void animateHurt(float yaw) {
            this.setHurtDir(-this.getHurtDir());
            this.setHurtTime(10);
            this.setDamage(this.getDamage() * 11.0F);
        }

        public boolean isPickable() {
            return !this.isRemoved();
        }

        public void lerpTo(double x, double y, double z, float yRot, float xRot, int steps) {
            this.lerpX = x;
            this.lerpY = y;
            this.lerpZ = z;
            this.lerpYRot = (double)yRot;
            this.lerpXRot = (double)xRot;
            this.lerpSteps = 10;
        }

        public double lerpTargetX() {
            return this.lerpSteps > 0 ? this.lerpX : this.getX();
        }

        public double lerpTargetY() {
            return this.lerpSteps > 0 ? this.lerpY : this.getY();
        }

        public double lerpTargetZ() {
            return this.lerpSteps > 0 ? this.lerpZ : this.getZ();
        }

        public float lerpTargetXRot() {
            return this.lerpSteps > 0 ? (float)this.lerpXRot : this.getXRot();
        }

        public float lerpTargetYRot() {
            return this.lerpSteps > 0 ? (float)this.lerpYRot : this.getYRot();
        }

        public Direction getMotionDirection() {
            return this.getDirection().getClockWise();
        }

        public void tick() {

            if (this.getHurtTime() > 0) {
                this.setHurtTime(this.getHurtTime() - 1);
            }

            if (this.getDamage() > 0.0F) {
                this.setDamage(this.getDamage() - 1.0F);
            }

            super.tick();
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
                Iterator var10 = list.iterator();

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

        @Nullable
        public Leashable.LeashData getLeashData() {
            return this.leashData;
        }

        public void setLeashData(@Nullable Leashable.LeashData leashData) {
            this.leashData = leashData;
        }

        public Vec3 getLeashOffset() {
            return new Vec3(0.0, (double)(0.88F * this.getEyeHeight()), (double)(this.getBbWidth() * 0.64F));
        }

        public void elasticRangeLeashBehaviour(Entity leashHolder, float distance) {
            Vec3 vec3 = leashHolder.position().subtract(this.position()).normalize().scale((double)distance - 6.0);
            Vec3 vec31 = this.getDeltaMovement();
            boolean flag = vec31.dot(vec3) > 0.0;
            this.setDeltaMovement(vec31.add(vec3.scale(flag ? 0.15000000596046448 : 0.20000000298023224)));
        }


        public float getGroundFriction() {
            AABB aabb = this.getBoundingBox();
            AABB aabb1 = new AABB(aabb.minX, aabb.minY - 0.001, aabb.minZ, aabb.maxX, aabb.minY, aabb.maxZ);
            int i = Mth.floor(aabb1.minX) - 1;
            int j = Mth.ceil(aabb1.maxX) + 1;
            int k = Mth.floor(aabb1.minY) - 1;
            int l = Mth.ceil(aabb1.maxY) + 1;
            int i1 = Mth.floor(aabb1.minZ) - 1;
            int j1 = Mth.ceil(aabb1.maxZ) + 1;
            VoxelShape voxelshape = Shapes.create(aabb1);
            float f = 0.0F;
            int k1 = 0;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(int l1 = i; l1 < j; ++l1) {
                for(int i2 = i1; i2 < j1; ++i2) {
                    int j2 = (l1 != i && l1 != j - 1 ? 0 : 1) + (i2 != i1 && i2 != j1 - 1 ? 0 : 1);
                    if (j2 != 2) {
                        for(int k2 = k; k2 < l; ++k2) {
                            if (j2 <= 0 || k2 != k && k2 != l - 1) {
                                blockpos$mutableblockpos.set(l1, k2, i2);
                                BlockState blockstate = this.level().getBlockState(blockpos$mutableblockpos);
                                if (!(blockstate.getBlock() instanceof WaterlilyBlock) && Shapes.joinIsNotEmpty(blockstate.getCollisionShape(this.level(), blockpos$mutableblockpos).move((double)l1, (double)k2, (double)i2), voxelshape, BooleanOp.AND)) {
                                    f += blockstate.getFriction(this.level(), blockpos$mutableblockpos, this);
                                    ++k1;
                                }
                            }
                        }
                    }
                }
            }

            return f / (float)k1;
        }

        private boolean checkInWater() {
            AABB aabb = this.getBoundingBox();
            int i = Mth.floor(aabb.minX);
            int j = Mth.ceil(aabb.maxX);
            int k = Mth.floor(aabb.minY);
            int l = Mth.ceil(aabb.minY + 0.001);
            int i1 = Mth.floor(aabb.minZ);
            int j1 = Mth.ceil(aabb.maxZ);
            boolean flag = false;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(int k1 = i; k1 < j; ++k1) {
                for(int l1 = k; l1 < l; ++l1) {
                    for(int i2 = i1; i2 < j1; ++i2) {
                        blockpos$mutableblockpos.set(k1, l1, i2);
                        FluidState fluidstate = this.level().getFluidState(blockpos$mutableblockpos);
                    }
                }
            }

            return flag;
        }

        @Nullable
        private net.minecraft.world.entity.vehicle.Boat.Status isUnderwater() {
            AABB aabb = this.getBoundingBox();
            double d0 = aabb.maxY + 0.001;
            int i = Mth.floor(aabb.minX);
            int j = Mth.ceil(aabb.maxX);
            int k = Mth.floor(aabb.maxY);
            int l = Mth.ceil(d0);
            int i1 = Mth.floor(aabb.minZ);
            int j1 = Mth.ceil(aabb.maxZ);
            boolean flag = false;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(int k1 = i; k1 < j; ++k1) {
                for(int l1 = k; l1 < l; ++l1) {
                    for(int i2 = i1; i2 < j1; ++i2) {
                        blockpos$mutableblockpos.set(k1, l1, i2);
                        FluidState fluidstate = this.level().getFluidState(blockpos$mutableblockpos);
                    }
                }
            }

            return flag ? net.minecraft.world.entity.vehicle.Boat.Status.UNDER_WATER : null;
        }

        protected double getDefaultGravity() {
            return 0.04;
        }

        private void floatBoat() {
            this.invFriction = 0.5F;

            Vec3 vec3 = this.getDeltaMovement();
            this.setDeltaMovement(vec3.x * (double)this.invFriction, 0, vec3.z * (double)this.invFriction);
            this.deltaRotation *= this.invFriction;

        }

        private void controlBoat() {
            if (this.isVehicle()) {
                float f = 0.0F;
                if (this.inputLeft) {
                    --this.deltaRotation;
                }

                if (this.inputRight) {
                    ++this.deltaRotation;
                }

                if (this.inputRight != this.inputLeft && !this.inputForward && !this.inputBackward) {
                    f += 0.005F;
                }

                this.setYRot(this.getYRot() + this.deltaRotation);
                if (this.inputForward) {
                    f += 4F;
                }

                if (this.inputBackward) {
                    f -= 4F;
                }

                if(this.inputUp){
                    this.setDeltaMovement(0,1,0);
                }
                if(this.inputDown){
                    this.setDeltaMovement(0,-1,0);
                }

                this.setDeltaMovement(this.getDeltaMovement().add((Mth.sin(-this.getYRot() * 0.017453292F) * f), 0.0, (double)(Mth.cos(this.getYRot() * 0.017453292F) * f)));
            }
        }

        protected float getSinglePassengerXOffset() {
            return 0.0F;
        }

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
            this.writeLeashData(compound, this.leashData);
        }

        protected void readAdditionalSaveData(CompoundTag compound) {
            this.leashData = this.readLeashData(compound);
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

        public void remove(Entity.RemovalReason reason) {
            if (!this.level().isClientSide && reason.shouldDestroy() && this.isLeashed()) {
                this.dropLeash(true, true);
            }

            super.remove(reason);
        }

        protected boolean canAddPassenger(Entity passenger) {
            return this.getPassengers().size() < this.getMaxPassengers();
        }

        protected int getMaxPassengers() {
            return 2;
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

        public void setInput(boolean inputLeft, boolean inputRight, boolean inputFW, boolean inputBW, boolean inputUp, boolean inputDown) {
            this.inputLeft = inputLeft;
            this.inputRight = inputRight;
            this.inputUp = inputUp;
            this.inputDown = inputDown;
            this.inputBackward = inputBW;
            this.inputForward = inputFW;
        }

        protected Component getTypeName() {
            return Component.translatable(this.getDropItem().getDescriptionId());
        }

        public ItemStack getPickResult() {
            return new ItemStack(this.getDropItem());
        }
}

