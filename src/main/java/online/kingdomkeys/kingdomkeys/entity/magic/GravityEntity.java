package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.MagicTargetEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.TrainingDummyEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCRecalculateEyeHeight;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.function.Supplier;

// Gravity, Gravira and Graviga: a sphere that weighs down and takes a share of the health of those in it
public class GravityEntity extends BaseMagicProjectile {

	public enum Tier {
		GRAVITY(ModEntities.TYPE_GRAVITY::get, 2F, 0, 1F, 2F, true),
		GRAVIRA(ModEntities.TYPE_GRAVIRA::get, 2.5F, 1, 1.1F, 2.3F, false),
		GRAVIGA(ModEntities.TYPE_GRAVIGA::get, 3F, 2, 1.2F, 2.6F, false);

		final Supplier<EntityType<GravityEntity>> type;
		final float radius;
		final int amplifier;
		public final float damageMult, speed;
		// Gravity caps the share of health before the multiplier, the others after: kept as each always was
		final boolean capFirst;

		Tier(Supplier<EntityType<GravityEntity>> type, float radius, int amplifier, float damageMult, float speed, boolean capFirst) {
			this.type = type;
			this.radius = radius;
			this.amplifier = amplifier;
			this.damageMult = damageMult;
			this.speed = speed;
			this.capFirst = capFirst;
		}

		static Tier of(EntityType<?> type) {
			for (Tier tier : values()) {
				if (tier.type.get() == type) {
					return tier;
				}
			}
			return GRAVITY;
		}
	}

	final Tier tier;

	public GravityEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.tier = Tier.of(type);
	}

	public GravityEntity(Level world, LivingEntity player, float dmgMult, Tier tier) {
		super(tier.type.get(), player, world);
		this.tier = tier;
		this.dmgMult = dmgMult;
		setDamageType(KKDamageTypes.DARKNESS);
	}

	@Override
	public void tick() {
		if (tickCount > 2)
			level().addParticle(ParticleTypes.DRAGON_BREATH, getX(), getY(), getZ(), 0, 0, 0);

		super.tick();
	}

	@Override
	protected void onImpact(HitResult hit) {
		float radius = tier.radius;
		double X = getX();
		double Y = getY();
		double Z = getZ();

		for (int t = 1; t < 360; t += 20) {
			double radT = Math.toRadians(t);
			double sinT = Math.sin(radT);
			double y = Y + (radius * Math.cos(radT));
			for (int s = 1; s < 360 ; s += 20) {
				double radS = Math.toRadians(s);
				double x = X + (radius * Math.cos(radS) * sinT);
				double z = Z + (radius * Math.sin(radS) * sinT);
				((ServerLevel) level()).sendParticles(ParticleTypes.DRAGON_BREATH, x, y+1, z, 1, 0,0,0, 0);
			}
		}

		for (LivingEntity e : targetsAround(radius, null)) {
			MobEffectInstance instance = new MobEffectInstance(ModMobEffects.GRAVITY, 100, tier.amplifier, false, false, false);
			e.addEffect(instance);
			e.level().getServer().getPlayerList().getPlayers().forEach(player -> {
				player.connection.send(new ClientboundUpdateMobEffectPacket(e.getId(), instance, false));
			});
			if (Utils.isHostile(e) || e instanceof TrainingDummyEntity || e instanceof MagicTargetEntity) {
				e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS,this, this.getOwner()), healthShare(e));
			}

			if (e instanceof ServerPlayer)
				PacketHandler.sendTo(new SCRecalculateEyeHeight(), (ServerPlayer) e);
		}
		remove(RemovalReason.KILLED);
	}

	private float healthShare(LivingEntity e) {
		if (tier.capFirst) {
			float dmg = this.getOwner() instanceof LivingEntity magicCaster ? e.getHealth() * DamageCalculation.getMagicDamage(magicCaster) / 100 : 2;
			return Math.min(dmg, 99) * dmgMult;
		}
		float ratio = dmgMult * (this.getOwner() instanceof LivingEntity magicCaster ? DamageCalculation.getMagicDamage(magicCaster) / 100 : 2);
		return Math.min(e.getHealth() * ratio, 99);
	}

}
