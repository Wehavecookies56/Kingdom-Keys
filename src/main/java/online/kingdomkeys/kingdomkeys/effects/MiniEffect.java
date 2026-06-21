package online.kingdomkeys.kingdomkeys.effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import online.kingdomkeys.kingdomkeys.KingdomKeys;

public class MiniEffect extends MobEffect {

	public MiniEffect(MobEffectCategory category, int color) {
		super(category, color);
		addAttributeModifier(Attributes.SCALE, ResourceLocation.fromNamespaceAndPath(KingdomKeys.MODID, "mini_scale"), -0.6D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
		return false;
	}
}