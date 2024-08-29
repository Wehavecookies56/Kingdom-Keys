package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;
import online.kingdomkeys.kingdomkeys.damagesource.MagicDamageSource;
import online.kingdomkeys.kingdomkeys.entity.ModEntities;
import online.kingdomkeys.kingdomkeys.entity.mob.goal.EmeraldBluesGoal;

public class EmeraldBluesEntity extends BaseElementalMusicalHeartlessEntity {

    public EmeraldBluesEntity(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
        xpReward = 8;
    }

    @Override
    protected Goal goalToUse() {
        return new EmeraldBluesGoal(this);
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return BaseElementalMusicalHeartlessEntity.registerAttributes()
        		.add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D);
    }

    @Override
    public Element getElementToUse() {
        return Element.AERO;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ResourceLocation getTexture() {
        return ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "textures/entity/mob/emerald_blues.png");
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(!this.level().isClientSide()) {
            if(source instanceof MagicDamageSource)
            	return false;
        }
        return super.hurt(source, amount);
    }

}
