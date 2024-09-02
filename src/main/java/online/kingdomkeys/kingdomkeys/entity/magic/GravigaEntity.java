package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.data.GlobalData;
import online.kingdomkeys.kingdomkeys.data.ModData;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCRecalculateEyeHeight;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class GravigaEntity extends ThrowableProjectile {

	int maxTicks = 100;
	float dmgMult = 1;

	public GravigaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public GravigaEntity(Level world, Player player, float dmgMult) {
		super(ModEntities.TYPE_GRAVIGA.get(), player, world);
		this.dmgMult = dmgMult;
	}

	@Override
	protected double getDefaultGravity() {
		return 0;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}

		if (tickCount > 2)
			level().addParticle(ParticleTypes.DRAGON_BREATH, getX(), getY(), getZ(), 0, 0, 0);

		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		if (!level().isClientSide) {
			float radius = 3F;
			double X = getX();
			double Y = getY();
			double Z = getZ();
	
			for (int t = 1; t < 360; t += 20) {
				for (int s = 1; s < 360 ; s += 20) {
					double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double y = Y + (radius * Math.cos(Math.toRadians(t)));
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
                            GlobalData globalData = GlobalData.get(le);
                            globalData.setFlatTicks(100);

                            if (Utils.isHostile(e)) {
                                float dmg = this.getOwner() instanceof Player player ? le.getMaxHealth() * DamageCalculation.getMagicDamage(player) / 100 : 2;
                                dmg = Math.min(dmg, 99);
                                e.hurt(e.damageSources().thrown(this, this.getOwner()), dmg * dmgMult);
                            }
                            if (e instanceof LivingEntity)
                                PacketHandler.syncToAllAround(le, globalData);

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
