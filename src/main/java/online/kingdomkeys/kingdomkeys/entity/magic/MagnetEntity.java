package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.MagicTargetEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.ProjectileHits;
import online.kingdomkeys.kingdomkeys.entity.TrainingDummyEntity;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;
import java.util.function.Supplier;

// Magnet, Magnera and Magnega: a field that pulls everything in, Magnera and Magnega also grinding at what it holds
public class MagnetEntity extends BaseMagicProjectile {

	public enum Tier {
		MAGNET(ModEntities.TYPE_MAGNET::get, 100, 3, 2F, 20, false, 1.1F, 2),
		MAGNERA(ModEntities.TYPE_MAGNERA::get, 130, 3, 2.5F, 25, true, 0.9F, 3),
		MAGNEGA(ModEntities.TYPE_MAGNEGA::get, 160, 5, 3F, 30, true, 0.7F, 4);

		final Supplier<EntityType<MagnetEntity>> type;
		final int maxTicks;
		// When it starts pulling
		final int startTick;
		final float radius;
		// How long it takes to grow to full size, and to shrink away at the end
		final int rampTicks;
		final boolean hurts;
		final float soundPitch;
		public final float renderScale;

		Tier(Supplier<EntityType<MagnetEntity>> type, int maxTicks, int startTick, float radius, int rampTicks, boolean hurts, float soundPitch, float renderScale) {
			this.type = type;
			this.maxTicks = maxTicks;
			this.startTick = startTick;
			this.radius = radius;
			this.rampTicks = rampTicks;
			this.hurts = hurts;
			this.soundPitch = soundPitch;
			this.renderScale = renderScale;
		}

		static Tier of(EntityType<?> type) {
			for (Tier tier : values()) {
				if (tier.type.get() == type) {
					return tier;
				}
			}
			return MAGNET;
		}
	}

	public final Tier tier;

	public MagnetEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.tier = Tier.of(type);
		this.maxTicks = tier.maxTicks;
	}

	public MagnetEntity(Level world, LivingEntity player, float dmgMult, Tier tier) {
		super(tier.type.get(), player, world);
		this.tier = tier;
		this.maxTicks = tier.maxTicks;
		this.dmgMult = dmgMult;
		setDamageType(KKDamageTypes.AIR);
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks || getOwner() == null) {
			this.remove(RemovalReason.KILLED);
		}

		if (level() == null || WorldData.get(level().getServer()) == null)
			return;

		if (tickCount >= tier.startTick) {
			float radius = tier.radius;
			if (tickCount < tier.rampTicks) {
				radius = tickCount / 10F;
			}
			if (tickCount > maxTicks - tier.rampTicks) {
				radius = (maxTicks - tickCount) / 10F;
			}

			this.setDeltaMovement(0, 0, 0);
			this.hurtMarked = true;

			List<Entity> list = ProjectileHits.entitiesAround(this, getOwner(), radius, radius * 2, radius);

			for (Entity e : list) {
				double d = e.getX() - getX();
				double d1 = e.getZ() - getZ();
				if (e.getY() < this.getY() - 0.5) {
					e.setDeltaMovement(0, 0.5F, 0);
				}
				e.setDeltaMovement(d * -0.1, e.getDeltaMovement().y, d1 * -0.1);

				if (tier.hurts) {
					e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.AIR, this, this.getOwner()), dmgMult);

					if (tickCount + 2 > maxTicks) {
						if (Utils.isHostile(e) || e instanceof TrainingDummyEntity || e instanceof MagicTargetEntity) {
							e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.AIR, this, this.getOwner()), dmgMult);
						}
						remove(RemovalReason.KILLED);
					}
				}
			}
		}

		if (tickCount == maxTicks - tier.rampTicks) {
			getOwner().level().playSound(null, getOwner().blockPosition(), ModSounds.magnet2.get(), SoundSource.PLAYERS, 1F, tier.soundPitch);
		}

		super.tick();
	}

}
