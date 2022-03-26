package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PlayMessages;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class SkaterBombEntity extends BaseBombEntity {

    public SkaterBombEntity(EntityType<? extends BaseBombEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/skater_bomb.png");
    }

    public SkaterBombEntity(PlayMessages.SpawnEntity spawnEntity, Level world) {
        super(ModEntities.TYPE_SKATER_BOMB.get(), spawnEntity, world);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return BaseBombEntity.registerAttributes().add(Attributes.MAX_HEALTH, 32.0D);
    }



    @Override
    public float getExplosionStength() {
        return 3F;
    }
}
