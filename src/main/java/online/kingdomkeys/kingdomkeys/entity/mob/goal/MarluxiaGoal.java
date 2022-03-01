package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.mob.MarluxiaEntity;

public class MarluxiaGoal extends TargetGoal {
	// 0-Normal, 1-Armor (weak to fire), 2-Teleporting

	private final int MAX_ARMOR_TICKS = 30 * 20, MAX_ARMOR_USES = 3;
	private int armorTicks = 0, armorUses = 0;
	private final int MAX_TP_TICKS = 10 * 20, MAX_TPs = 3;
	private int tpTicks = 0, numOfTPs = 0;
	
	private int ticksToChooseAI = 0; //Ticks in base state after an attack happened
	
	public MarluxiaGoal(CreatureEntity creature) {
		super(creature, true);
		ticksToChooseAI = 200;
		//ticksUntilNextAttack = TIME_BEFORE_NEXT_ATTACK;
	}
	
	double posX, posY, posZ;

	@Override
	public boolean shouldContinueExecuting() {
		if (this.goalOwner.getAttackTarget() != null) {
			//Set AI to use
			if(ticksToChooseAI <= 0 && EntityHelper.getState(goalOwner) == 0) {
				if(goalOwner.world.rand.nextInt()*100 < 50) { // Armored?
					if(goalOwner.getHealth() < goalOwner.getMaxHealth() * 0.80 && !isArmored()) {
						useArmor((MarluxiaEntity) goalOwner);
						ticksToChooseAI = 200;
					} else {
						ticksToChooseAI = 100; //If can't use the armor wait 100 ticks
					}
				} else { //TP
					useTP((MarluxiaEntity) goalOwner);
					ticksToChooseAI = 150;
				}
			} else {
				if(EntityHelper.getState(goalOwner) == 0) {
					ticksToChooseAI--;
				}
			}
			
			System.out.println(ticksToChooseAI);

			if(isArmored()) {
				if(armorTicks <= 40) {
					goalOwner.setMotion(0, 0.2, 0);
					goalOwner.setInvulnerable(true);
				} else if(armorTicks < 45) {
					goalOwner.setMotion(0, -100, 0);
				}
				
				if(armorTicks == 48) {
					posX = goalOwner.getPosX();
					posY = goalOwner.getPosY();
					posZ = goalOwner.getPosZ();
					goalOwner.setInvulnerable(false);
				}
				
				if(armorTicks > 48 && armorTicks < 70) {
					double r = (armorTicks - 48) * 0.7;
	                for (int a = 1; a <= 360; a += 7) {
	                    double x = posX + (r * Math.cos(Math.toRadians(a)));
	                    double z = posZ + (r * Math.sin(Math.toRadians(a)));
	                    ((ServerWorld)goalOwner.world).spawnParticle(new RedstoneParticleData(1F,0.9F,0.9F,1F), x, posY + 0.2D, z,1, 0.0D, 0.0D, 0.0D,0);
	                }
	                
	                AxisAlignedBB aabb = new AxisAlignedBB(posX, posY, posZ, posX + 1, posY + 1, posZ + 1).grow(r, 0, r);
	        		List<LivingEntity> list = goalOwner.world.getEntitiesWithinAABB(LivingEntity.class, aabb);
	        		list.remove(goalOwner);
	        		
	                for(LivingEntity enemy : list) {
						goalOwner.attackEntityAsMob(enemy);
					}
	                
				}
				
				armorTicks++;
				if(armorTicks >= MAX_ARMOR_TICKS) {
					removeArmor((MarluxiaEntity) goalOwner);
					armorTicks = 0;
				}
			}

			if(isTeleporting()) {
				goalOwner.setNoGravity(true);
				if(tpTicks % 20 == 0) {
					attackWithTP();
				}
				if(tpTicks > 80) {
					goalOwner.setNoGravity(false);
					EntityHelper.setState(goalOwner, 0);

				}
				tpTicks++;
			}
			
			return true;
		}
		
		return false;
	}

	private void attackWithTP() {
		//if(numOfTPs)

		Random rand = goalOwner.world.rand;
		for(int i=0;i<10;i++)
			((ServerWorld) goalOwner.world).spawnParticle(new RedstoneParticleData(1F, 0.6F, 0.6F, 1F), goalOwner.getPosX() - 2 + rand.nextDouble() * 4, goalOwner.getPosY() + rand.nextDouble() * 4, goalOwner.getPosZ() - 2 + rand.nextDouble() * 4, 10, 0.0D, 0.0D, 0.0D, 100);
			
		goalOwner.setPositionAndUpdate(this.goalOwner.getAttackTarget().getPosX(), this.goalOwner.getAttackTarget().getPosY()+1, this.goalOwner.getAttackTarget().getPosZ());
		
		if(this.goalOwner.getAttackTarget().getDistance(goalOwner) <= 1) {
			if(this.goalOwner.getAttackTarget() instanceof PlayerEntity)
				((PlayerEntity)this.goalOwner.getAttackTarget()).travel(new Vector3d(0,2,0));
			this.goalOwner.getAttackTarget().setMotion(0,1.2,0);
			goalOwner.getAttackTarget().attackEntityFrom(DamageSource.MAGIC, 2);

		}
		numOfTPs++;
	}

	public void useArmor(MarluxiaEntity entity) {
		if(armorUses < MAX_ARMOR_USES) {
			armorTicks = 0;
			EntityHelper.setState(entity, 1);
			entity.addPotionEffect(new EffectInstance(Effects.GLOWING, 2000, 200));
			armorUses++;
		}
	}

	public void removeArmor(MarluxiaEntity entity) {
		EntityHelper.setState(entity, 0);
		entity.removePotionEffect(Effects.GLOWING);
	}
	
	public void useTP(MarluxiaEntity entity) {
		EntityHelper.setState(entity, 2);
		tpTicks = 0;
		numOfTPs = 0;
	}

	
	@Override
	public void startExecuting() {
		EntityHelper.setState(this.goalOwner, 0);
		this.goalOwner.setInvulnerable(false);
	}

	private boolean isArmored() {
		return EntityHelper.getState(this.goalOwner) == 1;
	}
	
	private boolean isTeleporting() {
		return EntityHelper.getState(this.goalOwner) == 2;
	}

	@Override
	public boolean shouldExecute() {
		return this.goalOwner.getAttackTarget() != null; //&& this.goalOwner.getDistanceSq(this.goalOwner.getAttackTarget()) < MAX_DISTANCE_FOR_AI;
	}

}