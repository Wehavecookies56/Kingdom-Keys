package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class DetonatorEntity extends BaseBombEntity {

    public DetonatorEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/detonator.png");
    }

    public DetonatorEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(ModEntities.TYPE_DETONATOR.get(), spawnEntity, world);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return BaseBombEntity.registerAttributes().createMutableAttribute(Attributes.MAX_HEALTH, 60.0D);
    }

    @Override
    public float getExplosionStength() {
        return 5F;
    }
}
