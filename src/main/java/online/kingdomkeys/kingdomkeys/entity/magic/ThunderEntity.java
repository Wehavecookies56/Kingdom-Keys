package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.ProjectileHits;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

// Thunder, Thundara, Thundaga and Thundaza: the same storm of bolts, only bigger, longer and faster each tier
public class ThunderEntity extends BaseMagicProjectile {

	public enum Tier {
		THUNDER(ModEntities.TYPE_THUNDER::get, 20, 2F, 9, 0.1F),
		THUNDARA(ModEntities.TYPE_THUNDARA::get, 35, 3F, 7, 0.25F),
		THUNDAGA(ModEntities.TYPE_THUNDAGA::get, 40, 4F, 6, 0.4F),
		THUNDAZA(ModEntities.TYPE_THUNDAZA::get, 45, 6F, 2, 0.5F);

		final Supplier<EntityType<ThunderEntity>> type;
		final int maxTicks;
		final float radius;
		// A bolt every this many ticks
		final int interval;
		// What a bolt that finds no one to strike still deals
		final float minDamage;

		Tier(Supplier<EntityType<ThunderEntity>> type, int maxTicks, float radius, int interval, float minDamage) {
			this.type = type;
			this.maxTicks = maxTicks;
			this.radius = radius;
			this.interval = interval;
			this.minDamage = minDamage;
		}

		static Tier of(EntityType<?> type) {
			for (Tier tier : values()) {
				if (tier.type.get() == type) {
					return tier;
				}
			}
			return THUNDER;
		}
	}

	final Tier tier;
	List<LivingEntity> list = new ArrayList<>();

	public ThunderEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.tier = Tier.of(type);
	}

	public ThunderEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockedOnEntity, Tier tier) {
		super(tier.type.get(), player, world);
		this.tier = tier;
		this.dmgMult = dmgMult;
		this.lockOnEntity = lockedOnEntity;
		setDamageType(KKDamageTypes.LIGHTNING);
	}

	@Override
	public void tick() {
		if (this.tickCount > tier.maxTicks) {
			this.remove(RemovalReason.KILLED);
		}

		if (getOwner() == null) {
			remove(RemovalReason.KILLED);
			return;
		}

		float radius = tier.radius;

		if (!level().isClientSide) { // Only calculate and spawn lightning bolts server side
			if (tickCount == 1) {
				if(getOwner() instanceof LivingEntity p) {
					if (lockOnEntity != null) {
						list = ProjectileHits.around(lockOnEntity, p, radius, null);
					} else {
						list = ProjectileHits.around(p, p, radius, null);
					}
				}
			}

			if (tickCount % tier.interval == 1) {
				if (!list.isEmpty()) { // find random entity
					int i = level().random.nextInt(list.size());
					Entity e = list.get(i);
					if(!e.isAlive()) {
						list.remove(e);
					}
					float dmg = getTotalDamage();

					ThunderBoltEntity shot = new ThunderBoltEntity(getOwner().level(), (LivingEntity) getOwner(), e.getX(), e.getY(), e.getZ(), dmg);
					level().addFreshEntity(shot);

					LightningBolt lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(this.level());
					lightningBoltEntity.setVisualOnly(true);
					lightningBoltEntity.moveTo(Vec3.atBottomCenterOf(e.blockPosition()));
					lightningBoltEntity.setCause(getOwner() instanceof ServerPlayer p ? p : null);
					this.level().addFreshEntity(lightningBoltEntity);
				} else {
					int x,z;
					if(lockOnEntity != null) {
						x = (int) lockOnEntity.getX();
						z = (int) lockOnEntity.getZ();
					} else {
						x = (int) getOwner().getX();
						z = (int) getOwner().getZ();
					}

					int posX = (int) (x + getOwner().level().random.nextInt((int) (radius*2)) - radius / 2)-1;
					int posZ = (int) (z + getOwner().level().random.nextInt((int) (radius*2)) - radius / 2)-1;

					int y = Utils.getYHeight(level(),posX,posZ);

					for(int px=(int)(x-radius);px<x+radius;px++) {
						for(int py=(int)(y-radius);py<y+radius;py++) {
							for(int pz=(int)(z-radius);pz<z+radius;pz++) {
								BlockPos blockpos = new BlockPos(px,py,pz);
								BlockState blockstate = level().getBlockState(blockpos);
								if(blockstate.getBlock() == Blocks.LIGHTNING_ROD) {
									posX = px;
									posZ = pz;
								}
							}
						}
					}

					float dmg = Math.max(tier.minDamage, getTotalDamage());
					ThunderBoltEntity shot = new ThunderBoltEntity(getOwner().level(), (LivingEntity) getOwner(), posX, Utils.getYHeight(level(),posX,posZ), posZ, dmg);
					level().addFreshEntity(shot);

					BlockPos pos = Utils.getBlockPosYHeight(level(),posX,posZ);
					LightningBolt lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(this.level());
					lightningBoltEntity.moveTo(Vec3.atBottomCenterOf(pos));
					lightningBoltEntity.setVisualOnly(true);
					lightningBoltEntity.setCause(getOwner() instanceof ServerPlayer p ? p : null);
					this.level().addFreshEntity(lightningBoltEntity);
				}
			}
		}

		super.tick();
	}

	@Override
	protected void onHit(HitResult result) {

	}

}
