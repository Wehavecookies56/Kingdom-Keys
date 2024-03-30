package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import online.kingdomkeys.kingdomkeys.capability.IGlobalCapabilities;
import online.kingdomkeys.kingdomkeys.capability.ModCapabilities;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;

public class BaseKHEntity extends Monster implements IKHMob {

	public BaseKHEntity(EntityType<? extends Monster> type, Level worldIn) {
		super(type, worldIn);
	}

	@Override
	public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn) {
		return true;
	}

	@Override
	public int getDefense() {
		return 0;
	}

	@Override
	public MobType getKHMobType() {
		return MobType.NPC;
	}

	
	@Override
	public int getExperienceReward() {
		if (getKHMobType() != MobType.NPC) {
			IGlobalCapabilities mobData = ModCapabilities.getGlobal(this);
			if(mobData != null && mobData.getLevel() > 0) {
				return (int) (super.getExperienceReward() * (mobData.getLevel()/10F));
			}
		}
		return super.getExperienceReward();
	}

}
