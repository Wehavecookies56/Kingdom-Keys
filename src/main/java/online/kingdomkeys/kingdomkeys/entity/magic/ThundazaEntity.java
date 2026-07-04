package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.damagesource.KKDamageTypes;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class ThundazaEntity extends BaseMagicProjectile {
	int maxTicks = 45;
	LivingEntity lockedOnEntity;
	
	public ThundazaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
	}

	public ThundazaEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockedOnEntity) {
		super(ModEntities.TYPE_THUNDAZA.get(), player, world);
		this.dmgMult = dmgMult;
		this.lockedOnEntity = lockedOnEntity;
		setDamageType(KKDamageTypes.LIGHTNING);
	}

	List<LivingEntity> list = new ArrayList<>();

	@Override
	public void tick() {
		if (this.tickCount > maxTicks) {
			this.remove(RemovalReason.KILLED);
		}

		if (getOwner() == null) {
			remove(RemovalReason.KILLED);
			return;
		}
		
		float radius = 6.0F;

		if (!level().isClientSide && getOwner() != null) { // Only calculate and spawn lightning bolts server side
			if (tickCount == 1) {
				if(getOwner() instanceof Player p) {
					if (lockedOnEntity != null) {
						list = Utils.getLivingEntitiesInRadiusExcludingParty(p, lockedOnEntity, radius, radius, radius);
					} else {
						list = Utils.getLivingEntitiesInRadiusExcludingParty(p, radius);
					}
				}
				list.remove(this);
			}

			if (tickCount % 2 == 1) {
				if (!list.isEmpty()) { // find random entity
					int i = level().random.nextInt(list.size());
					Entity e = list.get(i);
					if (e instanceof LivingEntity) {
						if(!e.isAlive()) {
							list.remove(e);
						}
						float dmg = getTotalDamage();

						ThunderBoltEntity shot = new ThunderBoltEntity(getOwner().level(), (LivingEntity) getOwner(), e.getX(), e.getY(), e.getZ(), dmg);
						level().addFreshEntity(shot);

						LightningBolt lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(this.level());
						lightningBoltEntity.setVisualOnly(true);
						lightningBoltEntity.moveTo(Vec3.atBottomCenterOf(e.blockPosition()));
						lightningBoltEntity.setCause(getOwner() instanceof ServerPlayer ? (ServerPlayer) getOwner() : null);
						this.level().addFreshEntity(lightningBoltEntity);
					}
				} else {
					int x,z;
					if(lockedOnEntity != null) {
						x = (int) lockedOnEntity.getX();
						z = (int) lockedOnEntity.getZ();
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

					float dmg = getTotalDamage();
					dmg = Math.max(0.5F, dmg);
					ThunderBoltEntity shot = new ThunderBoltEntity(getOwner().level(), (LivingEntity) getOwner(), posX, Utils.getYHeight(level(),posX,posZ), posZ, dmg);
					level().addFreshEntity(shot);

					BlockPos pos = Utils.getBlockPosYHeight(level(),posX,posZ);
					LightningBolt lightningBoltEntity = EntityType.LIGHTNING_BOLT.create(this.level());
					lightningBoltEntity.moveTo(Vec3.atBottomCenterOf(pos));
					lightningBoltEntity.setVisualOnly(true);
					lightningBoltEntity.setCause(getOwner() instanceof ServerPlayer ? (ServerPlayer) getOwner() : null);
					this.level().addFreshEntity(lightningBoltEntity);
				}
			}
		}

		super.tick();
	}

	@Override
	protected void onHit(HitResult result) {
		// TODO Auto-generated method stub

	}

}
