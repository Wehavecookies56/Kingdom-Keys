package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.item.ModItems;

import java.util.Collections;

public class TrainingDummyEntity extends LivingEntity {
    private Player lastAttacker;

    private float damageAccumulated = 0;
    private int dpsTimer = 0;

    private static final EntityDataAccessor<Boolean> IGNORE_CD = SynchedEntityData.defineId(TrainingDummyEntity.class, EntityDataSerializers.BOOLEAN);

    public TrainingDummyEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if(player.isShiftKeyDown()){
            if(player.level().isClientSide())
                return InteractionResult.SUCCESS;

            setIgnoreCD(!getIgnoreCD());
            player.displayClientMessage(Component.translatable("kingdomkeys.entity.training_dummy.iframes", (getIgnoreCD() ? "disabled" : "enabled")), true);
        }
        return super.interact(player, hand);
    }

    @Override
    public void tick() {
        if (this.entityData.get(HIT_TICKS) > 0) {
            this.entityData.set(HIT_TICKS, this.entityData.get(HIT_TICKS) - 1);
        }

        super.tick();
        this.setDeltaMovement(Vec3.ZERO);
        this.setPos(this.getX(), this.getY(), this.getZ());
        if (!level().isClientSide) {
            dpsTimer++;

            if (dpsTimer >= 20) {
                if (damageAccumulated > 0) {
                    if (lastAttacker instanceof ServerPlayer player) {
                        player.displayClientMessage(Component.literal(String.format("DPS: %.1f", damageAccumulated)), true);
                    }
                }
                damageAccumulated = 0;
                dpsTimer = 0;
            }
        }
    }

    public void recordDamage(Player player, float amount, DamageSource source) {
        this.lastAttacker = player;
        this.damageAccumulated += amount;

        String text = (amount % 1 == 0) ? String.valueOf((int) amount) : String.format("%.1f", amount);
        spawnDamageText(text, source);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(source.getEntity() instanceof Player player){
            if(player.getMainHandItem().getItem() == Items.FEATHER){
                ItemEntity itementity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(ModItems.trainingDummy.get()));
                itementity.setDefaultPickUpDelay();
                this.level().addFreshEntity(itementity);
                remove(RemovalReason.DISCARDED);
            }
        }
        if (level().isClientSide)
            return false;

        Entity directEntity = source.getDirectEntity();
        if (directEntity != null) {
            //Calculate the position it has to bounce off
            double dx = this.getX() - directEntity.getX();
            double dz = this.getZ() - directEntity.getZ();

            double length = Math.sqrt(dx * dx + dz * dz);

            if (length > 0) {
                dx /= length;
                dz /= length;
            }

            float yaw = this.getYRot();
            float rad = (float) Math.toRadians(-yaw);

            float localX = (float)(dx * Math.cos(rad) - dz * Math.sin(rad));
            float localZ = (float)(dx * Math.sin(rad) + dz * Math.cos(rad));

            if (length > 0) {
                this.entityData.set(HIT_DIR_X, localX);
                this.entityData.set(HIT_DIR_Z, localZ);
            }
        }

        return super.hurt(source, amount);
    }

    @Override
    protected void playHurtSound(DamageSource source) {
        this.playSound(SoundEvents.ARMOR_STAND_HIT, 1.0F, 1.0F);
    }

    @Override
    public void animateHurt(float yaw) {}

    @Override
    protected void actuallyHurt(DamageSource source, float amount) {
        float prevHealth = this.getHealth();

        super.actuallyHurt(source, amount);
        this.setHealth(prevHealth);
    }

    private void spawnDamageText(String text, DamageSource source) {
        if (!(level() instanceof ServerLevel serverLevel))
            return;

        double x = this.getX() + (random.nextDouble() - 0.5D) * 0.5D;
        double y = this.getY() + 1.5D;
        double z = this.getZ() + (random.nextDouble() - 0.5D) * 0.5D;

        DamageNumberEntity dmg = new DamageNumberEntity(serverLevel, x, y, z, text, source.getMsgId());
        serverLevel.addFreshEntity(dmg);
    }

    @Override
    public void knockback(double strength, double x, double z) {}

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void move(MoverType type, Vec3 pos) {}

    @Override
    public void push(Entity entity) {}

    public static final EntityDataAccessor<Float> HIT_DIR_X = SynchedEntityData.defineId(TrainingDummyEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> HIT_DIR_Z = SynchedEntityData.defineId(TrainingDummyEntity.class, EntityDataSerializers.FLOAT);

    public static final EntityDataAccessor<Integer> HIT_TICKS = SynchedEntityData.defineId(TrainingDummyEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> HIT_STRENGTH = SynchedEntityData.defineId(TrainingDummyEntity.class, EntityDataSerializers.FLOAT);

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IGNORE_CD, false);
        builder.define(HIT_DIR_X, 0f);
        builder.define(HIT_DIR_Z, 0f);
        builder.define(HIT_TICKS, 0);
        builder.define(HIT_STRENGTH, 0f);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        setIgnoreCD(tag.getBoolean("text"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("text", getIgnoreCD());
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0D)
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                ;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {}

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    public void setIgnoreCD(boolean text) {
        this.entityData.set(IGNORE_CD, text);
    }

    public boolean getIgnoreCD() {
        return this.entityData.get(IGNORE_CD);
    }

    public int getHitTicks() {
        return this.entityData.get(HIT_TICKS);
    }

    public float getHitStrength() {
        return this.entityData.get(HIT_STRENGTH);
    }

    public float getHitDirX(){
        return this.entityData.get(HIT_DIR_X);
    }
    public float getHitDirZ(){
        return this.entityData.get(HIT_DIR_Z);
    }
}