package online.kingdomkeys.kingdomkeys.entity.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import online.kingdomkeys.kingdomkeys.data.WorldData;
import online.kingdomkeys.kingdomkeys.effects.ModMobEffects;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.lib.Party;
import online.kingdomkeys.kingdomkeys.util.Utils;

import java.util.List;

public class CrawlingFiragaEntity extends FiragaEntity {
	public CrawlingFiragaEntity(EntityType<? extends ThrowableProjectile> type, Level world) {
		super(type, world);
		this.blocksBuilding = true;
	}

	public CrawlingFiragaEntity(Level world, LivingEntity player, float dmgMult, LivingEntity lockOnEntity) {
		super(ModEntities.TYPE_CRAWLINGFIRAGA.get(), world, player, dmgMult, lockOnEntity);
	}

	@Override
	protected void onHit(HitResult rtRes) {
		//super.onHit(rtRes);
		if (!level().isClientSide && getOwner() != null) {
			EntityHitResult ertResult = null;
			BlockHitResult brtResult = null;

			if (rtRes instanceof EntityHitResult) {
				ertResult = (EntityHitResult) rtRes;
			}

			if (rtRes instanceof BlockHitResult) {
				brtResult = (BlockHitResult) rtRes;
			}

			LivingEntity target = null;
			if(ertResult != null && ertResult.getEntity() instanceof LivingEntity t){
				target = t;
			}

			if (target != null) {
                if (target != getOwner()) {
					if (target.getEffect(ModMobEffects.FREEZE) != null) {
						target.removeEffect(ModMobEffects.FREEZE);
					}
					Party p = null;
					if (getOwner() != null) {
						p = WorldData.get(getOwner().getServer()).getPartyFromMember(getOwner().getUUID());
					}
					if(p == null || (p.getMember(target.getUUID()) == null || p.getFriendlyFire())) { //If caster is not in a party || the party doesn't have the target in it || the party has FF on
						target.setRemainingFireTicks(15);
						damageEntity(target);
						target.invulnerableTime = 10;
						level().playSound(null, position().x(), position().y(), position().z(), SoundEvents.GHAST_SHOOT, SoundSource.PLAYERS, 1F, 1F);
					}
				}
				return;
			}

			super.onHit(rtRes);
			float radius = 1.5F;
			
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
							if(blockstate.hasProperty(BlockStateProperties.LIT))
								level().setBlock(blockpos, blockstate.setValue(BlockStateProperties.LIT, true), 11);
						}
					}
				}
			}
			
			List<Entity> list = level().getEntities(getOwner(), getBoundingBox().inflate(radius));
			list = Utils.removePartyMembersFromList((Player)getOwner(), list);
			if(target != null) { //If was direct impact remove the target from the explosion damage
				list.remove(target);
			}

			for(SimpleParticleType p : getParticles()) {
				((ServerLevel)level()).sendParticles(p, getX(), getY(), getZ(), 200/getParticles().size(), Math.random() - 0.5D, Math.random() - 0.5D, Math.random() - 0.5D,0.1);
			}

			if (!list.isEmpty()) {
                for (Entity e : list) {
                    if (e instanceof LivingEntity ent) {
                        e.setRemainingFireTicks(15);
						damageEntity(ent);

						if (ent.getEffect(ModMobEffects.FREEZE) != null) {
							ent.removeEffect(ModMobEffects.FREEZE);
						}
                    }
                }
			}

			remove(RemovalReason.KILLED);
		}
	}
}
