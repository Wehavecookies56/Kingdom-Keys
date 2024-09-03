package online.kingdomkeys.kingdomkeys.entity.magic;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import online.kingdomkeys.kingdomkeys.damagesource.FireDamageSource;
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class FirazaEntity extends ThrowableProjectile {

	int maxTicks = 100;
	float dmgMult = 1;

	public FirazaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public FirazaEntity(Level world, LivingEntity player, float dmgMult) {
		super(ModEntities.TYPE_FIRAZA.get(), player, world);
		this.dmgMult = dmgMult;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity pEntity) {
		return super.getAddEntityPacket(pEntity);
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

		//world.addParticle(ParticleTypes.ENTITY_EFFECT, getPosX(), getPosY(), getPosZ(), 1, 1, 0);
		if(tickCount > 2) {
			float radius = 1F;
			for (int t = 1; t < 360; t += 30) {
				for (int s = 1; s < 360 ; s += 30) {
					double x = getX() + (radius * Math.cos(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double z = getZ() + (radius * Math.sin(Math.toRadians(s)) * Math.sin(Math.toRadians(t)));
					double y = getY() + (radius * Math.cos(Math.toRadians(t)));
					level().addParticle(ParticleTypes.FLAME, x, y, z, 0, 0, 0);
				}
			}
		}
		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		if (!level().isClientSide && getOwner() != null) {
			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) ertResult.getEntity();

				if (target != getOwner()) {
					Party p = null;
					if (getOwner() != null) {
						p = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
					}
					if(p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { //If caster is not in a party || the party doesn't have the target in it || the party has FF on
						target.setRemainingFireTicks(30);
						float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) : 2;
						target.hurt(FireDamageSource.getFireDamage(this, this.getOwner()), dmg * dmgMult);
					}
				}
			}
			
			float radius = 6F;
			
			if (brtResult != null) {
				BlockPos ogBlockPos = brtResult.getBlockPos();

				for(int x=(int)(ogBlockPos.getX()-radius);x<ogBlockPos.getX()+radius;x++) {
					for(int y=(int)(ogBlockPos.getY()-radius);y<ogBlockPos.getY()+radius;y++) {
						for(int z=(int)(ogBlockPos.getZ()-radius);z<ogBlockPos.getZ()+radius;z++) {
							BlockPos blockpos = new BlockPos(x,y,z);
							BlockState blockstate = level().getBlockState(blockpos);
							if(blockstate.getBlock() == Blocks.WET_SPONGE) {
								level().setBlockAndUpdate(blockpos, Blocks.SPONGE.defaultBlockState());
							}
							if(blockstate.hasProperty(BlockStateProperties.LIT)) {
								level().setBlock(blockpos, blockstate.setValue(BlockStateProperties.LIT, true), 11);
							}
						}
					}
				}
			}
			
			if(getOwner() instanceof Player) {
				List<LivingEntity> list = Utils.getLivingEntitiesInRadiusExcludingParty((Player) getOwner(), radius);
	
				((ServerLevel)level()).sendParticles(ParticleTypes.FLAME, getX(), getY(), getZ(), 1000, Math.random() - 0.5D, Math.random() - 0.5D, Math.random() - 0.5D, 0.3);
				
				if (!list.isEmpty()) {
					for (int i = 0; i < list.size(); i++) {
						Entity e = (Entity) list.get(i);
						if (e instanceof LivingEntity) {
							e.setRemainingFireTicks(25);
							float dmg = this.getOwner() instanceof Player ? DamageCalculation.getMagicDamage((Player) this.getOwner()) * 0.8F : 2;
							e.hurt(FireDamageSource.getFireDamage(this, this.getOwner()), dmg * dmgMult);
						}
					}
				}
			}
			remove(RemovalReason.KILLED);
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
