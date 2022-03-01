package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.SpawnReason;
import net.minecraft.world.IWorld;
import online.kingdomkeys.kingdomkeys.entity.EntityHelper.MobType;

public interface IKHMob {
    public MobType getMobType();
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn);
    public int getDefense();
}
