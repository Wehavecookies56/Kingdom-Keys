package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.DamageCalculation;
import online.kingdomkeys.kingdomkeys.lib.Party;

public class BlizzardEntity extends ThrowableEntity {

	int maxTicks = 120;

	public BlizzardEntity(EntityType<? extends ThrowableEntity> type, World world) {
		super(type, world);
		this.preventEntitySpawning = true;
	}

	public BlizzardEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_BLIZZARD.get(), world);
	}

	public BlizzardEntity(World world) {
		super(ModEntities.TYPE_BLIZZARD.get(), world);
		this.preventEntitySpawning = true;
	}

	public BlizzardEntity(World world, LivingEntity player) {
		super(ModEntities.TYPE_BLIZZARD.get(), player, world);
	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return NetworkHooks.getEntitySpawningPacket(this);
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

	@Override
	public void tick() {
		if (this.ticksExisted > maxTicks) {
			this.remove();
		}

		if (world.getBlockState(getPosition()).getBlockState() == Blocks.WATER.getDefaultState()) {
			world.setBlockState(getPosition(), Blocks.ICE.getDefaultState());
			remove();
		} else if(world.getBlockState(getPosition()).getBlockState() == Blocks.LAVA.getDefaultState()){
			world.setBlockState(getPosition(), Blocks.OBSIDIAN.getDefaultState());
			remove();
		}

		if (ticksExisted > 2)
			world.addParticle(ParticleTypes.CLOUD, getPosX(), getPosY(), getPosZ(), 0, 0, 0);

		super.tick();
	}

	@Override
	protected void onImpact(RayTraceResult rtRes) {
		if (!world.isRemote) {

			EntityRayTraceResult ertResult = null;
			BlockRayTraceResult brtResult = null;

			if (rtRes instanceof EntityRayTraceResult) {
				ertResult = (EntityRayTraceResult) rtRes;
			}

			if (rtRes instanceof BlockRayTraceResult) {
				brtResult = (BlockRayTraceResult) rtRes;
			}

			if (ertResult != null && ertResult.getEntity() instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) ertResult.getEntity();

				if (target.isBurning()) {
					target.extinguish();
				} else {
					if (target != func_234616_v_()) {
						Party p = null;
						if (func_234616_v_() != null) {
							p = ModCapabilities.getWorld(func_234616_v_().world).getPartyFromMember(func_234616_v_().getUniqueID());
						}
						if (p == null || (p.getMember(target.getUniqueID()) == null || p.getFriendlyFire())) { // If caster is not in a party || the party doesn't have the target in it || the party has FF on
							float dmg = this.func_234616_v_() instanceof PlayerEntity ? DamageCalculation.getMagicDamage((PlayerEntity) this.func_234616_v_(), 1) : 2;
							target.attackEntityFrom(DamageSource.causeThrownDamage(this, this.func_234616_v_()), dmg);
						}
					}
				}
			}
			remove();
		}

	}

	public int getMaxTicks() {
		return maxTicks;
	}

	public void setMaxTicks(int maxTicks) {
		this.maxTicks = maxTicks;
	}

	@Override
	public void writeAdditional(CompoundNBT compound) {
		// compound.putInt("lvl", this.getLvl());
	}

	@Override
	public void readAdditional(CompoundNBT compound) {
		// this.setLvl(compound.getInt("lvl"));
	}

	@Override
	protected void registerData() {
		// TODO Auto-generated method stub

	}
}
