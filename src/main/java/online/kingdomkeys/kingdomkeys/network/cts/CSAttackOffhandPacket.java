package online.kingdomkeys.kingdomkeys.network.cts;

import java.util.function.Supplier;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.entity.PartEntity;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.damagesource.KeybladeDamageSource;
import online.kingdomkeys.kingdomkeys.item.KeybladeItem;
import online.kingdomkeys.kingdomkeys.network.Packet;
import online.kingdomkeys.kingdomkeys.util.IExtendedReach;

public record CSAttackOffhandPacket(int entityId) implements Packet {

	public static final Type<CSAttackOffhandPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "cs_attack_off_hand"));

	public static final StreamCodec<FriendlyByteBuf, CSAttackOffhandPacket> STREAM_CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			CSAttackOffhandPacket::entityId,
			CSAttackOffhandPacket::new
	);

	/**
	* Attacks for the player the targeted entity with the currently equipped item. The equipped item has hitEntity
	* called on it. Args: targetEntity
	*/
	public static void attackTargetEntityWithOffhandItem(ServerPlayer player, Entity targetEntity) {
		if (!CommonHooks.onPlayerAttackTarget(player, targetEntity))
			return;
		if (targetEntity.isAttackable()) {
			if (!targetEntity.skipAttackInteraction(player)) {
				float damage = 1F;
				DamageSource damagesource = KeybladeDamageSource.causeOffhandKeybladeDamage(player);
				ItemStack itemstack = player.getOffhandItem();
				float enchantDamage = getEnchantedDamage(player, targetEntity, damage, damagesource) - damage;
				float scale = player.getAttackStrengthScale(0.5F);
				damage *= 0.2F + scale * scale * 0.8F;
				enchantDamage *= scale;
				if (targetEntity.getType().is(EntityTypeTags.REDIRECTABLE_PROJECTILE)
						&& targetEntity instanceof Projectile projectile
						&& projectile.deflect(ProjectileDeflection.AIM_DEFLECT, player, player, true)) {
					player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, player.getSoundSource());
					return;
				}

				if (damage > 0.0F || enchantDamage > 0.0F) {
					boolean charged = scale > 0.9F;
					boolean knockback;
					if (player.isSprinting() && charged) {
						player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_KNOCKBACK, player.getSoundSource(), 1.0F, 1.0F);
						knockback = true;
					} else {
						knockback = false;
					}
					damage += itemstack.getItem().getAttackDamageBonus(targetEntity, damage, damagesource);
					boolean isCrit = charged
							&& player.fallDistance > 0.0F
							&& !player.onGround()
							&& !player.onClimbable()
							&& !player.isInWater()
							&& !player.hasEffect(MobEffects.BLINDNESS)
							&& !player.isPassenger()
							&& targetEntity instanceof LivingEntity
							&& !player.isSprinting();
					// Neo: Fire the critical hit event and override the critical hit status and damage multiplier based on the event.
					// The boolean local above (flag2) is the vanilla critical hit result.
					var critEvent = CommonHooks.fireCriticalHit(player, targetEntity, isCrit, isCrit ? 1.5F : 1.0F);
					isCrit = critEvent.isCriticalHit();
					if (isCrit) {
						damage *= critEvent.getDamageMultiplier();
					}

					float combinedDamage = damage + enchantDamage;
					boolean canSweep = false;
					double d0 = (double)(player.walkDist - player.walkDistO);
					if (charged && !isCrit && !knockback && player.onGround() && d0 < (double)player.getSpeed()) {
						ItemStack itemstack1 = player.getItemInHand(InteractionHand.OFF_HAND);
						if (itemstack.getItem() instanceof KeybladeItem) {
							canSweep = itemstack1.canPerformAction(ItemAbilities.SWORD_SWEEP);
						}
					}

					float targetHealth = 0.0F;
					if (targetEntity instanceof LivingEntity livingentity) {
						targetHealth = livingentity.getHealth();
					}

					Vec3 vec3 = targetEntity.getDeltaMovement();
					boolean wasHurt = targetEntity.hurt(damagesource, combinedDamage);
					if (wasHurt) {
						float f4 = getKnockback(player, targetEntity, damagesource) + (knockback ? 1.0F : 0.0F);
						if (f4 > 0.0F) {
							if (targetEntity instanceof LivingEntity livingentity1) {
								livingentity1.knockback(
										(double)(f4 * 0.5F),
										(double)Mth.sin(player.getYRot() * (float) (Math.PI / 180.0)),
										(double)(-Mth.cos(player.getYRot() * (float) (Math.PI / 180.0)))
								);
							} else {
								targetEntity.push(
										(double)(-Mth.sin(player.getYRot() * (float) (Math.PI / 180.0)) * f4 * 0.5F),
										0.1,
										(double)(Mth.cos(player.getYRot() * (float) (Math.PI / 180.0)) * f4 * 0.5F)
								);
							}

							player.setDeltaMovement(player.getDeltaMovement().multiply(0.6, 1.0, 0.6));
							player.setSprinting(false);
						}

						if (canSweep) {
							float sweepDamage = 1.0F + (float)player.getAttributeValue(Attributes.SWEEPING_DAMAGE_RATIO) * damage;

							for (LivingEntity livingentity2 : player.level()
									.getEntitiesOfClass(LivingEntity.class, targetEntity.getBoundingBox().inflate(1.0, 0.25, 1.0))) {
								double entityReachSq = Mth.square(player.entityInteractionRange()); // Use entity reach instead of constant 9.0. Vanilla uses bottom center-to-center checks here, so don't update this to use canReach, since it uses closest-corner checks.
								if (livingentity2 != player
										&& livingentity2 != targetEntity
										&& !player.isAlliedTo(livingentity2)
										&& (!(livingentity2 instanceof ArmorStand) || !((ArmorStand)livingentity2).isMarker())
										&& player.distanceToSqr(livingentity2) < entityReachSq) {
									float totalSweepDamage = getEnchantedDamage(player, livingentity2, sweepDamage, damagesource) * scale;
									livingentity2.knockback(
											0.4F,
											(double)Mth.sin(player.getYRot() * (float) (Math.PI / 180.0)),
											(double)(-Mth.cos(player.getYRot() * (float) (Math.PI / 180.0)))
									);
									livingentity2.hurt(damagesource, totalSweepDamage);
									if (player.level() instanceof ServerLevel serverlevel) {
										EnchantmentHelper.doPostAttackEffects(serverlevel, livingentity2, damagesource);
									}
								}
							}

							player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, player.getSoundSource(), 1.0F, 1.0F);
							player.sweepAttack();
						}

						if (targetEntity instanceof ServerPlayer && targetEntity.hurtMarked) {
							((ServerPlayer) targetEntity).connection.send(new ClientboundSetEntityMotionPacket(targetEntity));
							targetEntity.hurtMarked = false;
							targetEntity.setDeltaMovement(vec3);
						}

						if (isCrit) {
							player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_CRIT, player.getSoundSource(), 1.0F, 1.0F);
							player.crit(targetEntity);
						}

						if (!isCrit && !canSweep) {
							if (charged) {
								player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_STRONG, player.getSoundSource(), 1.0F, 1.0F);
							} else {
								player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_WEAK, player.getSoundSource(), 1.0F, 1.0F);
							}
						}

						if (enchantDamage > 0.0F) {
							player.magicCrit(targetEntity);
						}

						player.setLastHurtMob(targetEntity);
						Entity entity = targetEntity;
						if (targetEntity instanceof PartEntity) {
							entity = ((PartEntity<?>) targetEntity).getParent();
						}

						boolean itemHurt = false;
						ItemStack copy = itemstack.copy();
						if (player.level() instanceof ServerLevel serverlevel1) {
							if (entity instanceof LivingEntity livingentity3) {
								itemHurt = itemstack.hurtEnemy(livingentity3, player);
							}

							EnchantmentHelper.doPostAttackEffects(serverlevel1, targetEntity, damagesource);
						}

						if (!player.level().isClientSide && !itemstack.isEmpty() && entity instanceof LivingEntity) {
							if (itemHurt) {
								itemstack.postHurtEnemy((LivingEntity) entity, player);
							}
							if (itemstack.isEmpty()) {
								net.neoforged.neoforge.event.EventHooks.onPlayerDestroyItem(player, copy, itemstack == player.getOffhandItem() ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
								if (itemstack == player.getOffhandItem()) {
									player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
								} else {
									player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
								}
							}
						}
						if (targetEntity instanceof LivingEntity) {
							float damageDealt = targetHealth - ((LivingEntity) targetEntity).getHealth();
							player.awardStat(Stats.DAMAGE_DEALT, Math.round(damageDealt * 10.0F));
							if (player.level() instanceof ServerLevel && damageDealt > 2.0F) {
								int i = (int)((double)damageDealt * 0.5);
								((ServerLevel)player.level())
										.sendParticles(ParticleTypes.DAMAGE_INDICATOR, targetEntity.getX(), targetEntity.getY(0.5), targetEntity.getZ(), i, 0.1, 0.0, 0.1, 0.2);
							}
						}

						player.causeFoodExhaustion(0.1F);
					} else {
						player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, player.getSoundSource(), 1.0F, 1.0F);
					}
				}
				player.swing(InteractionHand.OFF_HAND);
			}
		}
	}

	public static float getKnockback(ServerPlayer player, Entity pAttacker, DamageSource pDamageSource) {
		float f = (float)player.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
		return player.level() instanceof ServerLevel serverlevel ? EnchantmentHelper.modifyKnockback(serverlevel, player.getOffhandItem(), pAttacker, pDamageSource, f) : f;
	}

	public static float getEnchantedDamage(ServerPlayer player, Entity pEntity, float pDamage, DamageSource pDamageSource) {
		return EnchantmentHelper.modifyDamage(player.serverLevel(), player.getWeaponItem(), pEntity, pDamageSource, pDamage);
	}

	@Override
	public void handle(IPayloadContext context) {
		Player player = context.player();
		Entity entity = player.level().getEntity(entityId);
		if (entity != null) {
			if (player.getOffhandItem().getItem() instanceof IExtendedReach) {
				IExtendedReach theExtendedReachWeapon = (IExtendedReach) player.getOffhandItem().getItem();
				double distanceSq = player.distanceToSqr(entity);
				float reach = Math.max(5,theExtendedReachWeapon.getReach());
				double reachSq = reach * reach;
				if (reachSq >= distanceSq) {
					attackTargetEntityWithOffhandItem((ServerPlayer) player, entity);
				}
			}
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
