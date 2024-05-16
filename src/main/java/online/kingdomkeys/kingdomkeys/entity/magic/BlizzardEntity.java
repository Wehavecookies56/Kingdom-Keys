package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
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
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.damagesource.IceDamageSource;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;

public class BlizzardEntity extends ThrowableProjectile {

	int maxTicks = 120;
	float dmgMult = 1;

	public BlizzardEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public BlizzardEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
		super(ModEntities.TYPE_BLIZZARD.get(), world);
	}

	public BlizzardEntity(Level world, LivingEntity player, float dmgMult) {
		super(ModEntities.TYPE_BLIZZARD.get(), player, world);
		this.dmgMult = dmgMult;
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket() {
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
		if(ModConfigs.blizzardChangeBlocks && !level().isClientSide) {
			if (level().getBlockState(blockPosition()) == Blocks.WATER.defaultBlockState()) {
				level().setBlockAndUpdate(blockPosition(), Blocks.ICE.defaultBlockState());
				remove(RemovalReason.KILLED);
			} else if(level().getBlockState(blockPosition()) == Blocks.LAVA.defaultBlockState()){
				level().setBlockAndUpdate(blockPosition(), Blocks.OBSIDIAN.defaultBlockState());
				remove(RemovalReason.KILLED);
			}
		}

		if (tickCount > 2)
			level().addParticle(ParticleTypes.CLOUD, getX(), getY(), getZ(), 0, 0, 0);

		super.tick();
	}

	@Override
	protected void onHit(HitResult rtRes) {
		if (!level().isClientSide) {
			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity target) {
                if (target.isOnFire()) {
					target.clearFire();
				} else {
					if (target != getOwner()) {
						Party p = null;
						if (getOwner() != null) {
							p = ModCapabilities.getWorld(getOwner().level()).getPartyFromMember(getOwner().getUUID());
						}
						if (p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { // If caster is not in a party || the party doesn't have the target in it || the party has FF on
							float dmg = this.getOwner() instanceof Player player ? DamageCalculation.getMagicDamage(player) * 0.3F : 2;
							target.hurt(IceDamageSource.getIceDamage(this, this.getOwner()), dmg * dmgMult);
						}
					}
				}
			}
			
			if (brtResult != null) {
				BlockPos blockpos = brtResult.getBlockPos();
				BlockState blockstate = level().getBlockState(blockpos);
				if(blockstate.hasProperty(BlockStateProperties.LIT))
					level().setBlock(blockpos, blockstate.setValue(BlockStateProperties.LIT, false), 11);

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
	protected void defineSynchedData() {
		// TODO Auto-generated method stub

	}
}
