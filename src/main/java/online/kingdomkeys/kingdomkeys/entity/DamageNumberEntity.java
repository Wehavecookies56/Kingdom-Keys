package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class DamageNumberEntity extends Entity {

    private static final EntityDataAccessor<String> TEXT = SynchedEntityData.defineId(DamageNumberEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(DamageNumberEntity.class, EntityDataSerializers.STRING);

    private int life;

    public DamageNumberEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    public DamageNumberEntity(Level level, double x, double y, double z, String text, String type) {
        this(ModEntities.TYPE_DAMAGE_NUMBER.get(), level);
        this.setPos(x, y, z);
        this.setText(text);
        this.setDamageType(type);
        this.setDeltaMovement((random.nextDouble() - 0.5D) * 0.1D, 0.05D, (random.nextDouble() - 0.5D) * 0.1D);
    }

    public DamageNumberEntity(Level level, double x, double y, double z, String text) {
        this(level,x,y,z,text,"player");
    }

    @Override
    public void tick() {
        super.tick();

        life++;

        if (life > 20) {
            this.discard();
            return;
        }

        Vec3 motion = this.getDeltaMovement();
        this.setDeltaMovement(motion.x * 0.98D, motion.y + 0.002D, motion.z * 0.98D);

        this.move(MoverType.SELF, this.getDeltaMovement());
    }

    public void setText(String text) {
        this.entityData.set(TEXT, text);
    }

    public String getText() {
        return this.entityData.get(TEXT);
    }

    public void setDamageType(String text) {
        this.entityData.set(TYPE, text);
    }

    public String getDamageType() {
        return this.entityData.get(TYPE);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(TEXT, "");
        builder.define(TYPE, "");
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        setText(tag.getString("text"));
        setDamageType(tag.getString("type"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putString("text", getText());
        tag.putString("type", getDamageType());
    }
}
