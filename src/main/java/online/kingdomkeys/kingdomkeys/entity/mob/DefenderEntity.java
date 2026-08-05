package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.DefenderGoal;

public class DefenderEntity extends BaseKHEntity {
	// Defense while shield is up
	private static final float GUARD_DAMAGE = 0.25F;

	public DefenderEntity(EntityType<? extends Monster> type, Level worldIn) {
		super(type, worldIn);
		xpReward = 14;
	}

	@OnlyIn(Dist.CLIENT)
	public ResourceLocation getTexture() {
		return KingdomKeys.rl("textures/entity/mob/defender.png");
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
		this.targetSelector.addGoal(4, new DefenderGoal(this));
	}

	public static AttributeSupplier.Builder registerAttributes() {
		// Slow and heavy: it's meant to be walked around rather than traded with.
		return Mob.createLivingAttributes()
				.add(Attributes.FOLLOW_RANGE, 20.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.22D)
				.add(Attributes.MAX_HEALTH, 120.0D)
				.add(Attributes.ATTACK_DAMAGE, 6.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 1.5D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (GlobalData.get(this) == null) {
			KingdomKeys.LOGGER.warn("For some reason " + this + " doesn't have globaldata");
			return super.hurt(source, amount);
		}

		if (source.getEntity() instanceof LivingEntity && !source.is(KKDamageTypes.STOP)) {
			Entity attacker = source.getDirectEntity();

			if (attacker != null) {
				double dx = attacker.getX() - this.getX();
				double dz = attacker.getZ() - this.getZ();
				// Global degree the attack is coming from.
				float attackYaw = (float) Math.toDegrees(Mth.atan2(dz, dx));
				float diff = Mth.wrapDegrees(attackYaw - getYRot());

				if (diff > 30 && diff < 150 && !this.hasEffect(ModMobEffects.GRAVITY) && attacker instanceof LivingEntity living) {
					living.knockback(0.8F, -dx, -dz);
					level().playSound(null, blockPosition(), ModSounds.invincible_hit.get(), SoundSource.PLAYERS, 1F, 1F);
					attacker.setDeltaMovement(attacker.getDeltaMovement().x, 0.5F, attacker.getDeltaMovement().z);
					return false;
				}
			}
		}

		if (getState() == DefenderGoal.STATE_GUARD) {
			amount *= GUARD_DAMAGE;
		}
		return super.hurt(source, amount);
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 2;
	}

	@Override
	public EntityHelper.MobType getKHMobType() {
		return EntityHelper.MobType.HEARTLESS_EMBLEM;
	}
}
