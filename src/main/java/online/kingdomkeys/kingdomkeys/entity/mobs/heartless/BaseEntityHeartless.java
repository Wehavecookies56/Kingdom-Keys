package online.kingdomkeys.kingdomkeys.entity.mobs.heartless;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;



public class BaseEntityHeartless extends EntityMob {

    protected BaseEntityHeartless(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    @Override
    public void registerAttributes() {
        super.registerAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(1000.0D);
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    @Override
    public boolean isAIDisabled() {
        return false;
    }

   /* @Override
    public boolean getCanSpawnHere()
    {
        return true;
    }*/

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
       /* if (this instanceof IKHMob)
        {
            if (((IKHMob) this).getType() == MobType.HEARTLESS_EMBLEM)
            {
                if (cause.getTrueSource() instanceof EntityPlayer)
                {
                    EntityPlayer player = (EntityPlayer) cause.getTrueSource();
                    if ((player.getHeldItemMainhand() != null
                            && player.getHeldItemMainhand().getItem() instanceof ItemRealKeyblade)
                            || (player.getHeldItemOffhand() != null
                            && player.getHeldItemOffhand().getItem() instanceof ItemRealKeyblade))
                    {
                        if (!world.isRemote)
                        {
                            EntityFlyingHeart heart = new EntityFlyingHeart(this.world);
                            heart.setPosition(this.posX, this.posY + 1, this.posZ);
                            world.spawnEntity(heart);
                        }
                    }
                }
            }
        }*/
    }
}
