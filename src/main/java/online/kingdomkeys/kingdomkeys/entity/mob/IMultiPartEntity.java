package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import online.kingdomkeys.kingdomkeys.entity.EntityPart;

public interface IMultiPartEntity {

    World getWorld();

    boolean attackEntityFromPart(EntityPart part, DamageSource source, float damage);

}
