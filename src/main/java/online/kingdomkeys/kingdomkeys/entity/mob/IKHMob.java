package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;

public interface IKHMob {
    public MobType getKHMobType();
    public boolean checkSpawnRules(LevelAccessor worldIn, MobSpawnType spawnReasonIn);
    public int getDefense();
}
