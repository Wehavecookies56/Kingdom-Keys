package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.WhiteMushroomGoal;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.UUID;

public class WhiteMushroomEntity extends BaseKHEntity {

    public WhiteMushroomEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
        xpReward = 2;
    }

    int satisfied = 0;

    UUID lastUsed;

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(!level().isClientSide()){
            lastUsed = source.getEntity().getUUID();
            if (source.is(KKDamageTypes.FIRE)) {
                extinguishFire();
                checkSatisfy(1);
            } else if (source.is(KKDamageTypes.ICE)) {
                checkSatisfy(2);
            } else if (source.is(KKDamageTypes.LIGHTNING)) {
                checkSatisfy(3);
            } else {
                setState(-2);
            }
        }

        //TODO angry animation and despawn
        return false;
    }

    private void checkSatisfy(int i) {
        if(getState() >= 0) { //Prevents multiple hitting attacks from counting at the wrong moment
            if (getState() == i) { //If the magic is right
                setState(-1); //Set to satisfied pose
                satisfied++;
                if (satisfied >= 3) { //If it's the 3rd time in a row
                    setState(-3); //Set to victory pose

                    boolean givenItem = false;
                    ItemStack reward = Utils.getWhiteMushroomReward();
                    if (lastUsed != null) {
                        Entity lastUsedEntity = ((ServerLevel)level()).getEntity(lastUsed);
                        Player player = null;
                        if (lastUsedEntity instanceof Player entity) {
                            player = entity;
                        } else if (lastUsedEntity instanceof OwnableEntity entity && entity.getOwner() instanceof Player owner) {
                            player = owner;
                        }
                        if (player != null) {
                            Utils.giveItems((ServerPlayer) player, reward);
                            givenItem = true;
                        }
                    }
                    if (!givenItem) {
                        ItemEntity ie = new ItemEntity(level(), getX(), getY(), getZ(), reward);
                        level().addFreshEntity(ie);
                    }
                }
            } else { //If magic is wrong set to angry pose
                setState(-2);
            }
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(4, new WhiteMushroomGoal(this));
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createLivingAttributes()
            .add(Attributes.FOLLOW_RANGE, 15.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.2D)
            .add(Attributes.MAX_HEALTH, 50.0D)
            .add(Attributes.ATTACK_DAMAGE, 0)
			.add(Attributes.ATTACK_KNOCKBACK, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("last_used")) {
            lastUsed = compound.getUUID("last_used");
        }
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        if (lastUsed != null) {
            compound.putUUID("last_used", lastUsed);
        }
        super.addAdditionalSaveData(compound);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 4;
    }

    @Override
    public EntityHelper.MobType getKHMobType() {
        return EntityHelper.MobType.HEARTLESS_EMBLEM;
    }

}
