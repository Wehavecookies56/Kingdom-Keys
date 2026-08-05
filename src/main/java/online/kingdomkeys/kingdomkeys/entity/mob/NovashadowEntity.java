package online.kingdomkeys.kingdomkeys.entity.mob;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class NovashadowEntity extends NeoshadowEntity {

	public NovashadowEntity(EntityType<? extends Monster> type, Level worldIn) {
		super(type, worldIn);
		xpReward = 18;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public ResourceLocation getTexture() {
		return KingdomKeys.rl("textures/entity/mob/novashadow.png");
	}

	public static AttributeSupplier.Builder registerAttributes() {
		return Mob.createLivingAttributes()
				.add(Attributes.FOLLOW_RANGE, 35.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.32D)
				.add(Attributes.MAX_HEALTH, 120.0D)
				.add(Attributes.ATTACK_DAMAGE, 7.0D)
				.add(Attributes.ATTACK_KNOCKBACK, 1.0D);
	}

	@Override
	public int getMaxSpawnClusterSize() {
		return 2;
	}
}
