package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.IWorldCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.lib.Party.Member;
import online.kingdomkeys.kingdomkeys.network.PacketHandler;
import online.kingdomkeys.kingdomkeys.network.stc.SCRecalculateEyeHeight;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class GraviraEntity extends ThrowableProjectile {

	int maxTicks = 100;
	float dmgMult = 1;
	
	public GraviraEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public GraviraEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_GRAVIRA.get(), world);
	}

	public GraviraEntity(Level world) {
		super(ModEntities.TYPE_GRAVIRA.get(), world);
		this.blocksBuilding = true;
	}

	public GraviraEntity(Level world, Player player, float dmgMult) {
		super(ModEntities.TYPE_GRAVIRA.get(), player, world);
		this.dmgMult = dmgMult;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravity() {
		return 0F;
	}

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}

		if (tickCount > 2)
			level.addParticle(ParticleTypes.DRAGON_BREATH, getX(), getY(), getZ(), 0, 0, 0);

		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		if (!level.isClientSide) {
			float radius = 2.5F;
			double X = getX();
			double Y = getY();
			double Z = getZ();
	
			for (int t = 1; t < 360; t += 20) {
				for (int s = 1; s < 360 ; s += 20) {
					double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double y = Y + (radius * Math.cos(Math.toRadians(t)));
					((ServerLevel) level).sendParticles(ParticleTypes.DRAGON_BREATH, x, y+1, z, 1, 0,0,0, 0);
				}
			}
			
			IWorldCapabilities worldData = ModCapabilities.getWorld(level);
			if (!level.isClientSide && getOwner() != null && worldData != null) {
				List<Entity> list = level.getEntities(getOwner(), getBoundingBox().inflate(radius));
				Party casterParty = worldData.getPartyFromMember(getOwner().getUUID());
	
				if(casterParty != null && !casterParty.getFriendlyFire()) {
					for(Member m : casterParty.getMembers()) {
						list.remove(level.getPlayerByUUID(m.getUUID()));
					}
				} else {
					list.remove(getOwner());
				}
				
				if (!list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						Entity e = (Entity) list.get(i);
						if (e instanceof LivingEntity) {
							IGlobalCapabilities globalData = ModCapabilities.getGlobal((LivingEntity) e);
							globalData.setFlatTicks(100);
							
							if(Utils.isHostile(e)) {
								float dmg = this.getOwner() instanceof Player ? ((LivingEntity)e).getMaxHealth() * DamageCalculation.getMagicDamage((Player) this.getOwner()) / 100 : 2;
								dmg = Math.min(dmg, 99);
								e.hurt(DamageSource.thrown(this, this.getOwner()), dmg * dmgMult);
							}
							if (e instanceof LivingEntity)
								PacketHandler.syncToAllAround((LivingEntity) e, globalData);
	
							if(e instanceof ServerPlayer)
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
	protected void defineSynchedData() {
		// TODO Auto-generated method stub

	}
}
