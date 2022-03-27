package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;

public class BaseKHEntity extends MonsterEntity implements IKHMob {

    public BaseKHEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }
    
    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
    	return true;
    }

	@Override
	public int getDefense() {
		return 0;
	}

	@Override
	public MobType getMobType() {
		return MobType.NPC;
	}

}
