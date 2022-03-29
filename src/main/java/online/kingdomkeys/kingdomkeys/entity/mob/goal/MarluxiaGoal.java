package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import java.util.List;
import java.util.Random;

import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import online.kingdomkeys.kingdomkeys.client.sound.ModSounds;
import online.kingdomkeys.kingdomkeys.config.ModConfigs;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.MarluxiaEntity;

public class MarluxiaGoal extends TargetGoal {
	// 0-Normal, 1-Armor (weak to fire), 2-Teleporting, 3-Chasing (finish)

	private final int MAX_ARMOR_TICKS = 30 * 20, MAX_ARMOR_USES = 3;
	private int armorTicks = 0, armorUses = 0;
	private final int MAX_TP_TICKS = 80;
	private int tpTicks = 0;
	public int chasingTicks = 0, chasedTimes = 0;
	
	private int ticksToChooseAI = 0; //Ticks in base state after an attack happened
	
	public MarluxiaGoal(PathfinderMob creature) {
		super(creature, true);
		ticksToChooseAI = 200;
	}
	
	double posX, posY, posZ;

	@Override
	public boolean canContinueToUse() {
		System.out.println(mob.tickCount);
		if(mob.tickCount < 100) {
			mob.setDeltaMovement(0,0,0);
			mob.setInvulnerable(true);
			mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0);
		}

		if(mob.tickCount == 100) {
			mob.setInvulnerable(false);
			mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(11);
		}
		
		if (this.mob.getTarget() != null) {
			//Set AI to use
			if(ticksToChooseAI <= 0 && EntityHelper.getState(mob) == 0) {
				int n = mob.level.random.nextInt()*100;
				if(n < 50) { // Armored?
					if(mob.getHealth() < mob.getMaxHealth() * 0.80 && !isArmored()) {
						useArmor((MarluxiaEntity) mob);
						ticksToChooseAI = 200;
					} else {
						ticksToChooseAI = 100; //If can't use the armor wait 100 ticks
					}
				} else { //TP
					useTP((MarluxiaEntity) mob);
					ticksToChooseAI = 150;
				}
			} else {
				if(EntityHelper.getState(mob) == 0) {
					ticksToChooseAI--;
				}
			}

			if(isArmored()) {
				armoredAI();
			}

			if(isTeleporting()) {
				teleportAI();
			}
						
			if(isChasing()) {
				chasingAI();
			}
			
			return true;
		} else { //If no target
			if(ModConfigs.bossDespawnIfNoTarget) {
				mob.remove(Entity.RemovalReason.KILLED);
			} else {
				if(mob.isNoGravity())
					mob.setNoGravity(false);
				if(EntityHelper.getState(mob) == 0) {
					EntityHelper.setState(mob, 0);
				}
			}
		}
		
		return false;
	}

	private void teleportAI() {
		mob.setNoGravity(true);
		if(tpTicks % 10 == 0) {
			attackWithTP();
		}
		if(tpTicks > MAX_TP_TICKS) {
			mob.setNoGravity(false);
			EntityHelper.setState(mob, 0);

		}
		tpTicks++;
	}

	private void armoredAI() {
		if(armorTicks <= 40) {
			mob.setDeltaMovement(0, 0.2, 0);
			mob.setInvulnerable(true);
		} else if(armorTicks < 45) {
			mob.setDeltaMovement(0, -100, 0);
		}
		
		if(armorTicks == 48) {
			posX = mob.getX();
			posY = mob.getY();
			posZ = mob.getZ();
			mob.setInvulnerable(false);
		}
		
		if(armorTicks > 48 && armorTicks < 70) {
			double r = (armorTicks - 48) * 0.7;
            for (int a = 1; a <= 360; a += 7) {
                double x = posX + (r * Math.cos(Math.toRadians(a)));
                double z = posZ + (r * Math.sin(Math.toRadians(a)));
                ((ServerLevel)mob.level).sendParticles(new DustParticleOptions(new Vector3f(1F,0.9F,0.9F),1F), x, posY + 0.2D, z,1, 0.0D, 0.0D, 0.0D,0);
            }
            
            AABB aabb = new AABB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).inflate(r, 0, r);
    		List<LivingEntity> list = mob.level.getEntitiesOfClass(LivingEntity.class, aabb);
    		list.remove(mob);
    		
            for(LivingEntity enemy : list) {
				mob.doHurtTarget(enemy);
			}
            
		}
		
		armorTicks++;
		if(armorTicks >= MAX_ARMOR_TICKS) {
			removeArmor((MarluxiaEntity) mob);
			armorTicks = 0;
		}
	}
	
	private void chasingAI() {
		if(chasingTicks <= 40) {
			mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0);
			mob.setDeltaMovement(0, 0.2, 0);
			mob.setInvulnerable(true);
		} else if(chasingTicks < 300) {
			mob.absMoveTo(mob.getTarget().getX(), mob.getTarget().getY(), mob.getTarget().getZ(), mob.getTarget().getYRot(), mob.getTarget().getYRot());
			//goalOwner.faceEntity(goalOwner.getAttackTarget(), 0, 0);
			Random rand = ((ServerLevel) mob.level).random;
			((ServerLevel) mob.level).sendParticles(new DustParticleOptions(new Vector3f(1F, 0.6F, 0.6F), 1F), mob.getX() - 1 + rand.nextDouble() * 2, mob.getY(), mob.getZ() - 1 + rand.nextDouble() * 2, 10, 0.0D, 0.0D, 0.0D, 100);
			
			if(chasingTicks % 5 == 0) {
				int r = 1;
				double pX = mob.getTarget().getX() - 3 + rand.nextDouble() * 6;
				double pY = mob.getTarget().getY();
				double pZ = mob.getTarget().getZ() - 3 + rand.nextDouble() * 6;
				mob.level.playSound(null, new BlockPos(pX,pY,pZ), ModSounds.portal.get(), SoundSource.MASTER, 1, 1);

				for(double i=0;i<4;i=i+0.5) {
					for (int a = 1; a <= 360; a += 7) {
		                double x = pX + (r * Math.cos(Math.toRadians(a)));
		                double z = pZ + (r * Math.sin(Math.toRadians(a)));
						((ServerLevel) mob.level).sendParticles(new DustParticleOptions(new Vector3f(1F, 0.5F, 0.5F), 1F), x, pY + i, z, 1, 0.0D, 0.0D, 0.0D, 0);
		            }
				}
				
				AABB aabb = new AABB(pX, pY, pZ, pX + 1, pY + 1, pZ + 1).inflate(r, 4, r);
	    		List<LivingEntity> list = mob.level.getEntitiesOfClass(LivingEntity.class, aabb);
	    		list.remove(mob);
	    		
	            for(LivingEntity enemy : list) {
	            	enemy.hurt(DamageSource.MAGIC, 3);
				}						
			}
		} else if(chasingTicks > 300) {
			mob.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(11);
			useArmor((MarluxiaEntity) mob);
		}
		chasingTicks++;
	}

	private void attackWithTP() {
		Random rand = mob.level.random;
		for(int i=0;i<10;i++)
			((ServerLevel) mob.level).sendParticles(new DustParticleOptions(new Vector3f(1F, 0.6F, 0.6F), 1F), mob.getX() - 2 + rand.nextDouble() * 4, mob.getY() + rand.nextDouble() * 4, mob.getZ() - 2 + rand.nextDouble() * 4, 10, 0.0D, 0.0D, 0.0D, 100);
			
		mob.teleportTo(this.mob.getTarget().getX(), this.mob.getTarget().getY()+1, this.mob.getTarget().getZ());
		
		if(this.mob.getTarget().distanceTo(mob) <= 1) {
			if(this.mob.getTarget() instanceof Player)
				((Player)this.mob.getTarget()).travel(new Vec3(0,2,0));
			this.mob.getTarget().setDeltaMovement(0,1.2,0);
			mob.getTarget().hurt(DamageSource.MAGIC, 2);
		} else {
			EntityHelper.setState(mob, 0);
		}
	}

	public void useArmor(MarluxiaEntity entity) {
		if(armorUses < MAX_ARMOR_USES) {
			armorTicks = 0;
			EntityHelper.setState(entity, 1);
			entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 2000, 200));
			armorUses++;
		}
	}

	public void removeArmor(MarluxiaEntity entity) {
		EntityHelper.setState(entity, 0);
		entity.removeEffect(MobEffects.GLOWING);
	}
	
	public void useTP(MarluxiaEntity entity) {
		EntityHelper.setState(entity, 2);
		tpTicks = 0;
	}
	
	@Override
	public void start() {
		EntityHelper.setState(this.mob, 0);
		this.mob.setInvulnerable(false);
	}

	private boolean isArmored() {
		return EntityHelper.getState(this.mob) == 1;
	}
	
	private boolean isTeleporting() {
		return EntityHelper.getState(this.mob) == 2;
	}

	private boolean isChasing() {
		return EntityHelper.getState(this.mob) == 3;
	}
	
	@Override
	public boolean canUse() {
		return this.mob.getTarget() != null;
	}

}