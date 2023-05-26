package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import online.kingdomkeys.kingdomkeys.damagesource.KeybladeDamageSource;
import online.kingdomkeys.kingdomkeys.integration.epicfight.SeprateClassToAvoidLoadingIssuesExtendedReach;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.util.IExtendedReach;

public class CSAttackOffhandPacket {

    private int entityId;

    public CSAttackOffhandPacket() {
    }

    public CSAttackOffhandPacket(int entityId) {
        this.entityId = entityId;
    }

    public static CSAttackOffhandPacket decode(FriendlyByteBuf buffer) {
        CSAttackOffhandPacket msg = new CSAttackOffhandPacket();
        msg.entityId = buffer.readInt();
        return msg;
    }

    public static void handle(CSAttackOffhandPacket message, final Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = ctx.get().getSender();
            Entity entity = player.level.getEntity(message.entityId);
            if (SeprateClassToAvoidLoadingIssuesExtendedReach.isBattleMode(player))
                return;
            if (entity != null) {
                if (player.getOffhandItem().getItem() instanceof IExtendedReach theExtendedReachWeapon) {
					double distanceSq = player.distanceToSqr(entity);
                    float reach = Math.max(5, theExtendedReachWeapon.getReach());
                    double reachSq = reach * reach;
                    if (reachSq >= distanceSq) {
                        attackTargetEntityWithOffhandItem(player, entity);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    /**
     * Attacks for the player the targeted entity with the currently equipped item.  The equipped item has hitEntity
     * called on it. Args: targetEntity
     */
    public static void attackTargetEntityWithOffhandItem(Player player, Entity targetEntity) {
        if (!net.minecraftforge.common.ForgeHooks.onPlayerAttackTarget(player, targetEntity))
            return;
        if (targetEntity.isAttackable()) {
            if (!targetEntity.skipAttackInteraction(player)) {
                float damage = DamageCalculation.getKBStrengthDamage(player, player.getOffhandItem());
                float f1;
                if (targetEntity instanceof LivingEntity) {
                    f1 = EnchantmentHelper.getDamageBonus(player.getOffhandItem(), ((LivingEntity) targetEntity).getMobType());
                } else {
                    f1 = EnchantmentHelper.getDamageBonus(player.getOffhandItem(), MobType.UNDEFINED);
                }

                float f2 = player.getAttackStrengthScale(0.5F);
                damage = damage * (0.2F + f2 * f2 * 0.8F);
                f1 = f1 * f2;
                player.resetAttackStrengthTicker();
                if (damage > 0.0F || f1 > 0.0F) {
                    boolean flag = f2 > 0.9F;
                    boolean flag1 = false;
                    int i = 0;
                    i = i + EnchantmentHelper.getKnockbackBonus(player);
                    if (player.isSprinting() && flag) {
                        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, player.getSoundSource(), 1.0F, 1.0F);
                        ++i;
                        flag1 = true;
                    }

                    boolean flag2 = flag && player.fallDistance > 0.0F && !player.isOnGround() && !player.onClimbable() && !player.isInWater() && !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger() && targetEntity instanceof LivingEntity;
                    flag2 = flag2 && !player.isSprinting();
                    net.minecraftforge.event.entity.player.CriticalHitEvent hitResult = net.minecraftforge.common.ForgeHooks.getCriticalHit(player, targetEntity, flag2, flag2 ? 1.5F : 1.0F);
                    flag2 = hitResult != null;
                    if (flag2) {
                        damage *= hitResult.getDamageModifier();
                    }

                    damage = damage + f1;
                    boolean flag3 = false;
                    double d0 = player.walkDist - player.walkDistO;
                    if (flag && !flag2 && !flag1 && player.isOnGround() && d0 < (double) player.getSpeed()) {
                        ItemStack itemstack = player.getItemInHand(InteractionHand.OFF_HAND);
                        if (itemstack.getItem() instanceof KeybladeItem) {
                            flag3 = true;
                        }
                    }

                    float f4 = 0.0F;
                    boolean flag4 = false;
                    int j = EnchantmentHelper.getFireAspect(player);
                    if (targetEntity instanceof LivingEntity) {
                        f4 = ((LivingEntity) targetEntity).getHealth();
                        if (j > 0 && !targetEntity.isOnFire()) {
                            flag4 = true;
                            targetEntity.setSecondsOnFire(1);
                        }
                    }

                    Vec3 vec3d = targetEntity.getDeltaMovement();
                    boolean flag5 = targetEntity.hurt(KeybladeDamageSource.causeOffhandKeybladeDamage(player), damage);
                    if (flag5) {
                        if (i > 0) {
                            if (targetEntity instanceof LivingEntity) {
                                ((LivingEntity) targetEntity).knockback((float) i * 0.5F, Mth.sin(player.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(player.getYRot() * ((float) Math.PI / 180F)));
                            } else {
                                targetEntity.push(-Mth.sin(player.getYRot() * ((float) Math.PI / 180F)) * (float) i * 0.5F, 0.1D, Mth.cos(player.getYRot() * ((float) Math.PI / 180F)) * (float) i * 0.5F);
                            }

                            player.setDeltaMovement(player.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                            player.setSprinting(false);
                        }

                        if (flag3) {
                            float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * damage;

                            for (LivingEntity livingentity : player.level.getEntitiesOfClass(LivingEntity.class, targetEntity.getBoundingBox().inflate(1.0D, 0.25D, 1.0D))) {
                                if (livingentity != player && livingentity != targetEntity && !player.isAlliedTo(livingentity) && (!(livingentity instanceof ArmorStand) || !((ArmorStand) livingentity).isMarker()) && player.distanceToSqr(livingentity) < 9.0D) {
                                    livingentity.knockback(0.4F, Mth.sin(player.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(player.getYRot() * ((float) Math.PI / 180F)));
                                    livingentity.hurt(KeybladeDamageSource.causeOffhandKeybladeDamage(player), f3);
                                }
                            }

                            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
                            player.sweepAttack();
                        }

                        if (targetEntity instanceof ServerPlayer && targetEntity.hurtMarked) {
                            ((ServerPlayer) targetEntity).connection.send(new ClientboundSetEntityMotionPacket(targetEntity));
                            targetEntity.hurtMarked = false;
                            targetEntity.setDeltaMovement(vec3d);
                        }

                        if (flag2) {
                            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, player.getSoundSource(), 1.0F, 1.0F);
                            player.crit(targetEntity);
                        }

                        if (!flag2 && !flag3) {
                            if (flag) {
                                player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, player.getSoundSource(), 1.0F, 1.0F);
                            } else {
                                player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_WEAK, player.getSoundSource(), 1.0F, 1.0F);
                            }
                        }

                        if (f1 > 0.0F) {
                            player.magicCrit(targetEntity);
                        }

                        player.setLastHurtMob(targetEntity);
                        if (targetEntity instanceof LivingEntity) {
                            EnchantmentHelper.doPostHurtEffects((LivingEntity) targetEntity, player);
                        }

                        EnchantmentHelper.doPostDamageEffects(player, targetEntity);
                        ItemStack itemstack1 = player.getOffhandItem();
                        Entity entity = targetEntity;
                        if (targetEntity instanceof EnderDragonPart) {
                            entity = ((EnderDragonPart) targetEntity).parentMob;
                        }

                        if (!player.level.isClientSide && !itemstack1.isEmpty() && entity instanceof LivingEntity) {
                            ItemStack copy = itemstack1.copy();
                            itemstack1.hurtEnemy((LivingEntity) entity, player);
                            if (itemstack1.isEmpty()) {
                                net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(player, copy, InteractionHand.OFF_HAND);
                                player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                            }
                        }

                        if (targetEntity instanceof LivingEntity) {
                            float f5 = f4 - ((LivingEntity) targetEntity).getHealth();
                            player.awardStat(Stats.DAMAGE_DEALT, Math.round(f5 * 10.0F));
                            if (j > 0) {
                                targetEntity.setSecondsOnFire(j * 4);
                            }

                            if (player.level instanceof ServerLevel && f5 > 2.0F) {
                                int k = (int) ((double) f5 * 0.5D);
                                ((ServerLevel) player.level).sendParticles(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getX(), targetEntity.getY(0.5D), targetEntity.getZ(), k, 0.1D, 0.0D, 0.1D, 0.2D);
                            }
                        }

                        player.causeFoodExhaustion(0.1F);
                    } else {
                        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, player.getSoundSource(), 1.0F, 1.0F);
                        if (flag4) {
                            targetEntity.clearFire();
                        }
                    }
                }

            }
        }
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeInt(entityId);
    }

}
