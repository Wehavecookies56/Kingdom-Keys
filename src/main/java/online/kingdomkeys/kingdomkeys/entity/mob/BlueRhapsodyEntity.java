package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.magic.BlizzardEntity;
import online.kingdomkeys.kingdomkeys.entity.magic.FireEntity;

public class BlueRhapsodyEntity extends BaseElementalMusicalHeartlessEntity {

	public BlueRhapsodyEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
		super(type, worldIn);
	}

	public BlueRhapsodyEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
		super(ModEntities.TYPE_BLUE_RHAPSODY.get(), spawnEntity, world);
	}

	@Override
	protected Goal goalToUse() {
		return new BlueRhapsodyGoal(this);
	}

	@Override
	protected double getMaxHP() {
		return 40.0D;
	}

	@Override
	public Element getElementToUse() {
		return Element.BLIZZARD;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ResourceLocation getTexture() {
		return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/blue_rhapsody.png");
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		float multiplier = 1;
		if (!this.world.isRemote) {
			if (source.getImmediateSource() instanceof FireEntity)
				multiplier = 2;
		}
		return super.attackEntityFrom(source, amount * multiplier);
	}

	class BlueRhapsodyGoal extends TargetGoal {
		private int ticksUntilNextAttack, shootTicks = 70;

		private int MAX_DISTANCE_FOR_AI = 100, TIME_BEFORE_NEXT_ATTACK = 70;

		public BlueRhapsodyGoal(BlueRhapsodyEntity e) {
			super(e, true);
			ticksUntilNextAttack = TIME_BEFORE_NEXT_ATTACK;
		}

		@Override
		public boolean shouldExecute() {
			return this.goalOwner.getAttackTarget() != null && this.goalOwner.getDistanceSq(this.goalOwner.getAttackTarget()) < MAX_DISTANCE_FOR_AI;
		}
		@Override
		public boolean shouldContinueExecuting() {
			if (this.goalOwner.getAttackTarget() != null) {
				
				/*if (isUnderground()) {
					this.goalOwner.setInvulnerable(true);

					canUseNextAttack = false;
					if(this.goalOwner.getDistance(this.goalOwner.getAttackTarget()) < 5) {
						this.goalOwner.attackEntityAsMob(this.goalOwner.getAttackTarget());
					} else {
						EntityHelper.setState(this.goalOwner, 0);
						this.goalOwner.setInvulnerable(false);
						shootTicks = TIME_BEFORE_NEXT_ATTACK;
						canUseNextAttack = true;
					}
					
					shootTicks++;
					if (shootTicks >= TIME_UNDERGROUND) { //Go to the surface
						EntityHelper.setState(this.goalOwner, 0);
						this.goalOwner.setInvulnerable(false);

						canUseNextAttack = true;
					}
				}*/
				
				if(this.goalOwner.getDistance(this.goalOwner.getAttackTarget()) < 5) { //If target is in range
					if (this.goalOwner.onGround) {
						//shootTicks--;
						
						if (ticksUntilNextAttack <= 0) {
							if(world.rand.nextInt(100) <= 50) {
								EntityHelper.setState(this.goalOwner, 1);
								this.goalOwner.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);
		                        this.goalOwner.getLookController().setLookPositionWithEntity(target, 0F, 0F);

		                        double d0 = this.goalOwner.getDistanceSq(this.goalOwner.getAttackTarget());
		                        float f = MathHelper.sqrt(MathHelper.sqrt(d0));
		                        double d1 = this.goalOwner.getAttackTarget().getPosX() - this.goalOwner.getPosX();
		                        double d2 = this.goalOwner.getAttackTarget().getBoundingBox().minY + (double) (this.goalOwner.getAttackTarget().getHeight() / 2.0F) - (this.goalOwner.getPosY() + (double) (this.goalOwner.getHeight() / 2.0F));
		                        double d3 = this.goalOwner.getAttackTarget().getPosZ() - this.goalOwner.getPosZ();
		                        BlizzardEntity esfb = new BlizzardEntity(this.goalOwner.world);
		                        esfb.shoot(d1, d2, d3, 1, 0);
		                        esfb.setPosition(esfb.getPosX(), this.goalOwner.getPosY() + (double) (this.goalOwner.getHeight() / 2.0F) + 0.5D, esfb.getPosZ());
		                        this.goalOwner.world.addEntity(esfb);
		                        ticksUntilNextAttack = TIME_BEFORE_NEXT_ATTACK;
							} else {
								if (goalOwner.getDistance(goalOwner.getAttackTarget()) < 8) {
		                            EntityHelper.setState(this.goalOwner, 2);

		                            this.goalOwner.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.0D);

		                            for (LivingEntity enemy : EntityHelper.getEntitiesNear(this.goalOwner, 4))
		                                enemy.attackEntityFrom(DamageSource.causeMobDamage(this.goalOwner), 4);
		                        } else
		                            return false;
							}
							
						}
					}
			
					if (ticksUntilNextAttack > 0) {
						ticksUntilNextAttack--;
						if (ticksUntilNextAttack <= 0) {
							ticksUntilNextAttack = TIME_BEFORE_NEXT_ATTACK;
						}
					}
		
				}

				return true;
			}
			EntityHelper.setState(this.goalOwner, 0);
			this.goalOwner.setInvulnerable(false);
			return false;
		}

		@Override
		public void startExecuting() {
			EntityHelper.setState(this.goalOwner, 0);
			this.goalOwner.setInvulnerable(false);
		}
	}

}
