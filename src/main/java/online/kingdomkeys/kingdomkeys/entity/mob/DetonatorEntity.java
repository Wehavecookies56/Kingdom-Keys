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

public class DetonatorEntity extends BaseBombEntity {

    public DetonatorEntity(EntityType<? extends BaseBombEntity> type, Level worldIn) {
        super(type, worldIn);
        xpReward = 10;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture() {
        return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/mob/detonator.png");
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return BaseBombEntity.registerAttributes()
        		.add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public float getExplosionStength() {
        return 5F;
    }
}
