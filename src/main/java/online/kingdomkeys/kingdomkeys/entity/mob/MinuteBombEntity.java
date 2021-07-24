package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fmllegacy.network.FMLPlayMessages;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;

public class MinuteBombEntity extends BaseBombEntity {

    public MinuteBombEntity(EntityType<? extends BaseBombEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(KingdomKeys.MODID, "textures/entity/mob/minute_bomb.png");
    }

    public MinuteBombEntity(FMLPlayMessages.SpawnEntity spawnEntity, Level world) {
        super(ModEntities.TYPE_MINUTE_BOMB.get(), spawnEntity, world);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return BaseBombEntity.registerAttributes().add(Attributes.MAX_HEALTH, 20.0D);
    }

    @Override
    public float getExplosionStength() {
        return 2F;
    }
}
