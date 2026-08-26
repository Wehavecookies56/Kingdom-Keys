package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MeteorEntity extends ThrowableProjectile {
    int maxTicks = 80;
    float dmgMult = 2;
    LivingEntity lockedOnEntity;

    public MeteorEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
        super(type, world);
        this.blocksBuilding = true;
    }

    public MeteorEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockedOnEntity) {
        super(ModEntities.TYPE_METEOR.get(), player, world);
        setCaster(player.getUUID());
        this.dmgMult = dmgMult;
        this.lockedOnEntity = lockedOnEntity;
    }

    public int getMaxTicks() {
        return maxTicks;
    }

    public void setMaxTicks(int maxTicks) {
        this.maxTicks = maxTicks;
    }

    private static final EntityDataAccessor<Optional<UUID>> OWNER = SynchedEntityData.defineId(MeteorEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        if (this.entityData.get(OWNER) != null) {
            compound.putString("OwnerUUID", this.entityData.get(OWNER).get().toString());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.entityData.set(OWNER, Optional.of(UUID.fromString(compound.getString("OwnerUUID"))));
    }

    public Player getCaster() {
        return this.getEntityData().get(OWNER).isPresent() ? this.level().getPlayerByUUID(this.getEntityData().get(OWNER).get()) : null;
    }

    public void setCaster(UUID uuid) {
        this.entityData.set(OWNER, Optional.of(uuid));
    }

    List<LivingEntity> list = new ArrayList<>();


    @Override
    public void tick() {
        if (this.tickCount > maxTicks) {
            this.remove(RemovalReason.KILLED);
        }

        if (getCaster() == null) {
            remove(RemovalReason.KILLED);
            return;
        }

        float radius = 5F;

        if (!level().isClientSide && getCaster() != null) { // Only calculate and spawn lightning bolts server side
            if (tickCount == 1) {
                if (lockedOnEntity != null) {
                    list = Utils.getLivingEntitiesInRadiusExcludingParty(getCaster(), lockedOnEntity, radius,radius,radius); //This method removes the second param entity from the list returned, so we add it back
                    list.add(lockedOnEntity);
                } else {
                    list = Utils.getLivingEntitiesInRadiusExcludingParty(getCaster(), radius);
                }
                list.remove(this);
            }
            if (tickCount % 6 == 1){
                float dmg = this.getOwner() instanceof Player player ? DamageCalculation.getMagicDamage(player) * 0.055F :3;

                /*if (!list.isEmpty()) { //If it detects entities either around the caster or around the locked on entity
                    int i = level().random.nextInt(list.size());
                    Entity e = list.get(i);
                    if (e instanceof LivingEntity) {
                        if(!e.isAlive()){
                            list.remove(e);
                        }
                        CometEntity shot = new CometEntity(getCaster().level(), getCaster(), dmg * dmgMult, 3, 0, e.getX(), e.getY()+20, e.getZ(), true);
                        //level().playSound(null,shot.blockPosition().below(15), ModSoundsRM.RUIN.get(), SoundSource.PLAYERS,1,1);
                        level().addFreshEntity(shot);
                    }
                } else { //Random around player
                    int posX = (int) (getCaster().getX() + level().random.nextInt((int) (radius*2)) - radius / 2)-1;
                    int posY = (int) getCaster().getY() + 3;
                    int posZ = (int) (getCaster().getZ() + level().random.nextInt((int) (radius*2)) - radius / 2)-1;

                    CometEntity shot = new CometEntity(getCaster().level(), getCaster(), dmg * dmgMult, posX, 1, posX , level().getHeight(Types.WORLD_SURFACE, posX, posZ)+20, posZ, true);
                    //level().playSound(null,shot.blockPosition().below(15), ModSoundsRM.LIGHT_BEAM.get(), SoundSource.PLAYERS,1,1);
                    level().addFreshEntity(shot);
                }*/
            }
        }

        super.tick();
    }

    @Override
    protected void onHit(HitResult result){

    }


    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(OWNER, Optional.of(Util.NIL_UUID));
    }
}
