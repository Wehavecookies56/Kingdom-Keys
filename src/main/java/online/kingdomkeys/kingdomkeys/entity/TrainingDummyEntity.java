package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.damagesource.StopDamageSource;
import online.kingdomkeys.kingdomkeys.data.PlayerData;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.item.organization.IOrgWeapon;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Strings;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.Collections;

public class TrainingDummyEntity extends LivingEntity {
    private Player lastAttacker;

    private float damageAccumulated = 0;
    private int dpsTimer = 0;

    protected TrainingDummyEntity(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void move(MoverType type, Vec3 pos) {

    }

    @Override
    public void push(Entity entity) {

    }

    @Override
    public void tick() {
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

        spawnDamageText(String.valueOf((int) amount), source);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (level().isClientSide)
            return false;

       /* Entity attacker = source.getEntity();

        // If damagesource is player it's a keyblade attack, therefore calculate damage multiplier
        float newDMG = source.getMsgId().equals("player") ? 0 : amount;
        if (attacker instanceof Player player) {
            //First we calculate the weapon damage
            ItemStack weapon = Utils.getWeaponDamageStack(source, player);
            if (weapon != null && !(source instanceof StopDamageSource)) {
                float dmg = 0;
                if (weapon.getItem() instanceof KeybladeItem) {
                    dmg = DamageCalculation.getKBStrengthDamage(player, weapon);
                } else if (weapon.getItem() instanceof IOrgWeapon) {
                    dmg = DamageCalculation.getOrgStrengthDamage(player, weapon);
                }

                if (player.fallDistance > 0.0F && !player.onGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger()) { // Crit attack formula
                    dmg *= ModConfigs.critMult;
                    dmg += dmg * PlayerData.get(player).getNumberOfAbilitiesEquipped(Strings.criticalBoost) * 0.1F;
                }
                newDMG = (amount - 1) + dmg * player.getAttackStrengthScale(0);
            }

            PlayerData playerData = PlayerData.get(player);
            if (playerData != null && playerData.getActiveDriveForm().equals(Strings.Form_Anti)) {
                newDMG = playerData.getStrength(true);
            }
            recordDamage(player, newDMG, source);
        }

        boolean result = super.hurt(source, newDMG);
        this.invulnerableTime = 0;*/
        return super.hurt(source, amount);
    }

    @Override
    public void animateHurt(float yaw) {

    }

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
    public void knockback(double strength, double x, double z) {

    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {}

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {}

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
                .add(Attributes.MAX_HEALTH, 100.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                ;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {}

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }
}