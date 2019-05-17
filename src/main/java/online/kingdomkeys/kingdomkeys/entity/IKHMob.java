package online.kingdomkeys.kingdomkeys.entity;

import net.minecraft.entity.passive.IAnimal;

public interface IKHMob extends IAnimal {
    public EntityHelper.MobType getEnemyType();
}