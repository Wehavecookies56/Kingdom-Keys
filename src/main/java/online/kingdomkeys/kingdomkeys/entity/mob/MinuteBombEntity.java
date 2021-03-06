package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class MinuteBombEntity extends BaseBombEntity {

    public MinuteBombEntity(EntityType<? extends BaseBombEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/minute_bomb.png");
    }

    public MinuteBombEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(ModEntities.TYPE_MINUTE_BOMB.get(), spawnEntity, world);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return BaseBombEntity.registerAttributes().createMutableAttribute(Attributes.MAX_HEALTH, 20.0D);
    }

    @Override
    public float getExplosionStength() {
        return 2F;
    }
}
