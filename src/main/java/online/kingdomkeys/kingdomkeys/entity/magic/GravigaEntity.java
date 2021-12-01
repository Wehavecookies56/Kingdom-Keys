package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.fmllegacy.network.FMLPlayMessages;
import net.minecraftforge.network.NetworkHooks;
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

public class GravigaEntity extends ThrowableProjectile {

	int maxTicks = 100;
	float dmgMult = 1;

	public GravigaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public GravigaEntity(FMLPlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_GRAVIGA.get(), world);
	}

	public GravigaEntity(Level world) {
		super(ModEntities.TYPE_GRAVIGA.get(), world);
		this.blocksBuilding = true;
	}

	public GravigaEntity(Level world, Player player, float dmgMult) {
		super(ModEntities.TYPE_GRAVIGA.get(), player, world);
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
			this.remove(false);
		}

		if (tickCount > 2)
			level.addParticle(ParticleTypes.DRAGON_BREATH, getX(), getY(), getZ(), 0, 0, 0);

		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		float radius = 3F;
		double X = getX();
		double Y = getY();
		double Z = getZ();

		for (int t = 1; t < 360; t += 20) {
			for (int s = 1; s < 360 ; s += 20) {
				double x = X + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
				double z = Z + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
				double y = Y + (radius * Math.cos(Math.toRadians(t)));
				level.addParticle(ParticleTypes.DRAGON_BREATH, x, y, z, 0, -0.05, 0);
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
							float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) * 0.4F : 2;
							e.hurt(DamageSource.thrown(this, this.getOwner()), dmg * dmgMult);
						}
						if (e instanceof LivingEntity)
							PacketHandler.syncToAllAround((LivingEntity) e, globalData);

						if(e instanceof ServerPlayer)
							PacketHandler.sendTo(new SCRecalculateEyeHeight(), (ServerPlayer) e);
					}
				}
			}
			this.remove(false);
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
