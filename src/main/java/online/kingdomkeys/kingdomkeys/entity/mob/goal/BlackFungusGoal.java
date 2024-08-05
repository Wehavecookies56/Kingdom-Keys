package online.kingdomkeys.kingdomkeys.entity.mob.goal;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper;
import online.kingdomkeys.kingdomkeys.util.Utils;

public class BlackFungusGoal extends TargetGoal {
	// 0-Normal, -4 Stonify, -5 Poison
	private final int MAX_NO_AI_DURATION = 6 * 20;
	private final int MAX_STONE_DURATION = 10 * 20;
	private final int MAX_POISON_DURATION = 3 * 20;
	private int ticksToChooseAI = 0, //Ticks in base state after an attack happened
				stoneDuration = 0,
				poisonDuration = 0;

	public BlackFungusGoal(PathfinderMob creature) {
		super(creature, true);
	}

	@Override
	public boolean canContinueToUse() {
		if (this.mob.getTarget() != null) {
			//-4 for stone, -5 for poison
			switch(EntityHelper.getState(mob)){
				case 0:
					if(ticksToChooseAI <= 0) {
						int randomCharade = Utils.randomWithRange(-5,-4);
						EntityHelper.setState(mob,randomCharade);
						ticksToChooseAI = MAX_NO_AI_DURATION;
					} else {
						ticksToChooseAI-=2;
					}
					break;
				case -4: //Stone
					if(stoneDuration <= 0) {
						stoneDuration = MAX_STONE_DURATION;
						EntityHelper.setState(mob,0);
					} else {
						stoneDuration-=2; //Time to complain
					}
					break;
				case -5: //Poison
					if(poisonDuration <= 0) {
						poisonDuration = MAX_POISON_DURATION;
						EntityHelper.setState(mob,0);
						mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2D);

						double X = mob.getX();
						double Y = mob.getY();
						double Z = mob.getZ();

						AreaEffectCloud areaeffectcloud = new AreaEffectCloud(mob.level(),X,Y+0.5D,Z);
						Entity entity = mob;
						if (entity instanceof LivingEntity) {
							areaeffectcloud.setOwner((LivingEntity)entity);
						}

						areaeffectcloud.setRadius(3.0F);
						areaeffectcloud.setRadiusOnUse(-0.5F);
						areaeffectcloud.setWaitTime(10);
						areaeffectcloud.setDuration(6*20);
						areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());
						areaeffectcloud.setPotion(Potions.STRONG_POISON);

						for(MobEffectInstance mobeffectinstance : Potions.STRONG_POISON.getEffects()) {
							areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
						}
						areaeffectcloud.setFixedColor(0xAA00AA);

						mob.level().addFreshEntity(areaeffectcloud);
					} else {
						poisonDuration-=2; //Time to complain
						mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.0D);
					}
					break;
			}
			return true;
		} else { //If no target
			EntityHelper.setState(this.mob, 0);
		}
		return false;
	}

	@Override
	public void start() {
		EntityHelper.setState(this.mob, 0);
	}

	@Override
	public boolean canUse() {
		return this.mob.getTarget() != null;
	}

}