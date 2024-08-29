package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class MinuteBombEntity extends BaseBombEntity {

    public MinuteBombEntity(EntityType<? extends BaseBombEntity> type, Level worldIn) {
        super(type, worldIn);
        xpReward = 6;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture() {
        return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/mob/minute_bomb.png");
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return BaseBombEntity.registerAttributes().add(Attributes.MAX_HEALTH, 15.0D);
    }

    @Override
    public float getExplosionStength() {
        return 2F;
    }
}
