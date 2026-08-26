package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.MagicTargetEntity;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.TrainingDummyEntity;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCRecalculateEyeHeight;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class GravigaEntity extends BaseMagicProjectile {

	float dmgMult = 1;

	public GravigaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public GravigaEntity(Level world, LivingEntity player, float dmgMult) {
		super(ModEntities.TYPE_GRAVIGA.get(), player, world);
		this.dmgMult = dmgMult;
		setDamageType(KKDamageTypes.DARKNESS);
	}

	@Override
	protected double getDefaultGravity() {
		return 0;
	}

	@Override
	public void tick() {
		if (tickCount > 2)
			level().addParticle(ParticleTypes.DRAGON_BREATH, getX(), getY(), getZ(), 0, 0, 0);

		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		super.onHit(rtRes);
		if (!level().isClientSide) {
			float radius = 3F;
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

			WorldData worldData = WorldData.get(level().getServer());
			if (!level().isClientSide && getOwner() != null && worldData != null) {
				List<Entity> oList = level().getEntities(getOwner(), getBoundingBox().inflate(radius));
				List<Entity> list = Utils.removePartyMembersFromList((Player) getOwner(),oList);

				if (!list.isEmpty()) {
					for (Entity e : list) {
						if (e instanceof LivingEntity le) {
							MobEffectInstance instance = new MobEffectInstance(ModMobEffects.GRAVITY, 100, 2, false, false, false);
							le.addEffect(instance);
							e.level().getServer().getPlayerList().getPlayers().forEach(player -> {
								player.connection.send(new ClientboundUpdateMobEffectPacket(le.getId(), instance, false));
							});
							if (Utils.isHostile(e) || e instanceof TrainingDummyEntity || e instanceof MagicTargetEntity) {
								float ratio = dmgMult * (this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) / 100 : 2);
								float dmg = le.getHealth() * ratio;
								dmg = Math.min(dmg, 99);
								e.hurt(KKDamageTypes.getElementalDamage(KKDamageTypes.DARKNESS,this, this.getOwner()), dmg);
							}

							if (e instanceof ServerPlayer)
								PacketHandler.sendTo(new SCRecalculateEyeHeight(), (ServerPlayer) e);
						}
					}
				}
				remove(RemovalReason.KILLED);
			}
		}
	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		// compound.putInt("lvl", this.getLvl());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		// this.setLvl(compound.getInt("lvl"));
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {

	}
}